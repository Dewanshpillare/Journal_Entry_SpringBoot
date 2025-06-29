package net.engineeringdigest.journalApp.JournalRepo;

import net.engineeringdigest.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<User, ObjectId> {
    Optional<User> findByuserName(String userName);

    User deleteByUserName(String userName);
}
