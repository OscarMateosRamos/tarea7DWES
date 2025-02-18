package com.oscar.vivero.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oscar.vivero.modelo.Ejemplar;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {

	@Query("SELECT e FROM Ejemplar e WHERE e.planta.codigo = :codigoPlanta")
	List<Ejemplar> ejemplaresPorTipoPlanta(@Param("codigoPlanta") String codigoPlanta);

	Optional<Ejemplar> findByNombre(String nombre);

}
