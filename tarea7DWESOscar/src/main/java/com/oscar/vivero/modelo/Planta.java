package com.oscar.vivero.modelo;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "plantas")
public class Planta {

	@Id
	@Column(unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String codigo;

	@Column
	private String nombrecomun;

	@Column
	private String nombrecientifico;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "idplanta")
	private List<Ejemplar> ejemplares = new LinkedList<Ejemplar>();

	public Planta() {

	}

	public Planta(Long id, String codigo, String nombrecomun, String nombrecientifico, List<Ejemplar> ejemplares) {
		this.id = id;
		this.codigo = codigo;
		this.nombrecomun = nombrecomun;
		this.nombrecientifico = nombrecientifico;
		this.ejemplares = ejemplares;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombrecomun() {
		return nombrecomun;
	}

	public void setNombrecomun(String nombrecomun) {
		this.nombrecomun = nombrecomun;
	}

	public String getNombrecientifico() {
		return nombrecientifico;
	}

	public void setNombrecientifico(String nombrecientifico) {
		this.nombrecientifico = nombrecientifico;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Ejemplar> getEjemplares() {
		return ejemplares;
	}

	public void setEjemplares(List<Ejemplar> ejemplares) {
		this.ejemplares = ejemplares;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo, ejemplares, id, nombrecientifico, nombrecomun);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Planta other = (Planta) obj;
		return Objects.equals(codigo, other.codigo) && Objects.equals(ejemplares, other.ejemplares)
				&& Objects.equals(id, other.id) && Objects.equals(nombrecientifico, other.nombrecientifico)
				&& Objects.equals(nombrecomun, other.nombrecomun);
	}

	@Override
	public String toString() {
		return "Planta [id=" + id + ", codigo=" + codigo + ", nombrecomun=" + nombrecomun + ", nombrecientifico="
				+ nombrecientifico + "]";
	}

	

}
