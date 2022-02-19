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
import guru.hakandurmaz.clients.emailcheck.MailCheckerResponse;
import guru.hakandurmaz.clients.emailcheck.MailClient;
import guru.hakandurmaz.clients.notification.NotificationClient;
import guru.hakandurmaz.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MailClient mailClient;
    private final NotificationClient notificationClient;


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
            userRepository.saveAndFlush(user);
            //TODO IS MAIL VALID

            //register notification
            notificationClient.sendNotification(
                    new NotificationRequest(
                            user.getId(),
                            user.getEmail(),
                            String.format("Welcome to my blog",user.getName()    )
                    )
            );



            MailCheckerResponse mailCheckerResponse =
                    mailClient.isIllegal(signupRequest.getEmail());

            if(mailCheckerResponse.isIllegal()) {
                throw new IllegalStateException("Mail adresi yasaklı.");
            }else {
                return "Kullanici Kaydedildi";
            }
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
