package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.JournalRepo.UserRepo;
import net.engineeringdigest.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;



    public User saveUser(User entry){
        return userRepo.save(entry);
    }

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();
    public User saveNewUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("User"));
        return userRepo.save(user);
    }

    public List<User> getJournalEntries(){
        return userRepo.findAll();
    }

    public Optional<User> findByUserName(String name){
        return userRepo.findByuserName(name);
    }

    public User deleteByUserName(String name){
        Optional<User> userOptional = userRepo.findByuserName(name);

        if(userOptional.isPresent()){
            User user = userOptional.get();
            userRepo.delete(user);
            return user;
        }else{
            return null;
        }
    }



}
