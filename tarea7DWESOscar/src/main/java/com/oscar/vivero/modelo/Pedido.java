package com.oscar.vivero.modelo;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedido")
public class Pedido {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "fecha")
	private Date fecha;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "idejemplar")
	private List<Ejemplar> ejemplares = new LinkedList<Ejemplar>();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "idcliente")
	private List<Cliente> clientes = new LinkedList<Cliente>();

	@ElementCollection
	@MapKeyColumn(name = "idejemplar")
	@Column(name = "cantidad")
	private Map<String, Integer> cantidades;

	public Pedido() {

	}

	public Pedido(Long id, Date fecha, List<Ejemplar> ejemplares, List<Cliente> clientes,
			Map<String, Integer> cantidades) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.ejemplares = ejemplares;
		this.clientes = clientes;
		this.cantidades = cantidades;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public List<Ejemplar> getEjemplares() {
		return ejemplares;
	}

	public void setEjemplares(List<Ejemplar> ejemplares) {
		this.ejemplares = ejemplares;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public Map<String, Integer> getCantidades() {
		return cantidades;
	}

	public void setCantidades(Map<String, Integer> cantidades) {
		this.cantidades = cantidades;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cantidades, clientes, ejemplares, fecha, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pedido other = (Pedido) obj;
		return Objects.equals(cantidades, other.cantidades) && Objects.equals(clientes, other.clientes)
				&& Objects.equals(ejemplares, other.ejemplares) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", fecha=" + fecha + ", ejemplares=" + ejemplares + ", clientes=" + clientes
				+ ", cantidades=" + cantidades + "]";
	}

}
