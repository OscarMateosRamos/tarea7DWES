package com.oscar.vivero.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscar.vivero.modelo.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	boolean existsByEmail(String email);

	Optional<Cliente> findByCredencial(Long id);

	
}
