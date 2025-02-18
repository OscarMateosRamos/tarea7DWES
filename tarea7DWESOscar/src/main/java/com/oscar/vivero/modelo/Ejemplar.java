package com.oscar.vivero.modelo;

import java.io.Serializable;
import java.util.List;
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
@Table(name = "ejemplares")
public class Ejemplar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;

	@Column
	private String nombre;

	@ManyToOne
	@JoinColumn(name = "idplanta")
	private Planta planta;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "idmensaje")
	private List<Mensaje> mensajes;

	@ManyToOne
	@JoinColumn(name = "idpedido")
	private Pedido pedido;

	public Ejemplar() {

	}

	public Ejemplar(Long id, String nombre, Planta planta, List<Mensaje> mensajes, Pedido pedido) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.planta = planta;
		this.mensajes = mensajes;
		this.pedido = pedido;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	public List<Mensaje> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	@Override
	public String toString() {
		return "Ejemplar [id=" + id + ", nombre=" + nombre + ", planta=" + planta + ", mensajes=" + mensajes
				+ ", pedido=" + pedido + "]";
	}

}
