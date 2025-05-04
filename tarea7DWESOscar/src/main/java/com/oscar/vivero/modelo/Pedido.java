package com.oscar.vivero.modelo;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "fecha")
	private Date fecha;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	@OneToMany
	@JoinColumn(name = "idEjemplar")
	private List<Ejemplar> ejemplares;

	@Column(name = "estado")
	private String estado;

	public Pedido() {
		super();
	}

	public Pedido(Long id, Date fecha, Cliente cliente, List<Ejemplar> ejemplares, String estado) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.cliente = cliente;
		this.ejemplares = ejemplares;
		this.estado = estado;
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cliente, estado, fecha, id);
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
		return Objects.equals(cliente, other.cliente) && Objects.equals(estado, other.estado)
				&& Objects.equals(fecha, other.fecha) && Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", fecha=" + fecha + ", cliente=" + cliente + ", estado=" + estado + "]";
	}

}
