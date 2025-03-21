package com.oscar.vivero.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscar.vivero.modelo.Credenciales;

@Repository
public interface CredencialRepository extends JpaRepository<Credenciales, Long> {

	Credenciales findByUsuario(String usuario);

	boolean existsByUsuario(String usuario);

	//boolean existsById(String usuario);



}
