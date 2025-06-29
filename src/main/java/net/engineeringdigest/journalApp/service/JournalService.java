package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.JournalRepo.JournalRepo;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JournalService {

    @Autowired
    private JournalRepo journalRepo;

    @Autowired
    private UserService userService;

    @Transactional
    public JournalEntry save(JournalEntry entry, String userName){
        Optional<User> userOptional = userService.findByUserName(userName);

        if(userOptional.isPresent()){
            User user = userOptional.get();

            JournalEntry savedEntry = journalRepo.save(entry);
            user.getJournalEntries().add(savedEntry);

            userService.saveNewUser(user);
            return savedEntry;
        }else{
            return null;
        }
    }

    public List<JournalEntry> getAll(){
        return journalRepo.findAll();
    }

    public Optional<JournalEntry> getById(ObjectId Id){
        return journalRepo.findById(Id);
    }

    public JournalEntry updateById(ObjectId Id, JournalEntry newEntry){
        Optional<JournalEntry> oldEntry = journalRepo.findById(Id);

        if(oldEntry.isPresent()){
            if(!(oldEntry.get().getTitle().equals(newEntry.getTitle()))){
                oldEntry.get().setTitle(newEntry.getTitle());
            }
            if(!(oldEntry.get().getBody().equals(newEntry.getBody()))) {
                oldEntry.get().setBody(newEntry.getBody());
            }
        }
        return journalRepo.save(oldEntry.get());
    }

    public JournalEntry deleteById(ObjectId Id){
        Optional<JournalEntry> entry = journalRepo.findById(Id);
        if(entry.isPresent()){
            journalRepo.deleteById(Id);
            return entry.get();
        }else{
            return null;
        }

    }
}
