package com.marcelo.chatapp.service;

import com.marcelo.chatapp.dto.AuthenticationResponse;
import com.marcelo.chatapp.dto.LoginRequest;
import com.marcelo.chatapp.dto.RefreshTokenRequest;
import com.marcelo.chatapp.dto.RegisterRequest;
import com.marcelo.chatapp.exceptions.SpringChatException;
import com.marcelo.chatapp.model.AppUser;
import com.marcelo.chatapp.model.NotificationEmail;
import com.marcelo.chatapp.model.VerificationToken;
import com.marcelo.chatapp.repository.UserRepository;
import com.marcelo.chatapp.repository.VerificationTokenRepository;
import com.marcelo.chatapp.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public void signup(RegisterRequest registerRequest) {
        AppUser user = new AppUser();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(true);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        //mailService.sendMail(new NotificationEmail("Please Activate your Account",
                //user.getEmail(), "Thank you for signing up to Chat App, " +
                //"please click on the below url to activate your account : " +
                //"http://localhost:8080/api/auth/accountVerification/" + token));
    }

    @Transactional(readOnly = true)
    public AppUser getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("AppUser name not found - " + principal.getSubject()));
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new SpringChatException("AppUser not found with name - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    private String generateVerificationToken(AppUser user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new SpringChatException("Invalid Token")));
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            String token = jwtProvider.generateToken(authenticate);
            return AuthenticationResponse.builder()
                    .authenticationToken(token)
                    .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                    .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                    .username(loginRequest.getUsername())
                    .build();
        } catch (BadCredentialsException e) {
            // Handle invalid username or password
            throw new SpringChatException("Invalid username or password for: " + loginRequest.getUsername(), e);
        } catch (LockedException e) {
            // Handle locked user account
            throw new SpringChatException("Account locked for user: " + loginRequest.getUsername(), e);
        } catch (Exception e) {
            // Handle other exceptions
            throw new SpringChatException("Error during login for user: " + loginRequest.getUsername(), e);
        }
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}