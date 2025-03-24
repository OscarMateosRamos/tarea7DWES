package com.oscar.vivero.modelo;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "cesta_compra")
public class CestaCompra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	
	@ElementCollection
	@MapKeyColumn(name = "codigo_planta")  
	@Column(name = "cantidad") 
	@CollectionTable(name = "cesta_compra_productos", joinColumns = @JoinColumn(name = "cesta_id"))
	private Map<String, Integer> productosEnCesta = new HashMap<>(); 

	public CestaCompra() {
	}

	public CestaCompra(Long id, Map<String, Integer> productosEnCesta) {
		super();
		this.id = id;
		this.productosEnCesta = productosEnCesta;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, Integer> getProductos() {
		return productosEnCesta;
	}

	public void setProductos(Map<String, Integer> productos) {
		this.productosEnCesta = productos;
	}

	public void agregarPlanta(String codigoPlanta, int cantidad) {
	   
	    productosEnCesta.merge(codigoPlanta, cantidad, Integer::sum);
	}

	public void retirarPlanta(String codigoPlanta) {
	   
	    productosEnCesta.remove(codigoPlanta);
	}

	public void vaciarCesta() {
		productosEnCesta.clear();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, productosEnCesta);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CestaCompra that = (CestaCompra) obj;
		return Objects.equals(id, that.id) && Objects.equals(productosEnCesta, that.productosEnCesta);
	}

	@Override
	public String toString() {
		return "CestaCompra{id=" + id + ", productos=" + productosEnCesta + "}";
	}
}
