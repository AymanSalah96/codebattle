package com.aymansalah.codebattle.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthService {
    public static boolean isAuthenticatedByUsername(String username) {
        if(null == username)
            return false;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String authenticatedUser;
        if(principal instanceof UserDetails) {
            authenticatedUser = ((UserDetails)principal).getUsername();
        } else {
            authenticatedUser = principal.toString();
        }
        return authenticatedUser.equalsIgnoreCase(username);
    }
}
