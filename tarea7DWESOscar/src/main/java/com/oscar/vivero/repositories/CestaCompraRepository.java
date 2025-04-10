package com.oscar.vivero.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oscar.vivero.modelo.CestaCompra;

public interface CestaCompraRepository extends JpaRepository<CestaCompra, Long> {

	CestaCompra findByUsuarioAndCodigoPlanta(String usuario, String codigoPlanta);

	void deleteByCodigoPlantaAndUsuario(String codigoPlanta, String usuario);

}
