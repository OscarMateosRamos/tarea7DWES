package com.oscar.vivero.modelo;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cesta_compra")
public class CestaCompra implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "CodigoPlanta")
	private String codigoPlanta;

	@Column(name = "cantidad")
	private int cantidad;

	@Column(name = "usuario")
	private String usuario;

	public CestaCompra() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoPlanta() {
		return codigoPlanta;
	}

	public void setCodigoPlanta(String codigoPlanta) {
		this.codigoPlanta = codigoPlanta;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cantidad, codigoPlanta, id, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CestaCompra other = (CestaCompra) obj;
		return cantidad == other.cantidad && Objects.equals(codigoPlanta, other.codigoPlanta)
				&& Objects.equals(id, other.id) && Objects.equals(usuario, other.usuario);
	}

	@Override
	public String toString() {
		return "CestaCompra [id=" + id + ", codigoPlanta=" + codigoPlanta + ", cantidad=" + cantidad + ", usuario="
				+ usuario + "]";
	}

}
