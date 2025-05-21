package com.eventix.eventix.infra.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eventix.eventix.domain.Usuario;
import com.eventix.eventix.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter{

  @Autowired
  private TokenService tokenService;

  @Autowired
  private UsuarioRepository usuarioRepository;

  private static final AntPathMatcher pathMatcher = new AntPathMatcher();

  // Lista completa de rotas públicas
    private static final List<String> PUBLIC_ROUTES = List.of(
            "/swagger-ui/",
            "/v3/api-docs",
            "/swagger-ui.html",
            "/webjars/",
            "/favicon.ico",
            "/auth/login",
            "/auth/register",
            "/public/"
    );

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {

    try {         
      String requestURI = request.getRequestURI();
      // Verifica se a rota é pública
      if (isPublicRoute(requestURI)) {
        filterChain.doFilter(request, response);
        return;
      }

      var token = this.recoverToken(request);

      if (token == null) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token não fornecido");
        return;
      }

      var login = tokenService.validateToken(token);
      Usuario usuario = usuarioRepository.findByEmail(login)
              .orElseThrow(() -> new Exception("Usuario Não Encontrado"));
      
      var authentication = new UsernamePasswordAuthenticationToken(
          usuario, 
          null, 
          usuario.getAuthorities() // Usa as authorities do usuário
      );
      
      SecurityContextHolder.getContext().setAuthentication(authentication);
      filterChain.doFilter(request, response);

    } catch (Exception e) {
      if (!response.isCommitted()) {
          sendErrorResponse(response, e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
      }
    }
  }

  private String recoverToken (HttpServletRequest request) {
    var authHeader = request.getHeader("Authorization");
    if(authHeader == null) return null;
    return authHeader.replace("Bearer ", "");
  }

  private boolean isPublicRoute(String requestURI) {
    return PUBLIC_ROUTES.stream().anyMatch(route -> pathMatcher.match(route, requestURI));
}

  private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
    response.setStatus(status);
    response.setContentType("application/json");
    response.getWriter().write("{\"error\": \"" + message + "\"}");
    response.getWriter().flush();
  }
}
