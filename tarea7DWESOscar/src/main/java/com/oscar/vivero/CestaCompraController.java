package com.oscar.vivero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oscar.vivero.modelo.CestaCompra;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.servicios.ServiciosCestaCompra;
import com.oscar.vivero.servicios.ServiciosPedido;
import com.oscar.vivero.servicios.ServiciosPlanta;

import jakarta.servlet.http.HttpSession;

@Controller
public class CestaCompraController {
	
    @Autowired
    private ServiciosPedido servPedido;
	
    @Autowired
    private ServiciosPlanta servPlanta;

    @Autowired
    private ServiciosCestaCompra servCesta;
    
    @GetMapping("/CestaCompra")
    public String mostrarCesta(Model model) {
        model.addAttribute("cesta", servCesta.obtenerProductosCesta());
        return "CestaCompra"; 
    }
    
    @PostMapping("/eliminarDeCesta")
    public String eliminarProducto(@RequestParam("plantaId") String plantaId, Model model) {
        Planta planta = servPlanta.buscarPlantaPorCodigo(plantaId);
        if (planta != null) {
            servCesta.retirarProductoDeCesta(planta);
        }
        return "redirect:/CestaCompra";
    }

   
    @PostMapping("/vaciarCesta")
    public String vaciarCesta() {
        servCesta.vaciarCesta();
        return "redirect:/CestaCompra";
    }

   
    
    @PostMapping("/confirmarCompra")
    public String confirmarCompra(HttpSession session, Model model) {
        CestaCompra cesta = (CestaCompra) servCesta.obtenerProductosCesta();
        
        if (cesta.obtenerProductos().isEmpty()) {
            model.addAttribute("mensajeError", "La cesta está vacía.");
            return "CestaCompra";
        }

        Pedido pedido = servPedido.crearPedidoDesdeCesta(cesta);
        servPedido.insertarPedido(pedido);
        servCesta.vaciarCesta();
        
        model.addAttribute("mensajeExito", "Pedido realizado con éxito.");
        return "redirect:/CestaCompra";
    }
}
