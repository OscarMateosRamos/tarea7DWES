package com.oscar.vivero.servicios;

import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.CestaCompra;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.repositories.CestaCompraRepository;

import jakarta.transaction.Transactional;

@Service
public class ServiciosCestaCompra {

	private final CestaCompraRepository cestaCompraRepository;
	private final ServiciosPlanta serviciosPlanta;

	public ServiciosCestaCompra(CestaCompraRepository cestaCompraRepository, ServiciosPlanta serviciosPlanta) {
		this.cestaCompraRepository = cestaCompraRepository;
		this.serviciosPlanta = serviciosPlanta;
	}

	@Transactional // Agrega una nueva Planta a la Cesta
	public void agregarPlanta(String codigoPlanta, int cantidad) {
		if (cantidad <= 0) {
			return;
		}

		Planta planta = serviciosPlanta.buscarPlantaPorCodigo(codigoPlanta);
		if (planta == null || planta.getCantidadDisponible() < cantidad) {
			throw new IllegalArgumentException("No hay suficiente stock de la planta con código: " + codigoPlanta);
		}

		CestaCompra cesta = cestaCompraRepository.findById(1L).orElse(new CestaCompra());

		cesta.setCantidad(cantidad);
		cesta.setCodigoPlanta(codigoPlanta);

		cestaCompraRepository.save(cesta);
	}

	public void insertarCesta(CestaCompra c) {
		cestaCompraRepository.saveAndFlush(c);
	}

	public void actualizarCesta(CestaCompra c) {
		cestaCompraRepository.saveAndFlush(c);
	}

	@Transactional // aumenta la cantida de una Planta en la Cesta
	public void actualizaPlanta(String codigoPlanta, int cantidad, CestaCompra c) {

		Planta planta = serviciosPlanta.buscarPlantaPorCodigo(codigoPlanta);
		if (planta == null || planta.getCantidadDisponible() < cantidad) {
			throw new IllegalArgumentException("No hay suficiente stock de la planta con código: " + codigoPlanta);
		}

		CestaCompra cesta = cestaCompraRepository.findById(1L).orElse(new CestaCompra());

		cesta.setCantidad(cantidad);
		cesta.setCodigoPlanta(codigoPlanta);

		cestaCompraRepository.save(cesta);
	}
	
	@Transactional	
	public void eliminarDeCesta(String codigoPlanta, String usuario) {
	    cestaCompraRepository.deleteByCodigoPlantaAndUsuario(codigoPlanta, usuario);
	}


//	@Transactional
//	public void retirarProductoDeCesta(String codigoPlanta) {
//		CestaCompra cesta = cestaCompraRepository.findById(1L).orElse(null);
//
//		if (cesta != null) {
//			cesta.retirarPlanta(codigoPlanta);
//			cestaCompraRepository.save(cesta);
//		}
//	}

//	@Transactional
//	public void confirmarPedido() {
//		CestaCompra cesta = cestaCompraRepository.findById(1L).orElse(null);
//
//		if (cesta != null) {
//			Map<String, Integer> productosEnCesta = cesta.getProductos();
//
//			for (Map.Entry<String, Integer> entry : productosEnCesta.entrySet()) {
//				String codigoPlanta = entry.getKey();
//				int cantidad = entry.getValue();
//
//				Planta planta = serviciosPlanta.buscarPlantaPorCodigo(codigoPlanta);
//
//				if (planta != null) {
//
//					int nuevaCantidadDisponible = (int) (planta.getCantidadDisponible() - cantidad);
//					planta.setCantidadDisponible(nuevaCantidadDisponible);
//					serviciosPlanta.actualizarCantidadDisponible(planta, nuevaCantidadDisponible);
//				}
//			}
//
//			cesta.vaciarCesta();
//			cestaCompraRepository.save(cesta);
//		}
//	}
//	public Map<String, Integer> obtenerProductosCesta(Long usuarioId) {
//	    // Obtener la cesta asociada al usuario autenticado
//	    CestaCompra cesta = cestaCompraRepository.findById(usuarioId).orElse(null);
//	    
//	    // Si la cesta existe, devolver los productos, sino devolver null
//	    return cesta != null ? cesta.getProductos() : null;
//	}
//
//
//	public void vaciarCesta() {
//		CestaCompra cesta = cestaCompraRepository.findById(1L).orElse(null);
//		if (cesta != null) {
//			cesta.vaciarCesta();
//			cestaCompraRepository.save(cesta);
//		}
//	}

}
