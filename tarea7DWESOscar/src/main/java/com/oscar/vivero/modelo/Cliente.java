package com.oscar.vivero.modelo;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "fechanac")
	private Date fechanac;

	@Column(name = "nif", unique = true)
	private String nif;

	@Column(name = "direccion")
	private String direccion;

	@Column(name = "email")
	private String email;

	@Column(name = "telefono")
	private String telefono;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idcredencial")
	private Credenciales credencial;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idpedido")
	private Pedido pedidos;

	public Cliente() {

	}

	public Cliente(Long id, String nombre, Date fechanac, String nif, String direccion, String email, String telefono,
			Credenciales credencial, Pedido pedidos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fechanac = fechanac;
		this.nif = nif;
		this.direccion = direccion;
		this.email = email;
		this.telefono = telefono;
		this.credencial = credencial;
		this.pedidos = pedidos;
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

	public Date getFechanac() {
		return fechanac;
	}

	public void setFechanac(Date fechanac) {
		this.fechanac = fechanac;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Credenciales getCredencial() {
		return credencial;
	}

	public void setCredencial(Credenciales credencial) {
		this.credencial = credencial;
	}

	public Pedido getPedidos() {
		return pedidos;
	}

	public void setPedidos(Pedido pedidos) {
		this.pedidos = pedidos;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nombre=" + nombre + ", fechanac=" + fechanac + ", nif=" + nif + ", direccion="
				+ direccion + ", email=" + email + ", telefono=" + telefono + ", credencial=" + credencial
				+ ", pedidos=" + pedidos + "]";
	}

}
