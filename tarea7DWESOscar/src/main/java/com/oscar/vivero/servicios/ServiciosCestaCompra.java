package com.oscar.vivero.servicios;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Planta;

@Service
public class ServiciosCestaCompra {

    private Map<Planta, Integer> productos;

    public ServiciosCestaCompra() {
        this.productos = new HashMap<>();
    }

    public void agregarPlanta(Planta planta, int cantidad) {
        if (cantidad <= 0) {
            return;
        }

        // Agregar o actualizar la cantidad de la planta en la cesta
        productos.put(planta, productos.getOrDefault(planta, 0) + cantidad);

        // Reducir la cantidad disponible en la planta
        int nuevaCantidadDisponible = (int) (planta.getCantidadDisponible() - cantidad);
        planta.setCantidadDisponible(Math.max(nuevaCantidadDisponible, 0));
    }

    public void retirarProductoDeCesta(Planta planta) {
        productos.remove(planta); // Se corrige para eliminar correctamente
    }

    public Map<Planta, Integer> obtenerProductosCesta() {
        return productos;
    }
}
