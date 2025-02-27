package com.oscar.vivero;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.Cliente;
import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.servicios.ServiciosCliente;
import com.oscar.vivero.servicios.ServiciosCredenciales;

@Controller
public class ClienteController {

	@Autowired
	ServiciosCliente servcliente;

	@Autowired
	ServiciosCredenciales servCredenciales;

	@PostMapping("/CamposCliente")
	public String RegistrarCliente(@ModelAttribute Cliente RegistroCliente, Model model) {

		Cliente c = new Cliente();

		String nombre = RegistroCliente.getNombre();
		Date fecahanac = RegistroCliente.getFechanac();
		String nif = RegistroCliente.getNif();
		String direccion = RegistroCliente.getDireccion();
		String email = RegistroCliente.getEmail();
		String telefono = RegistroCliente.getTelefono();
		String usuario = RegistroCliente.getCredencial().getUsuario();
		String password = RegistroCliente.getCredencial().getPassword();

		c.setNombre(nombre);
		c.setFechanac(fecahanac);
		c.setNif(nif);
		c.setDireccion(direccion);
		c.setEmail(email);
		c.setTelefono(telefono);

		Credenciales cr = new Credenciales();

		cr.setUsuario(usuario);
		cr.setPassword(password);

		servCredenciales.insertarCredencial(cr);
		c.setCredencial(cr);

		boolean camposValidos = servcliente.validarCliente(nombre, email, telefono, direccion, nif);

		if (!camposValidos) {
			model.addAttribute("error", " campos del Cliente Invalidos.");
			return "RegistroCliente";
		}

		servcliente.insertarCliente(c);

		return "/RegistroCliente";
	}

	@GetMapping("/ClienteRegistro")
	public String mostrarRegistroCliente(Model model) {
		model.addAttribute("cliente", new Cliente());
		return "RegistroCliente";
	}

}
