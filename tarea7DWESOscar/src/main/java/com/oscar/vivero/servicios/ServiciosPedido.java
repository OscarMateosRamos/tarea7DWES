package com.oscar.vivero.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscar.vivero.repositories.PedidoRepository;

@Service
public class ServiciosPedido {
	
	@Autowired
	PedidoRepository pedidorepo;
}
