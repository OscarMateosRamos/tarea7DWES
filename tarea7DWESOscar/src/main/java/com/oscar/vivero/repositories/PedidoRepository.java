package com.oscar.vivero.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Pedido;

@Service
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
