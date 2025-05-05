package com.oscar.vivero.modelo;

import java.io.Serializable;
import java.util.Objects;

public class CestaCompra implements Serializable{

	private String codigoPlanta;

	private int cantidad;

	public CestaCompra() {
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

	@Override
	public int hashCode() {
		return Objects.hash(cantidad, codigoPlanta);
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
		return cantidad == other.cantidad && Objects.equals(codigoPlanta, other.codigoPlanta);
	}

}
