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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.MapKeyColumn;
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

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "pedido_id")
	private List<Ejemplar> ejemplares = new LinkedList<>();

	@ElementCollection
	@MapKeyColumn(name = "ejemplar_id")
	@Column(name = "cantidad")
	private Map<String, Integer> cantidades;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
	private List<Mensaje> anotacion = new LinkedList<>();

	private List<Ejemplar> ejemplaresEnPedido = new LinkedList<>();
	
	
	
	public Pedido() {
		super();
	}

	public Pedido(Date fecha, Cliente cliente, List<Ejemplar> ejemplares, Map<String, Integer> cantidades,
			List<Mensaje> anotacion, List<Ejemplar> ejemplaresEnPedido) {
		super();
		this.fecha = fecha;
		this.cliente = cliente;
		this.ejemplares = ejemplares;
		this.cantidades = cantidades;
		this.anotacion = anotacion;
		this.ejemplaresEnPedido = ejemplaresEnPedido;
	}

	public void agregarEjemplar(Ejemplar ejemplar, Integer cantidad) {
		if (ejemplar == null || cantidad == null || cantidad <= 0) {
			return;
		}

		for (int i = 0; i < cantidad; i++) {
			ejemplaresEnPedido.add(ejemplar);
		}
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

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Ejemplar> getEjemplares() {
		return ejemplares;
	}

	public void setEjemplares(List<Ejemplar> ejemplares) {
		this.ejemplares = ejemplares;
	}

	public Map<String, Integer> getCantidades() {
		return cantidades;
	}

	public void setCantidades(Map<String, Integer> cantidades) {
		this.cantidades = cantidades;
	}

	public List<Mensaje> getAnotacion() {
		return anotacion;
	}

	public void setAnotacion(List<Mensaje> anotacion) {
		this.anotacion = anotacion;
	}

	public List<Ejemplar> getEjemplaresEnPedido() {
		return ejemplaresEnPedido;
	}

	public void setEjemplaresEnPedido(List<Ejemplar> ejemplaresEnPedido) {
		this.ejemplaresEnPedido = ejemplaresEnPedido;
	}

	@Override
	public int hashCode() {
		return Objects.hash(anotacion, cantidades, cliente, ejemplares, ejemplaresEnPedido, fecha, id);
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
		return Objects.equals(anotacion, other.anotacion) && Objects.equals(cantidades, other.cantidades)
				&& Objects.equals(cliente, other.cliente) && Objects.equals(ejemplares, other.ejemplares)
				&& Objects.equals(ejemplaresEnPedido, other.ejemplaresEnPedido) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", fecha=" + fecha + ", cliente=" + cliente + ", ejemplares=" + ejemplares
				+ ", cantidades=" + cantidades + ", anotacion=" + anotacion + ", ejemplaresEnPedido="
				+ ejemplaresEnPedido + "]";
	}
}
