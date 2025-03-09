package com.oscar.vivero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.servicios.ServiciosPedido;

@Controller
public class ConfirmarPedidoController {

    @Autowired
    private ServiciosPedido pedidoserv;  
   
    
    @GetMapping("/confirmarPedido")
    public String confirmarPedido(Model model) {
       
        Pedido pedido = pedidoserv.obtenerUltimoPedido();
        
     
        model.addAttribute("pedido", pedido);
        
      
        return "ConfirmacionPedido";
    }

   
    @PostMapping("/agregarACesta")
    public String agregarACesta() {
    
        	pedidoserv.agregarPedidoACesta(null);

       
        return "redirect:/cestaCompra";
    }
}
