package com.oscar.vivero.servicios;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.CestaCompra;
import com.oscar.vivero.modelo.Pedido;

@Service
public class ServiciosCestaCompra {

	private static final List<Pedido> pedidos = null;
	private CestaCompra cestaCompra = new CestaCompra();

	public boolean agregarPedidoAlCarrito(Pedido pedido) {
		if (pedido != null) {
			cestaCompra.agregarPedido(pedido);
			return true;
		}
		return false;
	}

	public boolean añadirPedidoACesta(Pedido pedido) {
        if (pedido != null) {
            cestaCompra.agregarPedido(pedido);
            return true; 
        }
        return false; 
    }

	public List<Pedido> obtenerPedidosEnCesta() {
        return this.pedidos;  
    }
	
	public int obtenerCantidadTotal() {
        int total = 0;
        for (Pedido pedido : pedidos) {
            total += pedido.getEjemplares().size();
        }
        return total;
    }

    public void vaciarCesta() {
        this.pedidos.clear();
    }

    public boolean estaVacia() {
        return this.pedidos.isEmpty();
    }
	
}
