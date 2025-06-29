package net.engineeringdigest.journalApp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "net.engineeringdigest.journalApp.JournalRepo")
@EnableTransactionManagement // Tells SpringBoot to setup itself for performing transactions
public class JournalApplication {
    public static void main(String[] args) {
        SpringApplication.run(JournalApplication.class, args);
    }

    @Bean
    public PlatformTransactionManager manager(MongoDatabaseFactory dbFactory){
        return new MongoTransactionManager(dbFactory);

        // PlatformTransactionManager is an interface which contain basic methods to perform transition like rollback().
        // MongoTransactionManager is implementation class of PlatformTransactionManager which has methods for transitions.
        // MongoDatabaseFactory is a class which helps in managing proper connection with database.
    }

}