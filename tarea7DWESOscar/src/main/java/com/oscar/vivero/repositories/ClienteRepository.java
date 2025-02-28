package com.oscar.vivero.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscar.vivero.modelo.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	boolean existsByUsuario(String usuario);

	boolean existsByEmail(String email);

}
