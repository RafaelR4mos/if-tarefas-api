package com.br.todo.service;

import com.br.todo.dto.auth.LoginRequestDTO;
import com.br.todo.dto.user.UserDTO;
import com.br.todo.entity.User;
import com.br.todo.entity.UserCreateDTO;
import com.br.todo.exception.RegraDeNegocioException;
import com.br.todo.exception.ResourceNotFoundException;
import com.br.todo.repository.UserRepository;
import com.br.todo.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UserDTO create(UserCreateDTO usuarioCreateDTO) {
        String encryptedPassword = encryptPassword(usuarioCreateDTO.getSenhaUsuario());

        User usuario = User.builder()
                .nomeUsuario(usuarioCreateDTO.getNomeUsuario())
                .senhaUsuario(encryptedPassword)
                .username(usuarioCreateDTO.getEmailUsuario())
                .build();

        return mapper.map(userRepository.save(usuario), UserDTO.class);
    }

    public String login(LoginRequestDTO loginRequestDTO) throws AuthenticationException {
        try {
            Authentication authentication = authenticateUser(loginRequestDTO.getEmailUsuario(), loginRequestDTO.getSenhaUsuario());
            return generateJwtToken(authentication);
         } catch (AuthenticationException ex) {
            throw  new RegraDeNegocioException("Usuário ou senha inválidos.");
        }

    }

    private Authentication authenticateUser(String username, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private String generateJwtToken(Authentication authentication) {
        return tokenService.generateToken((User) authentication.getPrincipal());
    }

    private String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    private boolean isPasswordMatches(String oldPassword, String newPassword) {
        return new BCryptPasswordEncoder().matches(newPassword, oldPassword);
    }

    public User getUsuario(String idUsuario) {
        return userRepository.findById(idUsuario)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Nenhum usuário com id " + idUsuario + " encontrado."));
    }
}
