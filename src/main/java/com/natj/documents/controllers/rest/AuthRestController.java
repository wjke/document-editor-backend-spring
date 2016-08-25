package com.natj.documents.controllers.rest;

import com.natj.documents.configs.security.TokenAuthenticationService;
import com.natj.documents.configs.security.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
public class AuthRestController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "/api/token", method = RequestMethod.POST)
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest loginRequest, BindingResult result, HttpServletResponse response) throws IOException {
        if(result.hasErrors())
            throw new BadCredentialsException("Bad credentials");

        Authentication authentication = authenticationManager.authenticate(loginRequest.toAuthenticationToken());
        UserDetails authenticatedUser = userDetailsService.loadUserByUsername(authentication.getName());
        UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);
        String token = tokenAuthenticationService.addAuthentication(response, userAuthentication);

        return new ResponseEntity<>(new UserLoginResponse(token), HttpStatus.OK);
    }

    private static class UserLoginRequest {
        @NotNull
        public String username;
        @NotNull
        public String password;

        public UsernamePasswordAuthenticationToken toAuthenticationToken() {
            return new UsernamePasswordAuthenticationToken(username, password);
        }
    }

    private static class UserLoginResponse {
        public String token;

        public UserLoginResponse(String token) {
            this.token = token;
        }
    }
}
