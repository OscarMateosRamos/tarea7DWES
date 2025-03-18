package com.oscar.vivero;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.servicios.ServiciosCredenciales;

@Controller
public class MainController {

    @Autowired
    ServiciosCredenciales servCredenciales;

    @GetMapping({ "/", "MenuInvitado" })
    public String MenuInvitado() {
        return "MenuInvitado";
    }

    @GetMapping("/MenuPersonal")
    public String mostrarMenuPersonal() {
        return "MenuPersonal";
    }

    @GetMapping("/MenuAdmin")
    public String mostrarMenuAdmin() {
        return "MenuAdmin";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("credenciales", new Credenciales());
        return "formularioLogin";
    }

    @PostMapping("/Sesion")
    public String logInSubmit(@ModelAttribute Credenciales formularioLogin, Model model, HttpSession session) {
        String usuario = formularioLogin.getUsuario();
        String password = formularioLogin.getPassword();

        // Validar que los campos no estén vacíos
        if (usuario == null || password == null || usuario.trim().isEmpty() || password.trim().isEmpty()) {
            model.addAttribute("error", "Por favor, ingrese ambos campos.");
            return "formularioLogin";
        }

        // Buscar las credenciales y validar
        Credenciales credencial = servCredenciales.buscarCredencialPorUsuario(usuario);
        if (credencial == null || !credencial.getPassword().equals(password)) {
            model.addAttribute("error", "Credenciales incorrectas");
            return "formularioLogin";
        }

     
        SecurityContextHolder.getContext().setAuthentication(((SecurityContext) credencial).getAuthentication());

        String rol = credencial.getRol();
        session.setAttribute("usuario", usuario);
        session.setAttribute("rol", rol);

        // Redirigir según el rol del usuario
        switch (rol.toLowerCase()) {
            case "admin":
                System.out.println("--Bienvenido Admin--");
                return "redirect:/MenuAdmin";
            case "personal":
                System.out.println("--Bienvenido Personal--");
                return "redirect:/MenuPersonal";
            case "cliente":
                System.out.println("--Bienvenido Cliente--");
                return "redirect:/RealizarPedido";
            default:
                System.out.println("--Rol no reconocido--");
                model.addAttribute("error", "Rol no reconocido.");
                return "formularioLogin";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        // Limpiar el contexto de seguridad
        SecurityContextHolder.clearContext();
        session.invalidate();
        return "redirect:/MenuInvitado";
    }
}
