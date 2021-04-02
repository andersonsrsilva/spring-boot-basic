package com.basic.service;

import com.basic.domain.entity.User;
import com.basic.exception.InvalidPasswordException;
import com.basic.domain.repository.UserRepository;
import com.basic.rest.dto.CredentialsRestDTO;
import com.basic.rest.dto.UserRestDTO;
import com.basic.service.dto.TokenDTO;
import com.basic.service.dto.UserServiceDTO;
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


    public UserServiceDTO findById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return MapperUtils.map(user, UserServiceDTO.class);
    }

    public List<UserServiceDTO> findAll() {
        List<User> list = userRepository.findAll();
        List<UserServiceDTO> listDTO = MapperUtils.mapAll(list, UserServiceDTO.class);

        return listDTO;
    }

    @Transactional
    public void create(UserRestDTO userRestDTO) {
        User user = MapperUtils.map(userRestDTO, User.class);
        user.setPassword(passwordEncoder.encode(userRestDTO.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void store(Long id, UserRestDTO userRestDTO) {
        User user = repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setUsername(userRestDTO.getUsername());
        user.setName(userRestDTO.getName());
        userRepository.save(user);
    }

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

    public TokenDTO authenticate(CredentialsRestDTO credentialsDTO) {
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
