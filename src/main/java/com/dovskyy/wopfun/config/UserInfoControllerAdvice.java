package com.dovskyy.wopfun.config;

import com.dovskyy.wopfun.model.User;
import com.dovskyy.wopfun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class UserInfoControllerAdvice {

    private final UserService userService;

    @ModelAttribute("currentUserDisplayName")
    public String getCurrentUserDisplayName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        String username = authentication.getName();
        return userService.getUserByUsername(username)
                .map(user -> {
                    if (user.getFirstName() != null || user.getLastName() != null) {
                        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
                        String lastName = user.getLastName() != null ? user.getLastName() : "";
                        return (firstName + " " + lastName).trim();
                    }
                    return user.getUsername();
                })
                .orElse(username);
    }
}
