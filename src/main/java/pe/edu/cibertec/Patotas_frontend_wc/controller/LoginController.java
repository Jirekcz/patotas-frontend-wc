package pe.edu.cibertec.Patotas_frontend_wc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.Patotas_frontend_wc.dto.LoginRequestDTO;
import pe.edu.cibertec.Patotas_frontend_wc.dto.LoginResponseDTO;
import pe.edu.cibertec.Patotas_frontend_wc.viewmodel.LoginModel;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/inicio")
    public String inicio(Model model) {
        LoginModel loginModel = new LoginModel("00", "", "");
        model.addAttribute("loginModel", loginModel);
        return "inicio";
    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam("tipoDocumento") String tipoDocumento,
                             @RequestParam("numeroDocumento") String numeroDocumento,
                             @RequestParam("password") String password,
                             Model model) {

        // Validar campos de entrada
        if (tipoDocumento == null || tipoDocumento.trim().length() == 0 || numeroDocumento == null || numeroDocumento.trim().length() == 0 || password == null || password.trim().length() == 0) {
            LoginModel loginModel = new LoginModel("01", "Error: Debe completar correctamente sus credenciales", "");
            model.addAttribute("loginModel", loginModel);
            return "inicio";
        }

        try {
            // Invocar API de validacion de usuario
            // String endpoint = "/login";
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(tipoDocumento, numeroDocumento, password);
            LoginResponseDTO loginResponseDTO = restTemplate.postForObject("/login", loginRequestDTO, LoginResponseDTO.class);

            //Validar respuesta
            if (loginResponseDTO.codigo().equals("00")) {
                LoginModel loginModel = new LoginModel("00", "", loginResponseDTO.nombreUsuario());
                model.addAttribute("loginModel", loginModel);
                return "principal";
            } else {
                LoginModel loginModel = new LoginModel("02", "Error: Autenticacion fallida", "");
                model.addAttribute("loginModel", loginModel);
                return "inicio";
            }
        } catch (Exception e) {
            LoginModel loginModel = new LoginModel("99", "Error: Autenticacion fallida", "");
            model.addAttribute("loginModel", loginModel);
            System.out.println(e.getMessage());
            return "inicio";
        }

    }
}
