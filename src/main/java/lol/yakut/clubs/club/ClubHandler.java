package lol.yakut.clubs.club;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lol.yakut.clubs.Clubs;
import org.bson.Document;
import org.bson.conversions.Bson;

public class ClubHandler {
   private MongoCollection<Document> collection = Clubs.getInstance().getMongoHandler().getDatabase().getCollection("Clubs");

   public Club getClubByLowercaseName(String lowercaseName) {
      Bson filter = Filters.eq("lowercaseName", lowercaseName);
      Document document = (Document)this.collection.find(filter).first();
      return document == null ? null : new Club(document.getString("name"), document.getString("lowercaseName"));
   }

   public void deleteClubByLowercaseName(String lowercaseName) {
      Bson filter = Filters.eq("lowercaseName", lowercaseName);
      this.collection.findOneAndDelete(filter);
   }

   public MongoCollection<Document> getCollection() {
      return this.collection;
   }
}
