package gerencia.reservas.api.infra.security;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import gerencia.reservas.api.entities.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UsuarioRepository repository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("** DO FILTER INTERNAL **");
			
		var tokenJWT = recuperarToken(request);
		
		if (tokenJWT != null) {
			
			var subject = tokenService.getSubject(tokenJWT);
			
			var usuario = repository.findByLogin(subject);
			
			var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		}
		filterChain.doFilter(request, response);
	}

	private String recuperarToken(HttpServletRequest request) {
		var authorizationHeader = request.getHeader("Authorization");
		var contentType = request.getHeader("content-Type");
		System.out.println("** RECUPERAR TOKEN JWT ** ");	
		System.out.println(authorizationHeader);
//		System.out.println(contentType);
//		System.out.println(request.getHeader("Access-Control-Allow-Origin"));
		if (authorizationHeader != null) {
			return authorizationHeader.replace("Bearer ", "");
		}
		return null;
	}
	
	private String requestBody(HttpServletRequest request) throws IOException {
		StringBuilder sb = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    String line;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line);
	    }
	    String requestBody = sb.toString();
	    return requestBody;
	}

}