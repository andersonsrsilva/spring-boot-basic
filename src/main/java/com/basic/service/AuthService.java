package com.basic.service;

import com.basic.domain.entity.User;
import com.basic.domain.repository.UserRepository;
import com.basic.exception.InvalidPasswordException;
import com.basic.rest.dto.CredentialsRestDTO;
import com.basic.service.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    private User user = new User();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        this.user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //creating tables ROLES

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(this.user.getUsername())
                .password(this.user.getPassword())
                .roles("USER", "ADMIN")
                .build();
    }

    public TokenDTO authenticate(CredentialsRestDTO credentialsDTO) {
            User user = User.builder()
                    .username(credentialsDTO.getUsername())
                    .password(credentialsDTO.getPassword())
                    .build();

            getAuthenticate(user);

            String token = jwtService.generateToken(user);
            return new TokenDTO(this.user.getName(), token);
    }

    public UserDetails getAuthenticate(User user) {
        UserDetails userDetail = loadUserByUsername(user.getUsername());

        boolean isMatched = encoder.matches(user.getPassword(), userDetail.getPassword());

        if(isMatched) {
            return userDetail;
        }

        throw new InvalidPasswordException();
    }

}
