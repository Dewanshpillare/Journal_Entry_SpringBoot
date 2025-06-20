package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController // marks the class as bean and returns the required data in JSON format.
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    // ✅ Create a new entry
    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry) {
        return new ResponseEntity<>(journalService.save(entry), HttpStatus.OK);
    }

    // ✅ Get all entries
    @GetMapping("/allEntry")
    public ResponseEntity<List<JournalEntry>> getAllEntries() {
        return new ResponseEntity<>(journalService.getAll(), HttpStatus.OK);
    }

    // ✅ Get entry by ID
    @GetMapping("/{id}")
    public ResponseEntity<JournalEntry> getEntry(@PathVariable ObjectId id) {
        Optional<JournalEntry> entry = journalService.getById(id);
        if(entry.isPresent()){
            return new ResponseEntity<>(entry.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // ✅ Update entry by ID
    @PutMapping("/{id}")
    public ResponseEntity<JournalEntry> updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry entry) {
        return new ResponseEntity<>(journalService.updateById(id, entry), HttpStatus.OK);
    }

    // ✅ Delete entry by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<JournalEntry> deleteEntry(@PathVariable ObjectId id) {
        JournalEntry deleted_Entry = journalService.deleteById(id);
        if(deleted_Entry != null){
            return new ResponseEntity<>(deleted_Entry, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
