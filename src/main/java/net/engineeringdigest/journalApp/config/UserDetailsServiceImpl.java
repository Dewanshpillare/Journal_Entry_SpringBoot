package net.engineeringdigest.journalApp.config;

import net.engineeringdigest.journalApp.JournalRepo.UserRepo;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByuserName(username);

        if(user.isPresent()){
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.get().getUserName())
                    .password(user.get().getPassword())
                    .roles(user.get().getRoles().toArray(new String[0]))
                    .build();
            return userDetails;
        }
        return null;
    }
}
