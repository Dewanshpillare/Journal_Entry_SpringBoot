package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController // marks the class as bean and returns the required data in JSON format.
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    // ✅ Create a new entry
    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry entry) {
        return journalService.save(entry);
    }

    // ✅ Get all entries
    @GetMapping("/allEntry")
    public List<JournalEntry> getAllEntries() {
        return journalService.getAll();
    }

    // ✅ Get entry by ID
    @GetMapping("/{id}")
    public JournalEntry getEntry(@PathVariable ObjectId id) {
        Optional<JournalEntry> entry = journalService.getById(id);
        return entry.get();
    }

    // ✅ Update entry by ID
    @PutMapping("/{id}")
    public JournalEntry updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry entry) {
        return journalService.updateById(id, entry);
    }

    // ✅ Delete entry by ID
    @DeleteMapping("/{id}")
    public JournalEntry deleteEntry(@PathVariable ObjectId id) {
        return journalService.deleteById(id);
    }
}
