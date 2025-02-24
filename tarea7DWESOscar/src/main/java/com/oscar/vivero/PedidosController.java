package com.oscar.vivero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.oscar.vivero.servicios.ServiciosPedido;

@Controller
public class PedidosController {

	@Autowired
	ServiciosPedido servPedidos;
	
	
}
