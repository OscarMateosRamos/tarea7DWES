package com.oscar.vivero.servicios;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Cliente;
import com.oscar.vivero.repositories.ClienteRepository;

@Service
public class ServiciosCliente {

	@Autowired
	ClienteRepository clienterepo;

	

	public boolean validarCliente(String nombre, String email, String nif, String telefono,String direccion) {

	   
	    if (nombre.length() > 255) {
	        System.out.println("Nombre inválido");
	        return false;
	    }

	    
	    if (email.length() > 255) {
	        System.out.println("Email inválido");
	        return false;
	    }
	    
	    if (direccion.length() > 255) {
	        System.out.println("Direccion inválida");
	        return false;
	    }

	    String patronEmail = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
	    Pattern patternEmail = Pattern.compile(patronEmail);
	    Matcher matcherEmail = patternEmail.matcher(email);
	    if (!matcherEmail.matches()) {
	        System.out.println("Formato de email inválido: " + email);
	        return false;
	    }

	  
	    String patronNIF = "^(\\d{8}[A-Z]|[XYZ]\\d{7}[A-Z])$";
	    Pattern patternNIF = Pattern.compile(patronNIF);
	    Matcher matcherNIF = patternNIF.matcher(nif);
	    if (!matcherNIF.matches()) {
	        System.out.println("Formato de NIF inválido: " + nif);
	        return false;
	    }

	  
	    String patronTelefono = "^[6789]\\d{8}$";
	    Pattern patternTelefono = Pattern.compile(patronTelefono);
	    Matcher matcherTelefono = patternTelefono.matcher(telefono);
	    if (!matcherTelefono.matches()) {
	        System.out.println("Formato de teléfono inválido: " + telefono);
	        return false;
	    }

	  
	    List<Cliente> clientes = clienterepo.findAll();
	    for (Cliente c : clientes) {
	        if (c.getEmail().equals(email)) {
	            System.out.println("El email ya existe: " + email);
	            return false;
	        }
	    }

	    return true;
	}


	public void insertarCliente(Cliente c) {
		clienterepo.saveAndFlush(c);
	}

}
