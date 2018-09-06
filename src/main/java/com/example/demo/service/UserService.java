package com.example.demo.service;

import com.example.demo.dto.CredentialsDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.tools.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user;
        if (login.contains("@")) {
            user = userRepository.findByEmail(login);
            if (user == null) {
                throw new UsernameNotFoundException(String.format("the user with email %s can't be found", login));
            }
        } else {
            var userOptional = userRepository.findByUserName(login);

            if (!userOptional.isPresent()) {
                throw new UsernameNotFoundException(String.format("the user %s can't be found.", login));
            }
            user = userOptional.get();
        }
        return user;
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("the user with id %s cannot be found.", userId)));
    }

    /**
     * Create user from registration with the user name, the email and the password
     * and give it the role 'ROLE_USER'
     * @param credentialsDto user credentials
     * @return the user dto
     */
    @Transactional
    public UserDto create(CredentialsDto credentialsDto) {
        var user = new User();
        user.setPassword(encoder.encode(credentialsDto.getPassword()));
        user.setUserName(credentialsDto.getUserName());
        user.setEmail(credentialsDto.getEmail());
        Role role = roleRepository.findByLabel("ROLE_USER");
        user.addRole(role);
        return Converter.map(userRepository.save(user), UserDto.class);
    }


    public List<UserDto> findAll() {
        return Converter.map(userRepository.findAll(), UserDto.class);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

}
