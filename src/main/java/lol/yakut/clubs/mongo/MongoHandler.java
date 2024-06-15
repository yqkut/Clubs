package lol.yakut.clubs.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

public class MongoHandler {
   private final MongoClient client;
   private final MongoDatabase database;

   public MongoHandler(boolean uri, String connectionString, String host, int port, String database, boolean authentication, String username, String password) {
      if (uri) {
         this.client = new MongoClient(new MongoClientURI(connectionString));
      } else if (authentication) {
         this.client = new MongoClient(new ServerAddress(host, port), MongoCredential.createCredential(username, database, password.toCharArray()), MongoClientOptions.builder().build());
      } else {
         this.client = new MongoClient(host, port);
      }

      this.database = this.client.getDatabase(database);
   }

   public MongoClient getClient() {
      return this.client;
   }

   public MongoDatabase getDatabase() {
      return this.database;
   }
}
