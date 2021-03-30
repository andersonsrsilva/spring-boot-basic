package com.basic.service;

import com.basic.domain.entity.User;
import com.basic.exception.InvalidPasswordException;
import com.basic.domain.repository.UserRepository;
import com.basic.rest.dto.CredentialsDTO;
import com.basic.service.dto.TokenDTO;
import com.basic.service.dto.UserDTO;
import com.basic.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //creating tables ROLES

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER", "ADMIN")
                .build();
    }

    public List<UserDTO> findAll() {
        List<User> list = userRepository.findAll();
        List<UserDTO> listDTO = MapperUtils.mapAll(list, UserDTO.class);

        return listDTO;
    }

    @Transactional
    public void create(UserDTO userDTO) {
        User user = MapperUtils.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
    }

    public TokenDTO authenticate(CredentialsDTO credentialsDTO) {
        try{
            User user = User.builder()
                    .username(credentialsDTO.getUsername())
                    .password(credentialsDTO.getPassword())
                    .build();

            getAuthenticate(user);

            String token = jwtService.generateToken(user);
            return new TokenDTO(user.getUsername(), token);
        } catch (UsernameNotFoundException | InvalidPasswordException e ){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
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
