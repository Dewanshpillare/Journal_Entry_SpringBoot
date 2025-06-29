package net.engineeringdigest.journalApp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "UserTest")
@Data
@Getter
@Setter

@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;
    @NonNull
    @Indexed(unique = true) // The userName is indexed for faster searching and querying, and it is always unique and not null.
    private String userName;
    @NonNull // If any of the field is null, then it throws null pointer exception.
    private String password;
    @DBRef
    private List<JournalEntry> journalEntries = new ArrayList<>();
    
    private List<String> roles; // To keep track of a person having different role like admin, user etc.
}
