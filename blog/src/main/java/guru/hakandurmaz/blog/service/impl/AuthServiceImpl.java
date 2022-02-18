package guru.hakandurmaz.blog.service.impl;

import guru.hakandurmaz.blog.entity.Role;
import guru.hakandurmaz.blog.entity.User;
import guru.hakandurmaz.blog.exception.BlogAPIException;
import guru.hakandurmaz.blog.payload.security.LoginRequest;
import guru.hakandurmaz.blog.payload.security.SignupRequest;
import guru.hakandurmaz.blog.repository.RoleRepository;
import guru.hakandurmaz.blog.repository.UserRepository;
import guru.hakandurmaz.blog.security.JwtTokenProvider;
import guru.hakandurmaz.blog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public String registerUser(SignupRequest signupRequest) {
        if(userRepository.existsByUsername(signupRequest.getUsername())){
            return "Username is already taken!";
        }else if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return "Email is already exist!";
        }else {
            //crete user object
            User user = new User();
            user.setEmail(signupRequest.getEmail());
            user.setUsername(signupRequest.getUsername());
            user.setName(signupRequest.getName());
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

            Role roles = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new BlogAPIException(HttpStatus.BAD_REQUEST,"Role Bulunamadı."));
            user.setRoles(Collections.singleton(roles));
            userRepository.save(user);

            return "User succesfully registered.";
        }
    }

    @Override
    @Transactional
    public String getToken(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //get token from token provider
        String token = jwtTokenProvider.generateToken(authentication);
        return token;

    }
}
