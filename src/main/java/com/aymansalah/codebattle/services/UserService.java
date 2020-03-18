package com.aymansalah.codebattle.services;

import com.aymansalah.codebattle.models.AuthGroup;
import com.aymansalah.codebattle.auth.UserPrincipal;
import com.aymansalah.codebattle.models.User;
import com.aymansalah.codebattle.repositories.AuthGroupRepository;
import com.aymansalah.codebattle.repositories.UserRepository;
import com.aymansalah.codebattle.util.NullAwareBeanUtilsBeanAndIgnoreIdProperty;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthGroupRepository authGroupRepository;

    public UserService(UserRepository userRepository, AuthGroupRepository authGroupRepository) {
        super();
        this.userRepository = userRepository;
        this.authGroupRepository = authGroupRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if(null == user) {
            throw new UsernameNotFoundException("Cannot find username: " + username);
        }
        List<AuthGroup> authGroups = this.authGroupRepository.findByUsername(username);
        return new UserPrincipal(user, authGroups);
    }

    public User registerNewUser(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        this.userRepository.saveAndFlush(user);
        this.authGroupRepository.saveAndFlush(new AuthGroup(user.getUsername(), "USER"));
        return user;
    }

    public boolean isUsernameExists(String username) {
        return null != userRepository.findByUsername(username);
    }

    public boolean isEmailExists(String email) {
        return null != userRepository.findByEmail(email) ;
    }

    public User getByUserName(String username) {
        return this.userRepository.findByUsername(username);
    }

    public void savePhotoForUsername(String username, String photo) {
       User user = this.userRepository.findByUsername(username);
       user.setPhoto(photo);
       this.userRepository.saveAndFlush(user);
    }

    public User update(String username, User user) {
        User existingUser = this.getByUserName(username);
        return this.userRepository.saveAndFlush(replaceNotNullProperties(existingUser, user));
    }

    private User replaceNotNullProperties(User existingUser, User updatedUser) {
        BeanUtilsBean notNull = new NullAwareBeanUtilsBeanAndIgnoreIdProperty();
        try {
            notNull.copyProperties(existingUser, updatedUser);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return existingUser;
    }

    public void save(User user) {
        this.userRepository.saveAndFlush(user);
    }

    public String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}
