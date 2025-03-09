package com.oscar.vivero.modelo;

import java.util.ArrayList;
import java.util.List;

public class CestaCompra {

	private List<Pedido> pedidos;

	public CestaCompra() {
		this.pedidos = new ArrayList<>();
	}

	public void agregarPedido(Pedido p) {
		this.pedidos.add(p);
	}

	public void retirarPedido(Long pedidoId) {
		this.pedidos.removeIf(pedido -> pedido.getId().equals(pedidoId));
	}

	public List<Pedido> obtenerPedidos() {
		return pedidos;
	}

	public int obtenerCantidadTotal() {
		int total = 0;

		for (Pedido pedido : pedidos) {
			total += pedido.getEjemplares().size();
		}

		return total;

	}

}
