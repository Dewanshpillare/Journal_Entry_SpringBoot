package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.*;

@RestController // marks the class as bean and returns the required data in JSON format.
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService service;

    @GetMapping
    public ResponseEntity<User> getUserByName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        Optional<User> person = service.findByUserName(name);
        if(person.isPresent()){
            return new ResponseEntity<>(person.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping
    public ResponseEntity<User> updateUserName(@RequestBody User newData){
        // Retrieves the current authenticated user's authentication details (like username, roles, etc.) from the security context.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName(); // getting name from the security context.
        Optional<User> data = service.findByUserName(name); // finding the user from the name

        try{
            if(data.isPresent()){
                User existingUser = data.get();
                existingUser.setUserName(newData.getUserName()); // update new data userName to existing user data
                existingUser.setPassword(newData.getPassword()); // update the new password

                // saveNewUser the existingUser in the database, and database will update,
                // because we are saving the data with existing ObjectId only. So data will be updated
                return new ResponseEntity<>(service.saveNewUser(existingUser), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @DeleteMapping
    public ResponseEntity<User> deleteByUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        User deletedUser = service.deleteByUserName(name);
        if(deletedUser != null){
            System.out.println("The user is deleted");
            return new ResponseEntity<>(deletedUser, HttpStatus.OK);
        }else{
            throw new RuntimeException("No such user exist.");
        }

    }
}
