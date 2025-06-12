package com.br.todo.security;

import com.br.todo.exception.InvalidTokenException;
import com.br.todo.exception.ResourceNotFoundException;
import com.br.todo.exception.handler.ErrorHandlerResponse;
import com.br.todo.exception.handler.ExceptionResponseHandler;
import com.br.todo.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService  tokenService;
    private final UserRepository userRepository;
    private final ExceptionResponseHandler exceptionResponseHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = this.recoverToken(request);
            if (token != null) {
                String login = tokenService.validateToken(token);
                UserDetails user = userRepository.findById(login)
                        .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
                        user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (InvalidTokenException ex) {
            handleInvalidTokenException(response, ex);
        }
    }

    private void handleInvalidTokenException(HttpServletResponse response, InvalidTokenException ex)
            throws IOException {
        ResponseEntity<ErrorHandlerResponse> errorResponseEntity =
                exceptionResponseHandler.handleInvalidTokenException(ex);

        response.setStatus(errorResponseEntity.getStatusCode().value());
        response.setContentType("application/json");
    }

    private String recoverToken(HttpServletRequest request) {
        // VIA HEADER AUTHORIZATION
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }

        return authHeader.replace("Bearer ", "");
    }
}
