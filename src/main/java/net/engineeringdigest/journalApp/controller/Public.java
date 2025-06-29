package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Public {

    @Autowired
    UserService userService;

    @GetMapping("/health")
    public String healthCheck(){
        return "OK";
    }

    // create new entry, any one can access it.
    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User saved = userService.saveNewUser(user);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }


}
