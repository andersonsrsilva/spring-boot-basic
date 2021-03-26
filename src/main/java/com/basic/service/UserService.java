package com.basic.service;

import com.basic.domain.entity.User;
import com.basic.exception.InvalidPasswordException;
import com.basic.domain.repository.UserRepository;
import com.basic.service.dto.CredentialsDTO;
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
    public void create(UserDTO dto) {
        User user = MapperUtils.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }

    public TokenDTO authenticate(CredentialsDTO dto) {
        try{
            User user = User.builder()
                    .password(dto.getPassword())
                    .password(dto.getPassword())
                    .build();
            UserDetails authenticateUser = authenticate(user); //REFACTORING
            String token = jwtService.generateToken(user);
            return new TokenDTO(user.getUsername(), token);
        } catch (UsernameNotFoundException | InvalidPasswordException e ){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    public UserDetails authenticate(User user) {
        UserDetails userDetail = loadUserByUsername(user.getUsername());
        boolean isMatched = encoder.matches(user.getPassword(), userDetail.getPassword());

        if(isMatched) {
            return userDetail;
        }

        throw new InvalidPasswordException();
    }

}
