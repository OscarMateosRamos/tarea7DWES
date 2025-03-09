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
    @JoinColumn(name = "idejemplar")  // Esto debería hacer referencia a la relación con el ejemplar.
    private List<Mensaje> mensajes;

    @ManyToOne
    @JoinColumn(name = "idpedido")
    private Pedido pedido;

  
    @Column(nullable = false)
    private boolean disponible = true;  

    
    @Column
    private String anotacion;   

    public Ejemplar() {}

    public Ejemplar(Long id, String nombre, Planta planta, List<Mensaje> mensajes, Pedido pedido, boolean disponible, String anotacion) {
        this.id = id;
        this.nombre = nombre;
        this.planta = planta;
        this.mensajes = mensajes;
        this.pedido = pedido;
        this.disponible = disponible;
        this.anotacion = anotacion;
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

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getAnotacion() {
        return anotacion;
    }

    public void setAnotacion(String anotacion) {
        this.anotacion = anotacion;
    }

    @Override
    public String toString() {
        return "Ejemplar [id=" + id + ", nombre=" + nombre + ", planta=" + planta + ", mensajes=" + mensajes
                + ", pedido=" + pedido + ", disponible=" + disponible + ", anotacion=" + anotacion + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ejemplar ejemplar = (Ejemplar) o;
        return disponible == ejemplar.disponible && Objects.equals(id, ejemplar.id) && Objects.equals(nombre, ejemplar.nombre) && Objects.equals(planta, ejemplar.planta) && Objects.equals(mensajes, ejemplar.mensajes) && Objects.equals(pedido, ejemplar.pedido) && Objects.equals(anotacion, ejemplar.anotacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, planta, mensajes, pedido, disponible, anotacion);
    }
}
