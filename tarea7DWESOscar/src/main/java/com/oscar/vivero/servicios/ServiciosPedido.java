package com.oscar.vivero.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.repositories.PedidoRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class ServiciosPedido {

	@Autowired
	PedidoRepository pedidorepo;

	@Autowired
	HttpSession session;

	public void insertarPedido(Pedido p) {
		pedidorepo.saveAndFlush(p);
	}

	public Pedido obtenerUltimoPedido() {
		return pedidorepo.findTopByOrderByFechaDesc();
	}

	public void agregarPedidoACesta(Pedido pedido) {

		List<Pedido> cesta = (List<Pedido>) session.getAttribute("cesta");

		if (cesta == null) {
			cesta = new ArrayList<>();
		}

		cesta.add(pedido);

		session.setAttribute("cesta", cesta);
	}

    public Pedido obtenerPedidoPorId(Long idPedido) {
        
        return pedidorepo.findById(idPedido).orElse(null);
    }
    
  
}
