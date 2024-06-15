package lol.yakut.clubs.profile;

import com.mongodb.client.MongoCollection;
import lol.yakut.clubs.Clubs;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ProfileHandler {
   private List<Profile> profiles = new ArrayList();
   private MongoCollection<Document> collection = Clubs.getInstance().getMongoHandler().getDatabase().getCollection("Profiles");

   public Profile getProfileByUsername(String username) {
      Player player = Bukkit.getPlayer(username);
      if (player != null) {
         Iterator var3 = this.profiles.iterator();

         while(var3.hasNext()) {
            Profile profile = (Profile)var3.next();
            if (profile.getUsername().equalsIgnoreCase(username)) {
               return profile;
            }
         }
      }

      OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
      return offlinePlayer.hasPlayedBefore() ? new Profile(username) : null;
   }

   public Profile getProfileByUUID(UUID uuid) {
      Player player = Bukkit.getPlayer(uuid);
      if (player != null) {
         Iterator var3 = this.profiles.iterator();

         while(var3.hasNext()) {
            Profile profile = (Profile)var3.next();
            if (profile.getUuid().equals(uuid)) {
               return profile;
            }
         }
      }

      OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
      return offlinePlayer.hasPlayedBefore() ? new Profile(uuid) : null;
   }

   public List<Profile> getProfiles() {
      return this.profiles;
   }

   public MongoCollection<Document> getCollection() {
      return this.collection;
   }
}
