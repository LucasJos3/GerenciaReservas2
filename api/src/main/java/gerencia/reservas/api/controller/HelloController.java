package gerencia.reservas.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

	@GetMapping
	public String HelloWorld() {
		return "O servidor do Gerenciador de Reservas está funcionando";
	}
	
}
