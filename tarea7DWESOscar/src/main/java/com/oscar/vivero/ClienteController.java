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

		String nombre = RegistroCliente.getNombre();
		Date fechanac = RegistroCliente.getFechanac();
		String nif = RegistroCliente.getNif();
		String direccion = RegistroCliente.getDireccion();
		String email = RegistroCliente.getEmail();
		String telefono = RegistroCliente.getTelefono();

		String usuario = RegistroCliente.getCredencial().getUsuario();
		String password = RegistroCliente.getCredencial().getPassword();

		Cliente c = new Cliente();
		c.setNombre(nombre);
		c.setFechanac(fechanac);
		c.setNif(nif);
		c.setDireccion(direccion);
		c.setEmail(email);
		c.setTelefono(telefono);

		Credenciales cr = new Credenciales();
		cr.setUsuario(usuario);
		cr.setPassword(password);
		cr.setRol("cliente");

		c.setCredencial(cr);

		boolean camposValidos = servcliente.validarCliente(nombre, email, nif, telefono, direccion, usuario, password);

		if (!camposValidos) {

			model.addAttribute("mensajeError", "Campos del Cliente inválidos.");
			model.addAttribute("cliente", RegistroCliente);
			return "RegistroCliente";
		}

		try {
			servcliente.insertarCliente(c);
			servCredenciales.insertarCredencial(cr);

			model.addAttribute("mensajeExito", "Cliente añadido correctamente.");
			model.addAttribute("cliente", new Cliente());
		} catch (Exception e) {

			model.addAttribute("mensajeError", "Hubo un error al registrar el cliente. Por favor, intente nuevamente.");
			model.addAttribute("cliente", RegistroCliente);
		}

		return "RegistroCliente";
	}

	@GetMapping("/ClienteRegistro")
	public String mostrarFormularioRegistroCliente(Model model) {

		model.addAttribute("cliente", new Cliente());
		return "RegistroCliente";
	}

}
