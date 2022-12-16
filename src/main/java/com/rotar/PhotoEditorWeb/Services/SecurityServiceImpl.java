package com.rotar.PhotoEditorWeb.Services;

import com.rotar.PhotoEditorWeb.Models.UserEntity;
import com.rotar.PhotoEditorWeb.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class SecurityServiceImpl implements SecurityService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public String findLoggedInUsername() {
        logger.info("Finding the name of this authorized used...");

        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        logger.info("Userdetails ={}. Name is ={}. Principal = {}", userDetails, auth.getName(), auth.getPrincipal());

/*        if (userDetails instanceof UserDetails){
            return ((UserDetails) ((UserDetails) userDetails)).getUsername();
        }*/

        return auth.getName();
    }





    @Override
    public void autoLogin(String username, String password) {

/*        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        // Authenticate the user
        Authentication authentication = daoAuthenticationProvider.authenticate(authRequest);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);*/

        System.out.println("USER " + username + " is authenticating...");

        UserDetails userDetails = userService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        System.out.println(" wait...");
        System.out.println(" wait..." + authenticationToken.toString());


        Authentication authentication = daoAuthenticationProvider.authenticate(authenticationToken);

        System.out.println(" wait... a bit ....");

        if (authenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            System.out.println("USER " + username + " AUTHENTICATED");

        }
    }
}
/*

<authentication-manager>
<authentication-provider ref="daoAuthenticationProvider" />
</authentication-manager>
*/
