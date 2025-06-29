package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController // marks the class as bean and returns the required data in JSON format.
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    // ✅ Create a new entry
    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        JournalEntry saved = journalService.save(entry, userName);
        if(saved != null){
            return new ResponseEntity<>(saved, HttpStatus.OK);
        }else{
            throw new RuntimeException("User not Found: " + userName);
        }
    }

    // ✅ Get all entries
    @GetMapping("/allEntry")
    public ResponseEntity<List<JournalEntry>> getAllEntries() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> userOptional = userService.findByUserName(userName);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return new ResponseEntity<>(user.getJournalEntries(), HttpStatus.OK);
        }else{
            throw new RuntimeException("User not Found: " + userName);
        }
    }

    // ✅ Get entry by ID
    // Endpoint to fetch a journal entry by its ID
    @GetMapping("id/{id}")
    public ResponseEntity<JournalEntry> getEntry(@PathVariable ObjectId id) {

        // Get the current authenticated user's username from Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        // Find the User object from the database using the username
        Optional<User> user = userService.findByUserName(userName);

        // Check if the current user's journal entry list contains the given ID
        // We are filtering the user's journal entries to match the provided entry ID
        List<JournalEntry> entriesCollection = user.get()
                .getJournalEntries()
                .stream() // Pipeline to process the list of journal entries we got from user.
                .filter(x -> x.getId().equals(id)) // match entry ID
                .collect(Collectors.toUnmodifiableList()); // collect results into an unmodifiable list

        // If the entry belong to the user
        if(!entriesCollection.isEmpty()){
            // fetch the entry from the database
            Optional<JournalEntry> entry = journalService.getById(id);

            if(entry.isPresent()){
                // Return the entry if it exists in DB
                return new ResponseEntity<>(entry.get(), HttpStatus.OK);
            } else {
                // Entry not found in DB
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            // If the entry is already in user's list, that means it's not a valid case to show again or not allowed
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // ✅ Update entry by ID
    @PutMapping("/{id}")
    public ResponseEntity<JournalEntry> updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry entry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        // Find the User object from the database using the username
        Optional<User> user = userService.findByUserName(userName);

        if(user.isPresent()){
            // Check if the current user's journal entry list contains the given ID
            // We are filtering the user's journal entries to match the provided entry ID
            List<JournalEntry> entriesCollection = user.get()
                    .getJournalEntries()
                    .stream() // Pipeline to process the list of journal entries we got from user.
                    .filter(x -> x.getId().equals(id)) // match entry ID
                    .collect(Collectors.toUnmodifiableList()); // collect results into an unmodifiable list

            if(!entriesCollection.isEmpty()){
                return new ResponseEntity<>(journalService.updateById(id, entry), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    // ✅ Delete entry by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<JournalEntry> deleteEntry(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> userOptional = userService.findByUserName(userName);

        if(userOptional.isPresent()){
            User user = userOptional.get();
            boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(removed){
               // all the entries will be deleted whose id is equal to given id.(Lambda Function)
                JournalEntry deleted_Entry = journalService.deleteById(id);
                userService.saveNewUser(user);
                return new ResponseEntity<>(deleted_Entry, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
