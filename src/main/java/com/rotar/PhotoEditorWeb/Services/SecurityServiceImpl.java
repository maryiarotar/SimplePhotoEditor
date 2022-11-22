package com.rotar.PhotoEditorWeb.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private DaoAuthenticationProvider authenticationManager;

    @Autowired
    private UserService userService;

    @Override
    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();

        if (userDetails instanceof UserDetails){
            return ((UserDetails) ((UserDetails) userDetails)).getUsername();
        }
        return null;
    }

    @Override
    public void autoLogin(String username, String password) {

        UserDetails userDetails = userService.loadUserByUsername(username);



        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(authenticationToken);

        if (authenticationToken.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        System.out.println("USER "+ username + " AUTHENTICATED");

    }
}
