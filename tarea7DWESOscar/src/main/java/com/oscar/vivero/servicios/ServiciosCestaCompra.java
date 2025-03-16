package com.oscar.vivero.servicios;

import org.springframework.stereotype.Service;
import com.oscar.vivero.modelo.CestaCompra;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.repositories.CestaCompraRepository;

import java.util.Map;

@Service
public class ServiciosCestaCompra {

    private final CestaCompraRepository cestaCompraRepository; // Repositorio para manejar la persistencia de CestaCompra
    private final ServiciosPlanta serviciosPlanta;   // Servicio para manejar las plantas

    // Constructor para inyectar el repositorio
    public ServiciosCestaCompra(CestaCompraRepository cestaCompraRepository, ServiciosPlanta serviciosPlanta) {
        this.cestaCompraRepository = cestaCompraRepository; // Repositorio de CestaCompra
        this.serviciosPlanta = serviciosPlanta;             // Servicio de plantas
    }

    // Método para agregar una planta a la cesta
    public void agregarPlanta(String codigoPlanta, int cantidad) {
        if (cantidad <= 0) {
            return; // No agregamos plantas si la cantidad es 0 o negativa
        }

        // Verificar si la planta existe y tiene suficiente stock
        Planta planta = serviciosPlanta.buscarPlantaPorCodigo(codigoPlanta);
        if (planta == null || planta.getCantidadDisponible() < cantidad) {
            throw new IllegalArgumentException("No hay suficiente stock de la planta con código: " + codigoPlanta);
        }

        // Obtener la cesta o crear una nueva si no existe
        CestaCompra cesta = cestaCompraRepository.findById(1L).orElse(new CestaCompra()); // Usamos el id 1 por ejemplo

        // Agregar la planta a la cesta
        cesta.agregarPlanta(codigoPlanta, cantidad);

        // Guardar la cesta actualizada
        cestaCompraRepository.save(cesta);
    }

    // Método para retirar una planta de la cesta
    public void retirarProductoDeCesta(String codigoPlanta) {
        CestaCompra cesta = cestaCompraRepository.findById(1L).orElse(null); // Usamos el id 1 por ejemplo

        if (cesta != null) {
            cesta.retirarPlanta(codigoPlanta);
            cestaCompraRepository.save(cesta);
        }
    }

    // Confirmar el pedido: actualiza las cantidades disponibles de las plantas
    public void confirmarPedido() {
        CestaCompra cesta = cestaCompraRepository.findById(1L).orElse(null); // Usamos el id 1 por ejemplo

        if (cesta != null) {
            Map<String, Integer> productosEnCesta = cesta.getProductos();

            for (Map.Entry<String, Integer> entry : productosEnCesta.entrySet()) {
                String codigoPlanta = entry.getKey();
                int cantidad = entry.getValue();

                Planta planta = serviciosPlanta.buscarPlantaPorCodigo(codigoPlanta);

                if (planta != null) {
                    // Actualizar la cantidad disponible de la planta
                    int nuevaCantidadDisponible = (int) (planta.getCantidadDisponible() - cantidad);
                    planta.setCantidadDisponible(nuevaCantidadDisponible);
                    serviciosPlanta.actualizarCantidadDisponible(planta, nuevaCantidadDisponible);
                }
            }

          
            cesta.vaciarCesta();
            cestaCompraRepository.save(cesta);
        }
    }
    
   


    
    public Map<String, Integer> obtenerProductosCesta() {
        CestaCompra cesta = cestaCompraRepository.findById(1L).orElse(null); // Usamos el id 1 por ejemplo
        return cesta != null ? cesta.getProductos() : null;
    }

    // Vaciar la cesta
    public void vaciarCesta() {
        CestaCompra cesta = cestaCompraRepository.findById(1L).orElse(null); // Usamos el id 1 por ejemplo
        if (cesta != null) {
            cesta.vaciarCesta();
            cestaCompraRepository.save(cesta);
        }
    }
}
