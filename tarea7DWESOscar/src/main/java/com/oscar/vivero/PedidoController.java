package com.oscar.vivero;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oscar.vivero.modelo.CestaCompra;
import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.servicios.ServiciosEjemplar;
import com.oscar.vivero.servicios.ServiciosPedido;
import com.oscar.vivero.servicios.ServiciosPlanta;

import jakarta.servlet.http.HttpSession;

@Controller
public class PedidoController {

    @Autowired
    private ServiciosPedido servPedido;

    @Autowired
    private ServiciosEjemplar servEjemplar;

    @Autowired
    private ServiciosPlanta servPlanta;

    @GetMapping("/RealizarPedido")
    public String mostrarRealizarPedido(HttpSession session, Model model) {
        List<Planta> plantas = servPlanta.obtenerPlantasConEjemplares();
        List<CestaCompra> lista = (List<CestaCompra>) session.getAttribute("lista");
        String usuario = (String) session.getAttribute("usuario");

        for (Planta planta : plantas) {
            long enCesta = 0;
            long cantidadDisponible = planta.getEjemplares().stream().filter(Ejemplar::isDisponible).count();

            if (lista != null) {
                for (CestaCompra cestaCompra : lista) {
                    if (cestaCompra.getCodigoPlanta().equalsIgnoreCase(planta.getCodigo())) {
                        enCesta += cestaCompra.getCantidad();
                    }
                }
            }
            planta.setCantidadDisponible((int) (cantidadDisponible - enCesta));
        }

        model.addAttribute("plantas", plantas);
        model.addAttribute("usuario", usuario);
        return "/cliente/RealizarPedido";
    }

    @PostMapping("/añadirACesta")
    public String añadirACesta(@RequestParam("codigo") String codigo, @RequestParam("cantidad") int cantidad,
                               HttpSession session, Model model) {

        if (cantidad <= 0) {
            model.addAttribute("error", "La cantidad debe ser mayor que cero.");
            return "redirect:/RealizarPedido";
        }

        ArrayList<CestaCompra> lista = (ArrayList<CestaCompra>) session.getAttribute("lista");
        if (lista == null) {
            lista = new ArrayList<>();
        }

        String usuario = (String) session.getAttribute("usuario");
        if (usuario == null) {
            model.addAttribute("error", "Debes estar autenticado para añadir productos a la cesta.");
            return "login";
        }

        boolean existe = false;
        for (CestaCompra item : lista) {
            if (item.getCodigoPlanta().equals(codigo)) {
                item.setCantidad(item.getCantidad() + cantidad);
                existe = true;
                break;
            }
        }

        if (!existe) {
            CestaCompra c = new CestaCompra();
            c.setCodigoPlanta(codigo);
            c.setCantidad(cantidad);
            lista.add(c);
        }

        session.setAttribute("lista", lista);
        model.addAttribute("success", "Producto añadido a la cesta con éxito.");

        return "redirect:/CestaCompra";
    }
}
