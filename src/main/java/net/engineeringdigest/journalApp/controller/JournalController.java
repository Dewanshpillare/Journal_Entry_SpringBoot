package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalPOJO;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalController {

    private Map<Long, JournalPOJO> map = new HashMap<>();

    // ✅ Create a new entry
    @PostMapping
    public String createEntry(@RequestBody JournalPOJO entry) {
        map.put(entry.getId(), entry);
        return "Entry created";
    }

    // ✅ Get all entries
    @GetMapping
    public List<JournalPOJO> getAllEntries() {
        return new ArrayList<>(map.values());
    }

    // ✅ Get entry by ID
    @GetMapping("/{id}")
    public JournalPOJO getEntry(@PathVariable Long id) {
        return map.get(id);
    }

    // ✅ Update entry by ID
    @PutMapping("/{id}")
    public String updateEntry(@PathVariable Long id, @RequestBody JournalPOJO entry) {
        if (map.containsKey(id)) {
            map.put(id, entry);
            return "Entry updated";
        } else {
            return "Entry not found";
        }
    }

    // ✅ Delete entry by ID
    @DeleteMapping("/{id}")
    public JournalPOJO deleteEntry(@PathVariable Long id) {
        JournalPOJO entry = map.get(id);
        map.remove(id);
        return entry;
    }
}
