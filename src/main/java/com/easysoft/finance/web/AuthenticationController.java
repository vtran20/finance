package com.easysoft.finance.web;

import com.easysoft.finance.configuration.MyUserDetailService;
import com.easysoft.finance.configuration.event.UserForgetPasswordEvent;
import com.easysoft.finance.configuration.event.UserRegistrationEvent;
import com.easysoft.finance.configuration.event.UserResendActivationCodeEvent;
import com.easysoft.finance.configuration.exception.ResourceAlreadyExists;
import com.easysoft.finance.configuration.exception.ResourceNotFoundException;
import com.easysoft.finance.configuration.exception.UnauthorizedException;
import com.easysoft.finance.configuration.util.JwtUtil;
import com.easysoft.finance.domain.User;
import com.easysoft.finance.domain.auth.AuthenticationRequest;
import com.easysoft.finance.domain.auth.AuthenticationResponse;
import com.easysoft.finance.repository.UserRepository;
import com.easysoft.finance.service.EmailService;
import com.easysoft.utils.Utils;
import javassist.bytecode.DuplicateMemberException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthenticationController {

    private Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyUserDetailService myUserDetailService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    public EmailService emailService;

    /**
     * {
     * "username": "",
     * "password": ""
     * }
     *
     * @param authenticationRequest
     * @return
     * @throws ResourceNotFoundException
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws ResourceNotFoundException {
        final String token;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            final UserDetails userDetails = myUserDetailService.loadUserByUsername(authenticationRequest.getUsername());
            token = jwtUtil.generateToken(userDetails);
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Bad Credentials", "Incorrect username or password", null, null);
        }

        return ResponseEntity.ok(new AuthenticationResponse(authenticationRequest.getUsername(), token));
    }

    /**
     * {
     * "firstName": "Vu",
     * "lastName": "Tran",
     * "username": "",
     * "password":"",
     * "roles" : "ADMIN"
     * }
     *
     * @param user
     * @return
     * @throws DuplicateMemberException
     */
    @PostMapping("/register")
    public User createUser(@RequestBody User user, @RequestBody Map data) throws DuplicateMemberException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResourceAlreadyExists("Conflict", "The username " + user.getUsername() + " is existing.", null, null);
        }
        String number = Utils.generateActivationCode();
        user.setActivationCode(number);
        user.setPassword(passwordEncoder.encode(data.get("password") + ""));
        User savedUser = userRepository.save(user);
        eventPublisher.publishEvent(new UserRegistrationEvent(savedUser));
        return savedUser;
    }

    /**
     * {
     * "token": "xxxxx"
     * }
     *
     * @param user
     * @return
     * @throws DuplicateMemberException
     */
    @PostMapping("/verify-token")
    public User verifyToken(@RequestBody User user) throws DuplicateMemberException {
        String token = user.getToken();
        if (StringUtils.hasLength(token)) {
            if (jwtUtil.isTokenExpired(token)) {
                throw new UnauthorizedException("Login", "Your login was expired. Please login again", null, null);
            } else {
                user.setUsername(jwtUtil.extractUsername(token));
            }
        } else {
            throw new UnauthorizedException("Login", "Your login was expired. Please login again", null, null);
        }
        return user;
    }

    /**
     * {
     * "username": "example@yahoo.com"
     * }
     *
     * @param user
     * @return
     */
    @PostMapping("/resend-activation-code")
    public ResponseEntity<?> resendActivationCode(@RequestBody User user) {
        String username = user.getUsername();
        if (StringUtils.hasLength(username)) {
            userRepository.findByUsername(username).map(u -> {
                String number = Utils.generateActivationCode();
                u.setActivationCode(number);
                userRepository.save(u);
                eventPublisher.publishEvent(new UserResendActivationCodeEvent(u));
                return ResponseEntity.ok().build();
            }).orElseThrow(() -> new ResourceNotFoundException("Not Found", "Cannot find your email in our system", null, null));
        } else {
            throw new ResourceNotFoundException("Not Found", "Cannot find your email in our system", null, null);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * {
     * "username": "example@yahoo.com",
     * "activationCode":"832893"
     * }
     *
     * @param user
     * @return
     */
    @PostMapping("/activate-user")
    public ResponseEntity<?> activateUser(@RequestBody User user) {
        String username = user.getUsername();
        String activationCode = user.getActivationCode();
        if (StringUtils.hasLength(username) && StringUtils.hasLength(activationCode)) {
            userRepository.findByUsername(username).map(u -> {
                if (activationCode.equals(u.getActivationCode())) {
                    u.setActivationCode("");
                    u.setActive("Y");
                    userRepository.save(u);
                    myUserDetailService.evictSingleCacheValue(u.getUsername()); //remove user from cache. This fix issue when register and try login first before activate the account.
                    return ResponseEntity.ok().build();
                } else {
                    throw new ResourceNotFoundException("Not Found", "Activation Code or Email is not correct", null, null);
                }
            }).orElseThrow(() -> new ResourceNotFoundException("Not Found", "Activation Code or Email is not correct", null, null));
        } else {
            throw new ResourceNotFoundException("Not Found", "Activation Code or Email is not correct", null, null);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * {
     * "username": "example@yahoo.com"
     * }
     *
     * @param user
     * @return
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody User user) {
        String username = user.getUsername();
        if (StringUtils.hasLength(username)) {
            userRepository.findByUsername(username).map(u -> {
                String tempPass = Utils.generateRandomNumber();
                u.setTempPassword(passwordEncoder.encode(tempPass));
                userRepository.save(u);
                eventPublisher.publishEvent(new UserForgetPasswordEvent(u, tempPass));
                return ResponseEntity.ok().build();
            }).orElseThrow(() -> new ResourceNotFoundException("Not Found", "Cannot find your email in our system", null, null));
        } else {
            throw new ResourceNotFoundException("Not Found", "Cannot find your email in our system", null, null);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Enter the temporary password has been sent from /forgot-password. Verifying the temp password
     * <p>
     * {
     * "username": "example@gmail.com",
     * "tempPassword":"430853"
     * }
     *
     * @param data
     * @return
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map data) {
        String username = (String) data.get("username");
        String tempPassword = (String) data.get("tempPassword");
        boolean goodTemporaryPassword = false;
        if (StringUtils.hasLength(username) && StringUtils.hasLength(tempPassword)) {
            goodTemporaryPassword = userRepository.findByUsername(username).map(u -> {
                if (passwordEncoder.encode(tempPassword).equals(u.getTempPassword())) {
                    return true;
                } else {
                    return false;
                }
            }).orElseThrow(() ->
                    new ResourceNotFoundException("Not Found", "Cannot find your email in our system", null, null));

        } else {
            throw new ResourceNotFoundException("Not Found", "Cannot find your email in our system", null, null);
        }
        if (goodTemporaryPassword) {
            return ResponseEntity.ok().build();
        } else {
            new ResourceNotFoundException("Not Found", "Your temporary password is incorrect", null, null);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * {
     * "username": "example@gmail.com",
     * "password": "newpassword",
     * "tempPassword":"430853"
     * }
     *
     * @param data
     * @return
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody AuthenticationRequest authenticationRequest, @RequestBody Map data) {
        String username = (String) data.get("username");
        String tempPassword = (String) data.get("tempPassword");
        boolean goodTemporaryPassword;
        if (StringUtils.hasLength(username) && StringUtils.hasLength(tempPassword)) {
            goodTemporaryPassword = userRepository.findByUsername(username).map(u -> {
                if (passwordEncoder.encode(tempPassword).equals(u.getTempPassword())) {
                    return true;
                } else {
                    return false;
                }
            }).orElseThrow(() ->
                    new ResourceNotFoundException("Not Found", "Cannot find your email in our system", null, null));

        } else {
            throw new ResourceNotFoundException("Not Found", "Cannot find your email in our system", null, null);
        }
        if (goodTemporaryPassword) {
            return createAuthenticationToken(authenticationRequest);//login after change password successfully
        } else {
            new ResourceNotFoundException("Not Found", "Your temporary password is incorrect", null, null);
        }
        return ResponseEntity.badRequest().build();
    }

}
