package com.oscar.vivero.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscar.vivero.repositories.ClienteRepository;

@Service
public class ServiciosCliente {

	@Autowired
	ClienteRepository credencialrepo;
	
}
