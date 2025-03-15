package com.oscar.vivero.modelo;

import java.util.HashMap;
import java.util.Map;

public class CestaCompra {

    private Map<Planta, Integer> productos; 

    public CestaCompra() {
        this.productos = new HashMap<>();
    }

       public void retirarPlanta(Planta planta) {
        productos.remove(planta);
    }

    public Map<Planta, Integer> obtenerProductos() {
        return productos;
    }

    public int obtenerCantidadTotal() {
        return productos.values().stream().mapToInt(Integer::intValue).sum();
    }
}
