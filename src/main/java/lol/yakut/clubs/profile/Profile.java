package lol.yakut.clubs.profile;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.channel.ChatChannel;
import lol.yakut.clubs.friend.FriendRequest;
import lol.yakut.clubs.friend.FriendRequestDeserializer;
import lol.yakut.clubs.friend.FriendRequestSerializer;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Profile {
   private UUID uuid;
   private String username;
   private String club;
   private List<FriendRequest> friendRequests;
   private List<UUID> friends;
   private long lastSeenOn;
   private String lastSeenServer;
   private boolean online;
   private ChatChannel chatChannel;

   public Profile(UUID uuid) {
      this.uuid = uuid;
      this.username = Bukkit.getOfflinePlayer(uuid).getName();
      this.club = "";
      this.friendRequests = new ArrayList();
      this.friends = new ArrayList();
      this.lastSeenOn = System.currentTimeMillis();
      this.lastSeenServer = "!@#$%^&*()";
      this.online = false;
      this.chatChannel = ChatChannel.NORMAL;
      this.load();
   }

   public Profile(String username) {
      this.uuid = Bukkit.getOfflinePlayer(username).getUniqueId();
      this.username = username;
      this.club = "";
      this.friendRequests = new ArrayList();
      this.friends = new ArrayList();
      this.lastSeenOn = System.currentTimeMillis();
      this.lastSeenServer = "!@#$%^&*()";
      this.online = false;
      this.chatChannel = ChatChannel.NORMAL;
      this.load();
   }

   public void save() {
      Document document = new Document();
      document.append("uuid", this.uuid.toString());
      document.append("username", this.username);
      if (!this.club.isEmpty()) {
         document.append("club", this.club);
      }

      if (!this.friendRequests.isEmpty()) {
         JsonArray jsonArray = new JsonArray();
         Iterator var3 = this.friendRequests.iterator();

         while(var3.hasNext()) {
            FriendRequest request = (FriendRequest)var3.next();
            if (!request.isExpired()) {
               jsonArray.add((JsonElement) FriendRequestSerializer.serialize(request));
            }
         }

         document.append("friendRequests", jsonArray.toString());
      }

      if (!this.friends.isEmpty()) {
         document.append("friends", this.friends.toString());
      }

      document.append("lastSeenOn", this.lastSeenOn);
      document.append("lastSeenServer", this.lastSeenServer);
      Bson filter = Filters.eq("uuid", this.uuid.toString());
      Clubs.getInstance().getProfileHandler().getCollection().replaceOne((Bson)filter, (Document) document, (ReplaceOptions)(new ReplaceOptions()).upsert(true));
   }

   public void load() {
      Bson filter = Filters.eq("uuid", this.uuid.toString());
      Document document = (Document) Clubs.getInstance().getProfileHandler().getCollection().find(filter).first();
      if (document != null) {
         this.uuid = UUID.fromString(document.getString("uuid"));
         this.username = document.getString("username");
         if (document.getString("club") != null) {
            this.club = document.getString("club");
         }

         if (document.getString("friendRequests") != null) {
            Iterator var3 = (new JsonParser()).parse(document.getString("friendRequests")).getAsJsonArray().iterator();

            while(var3.hasNext()) {
               JsonElement element = (JsonElement)var3.next();
               JsonObject object = element.getAsJsonObject();
               FriendRequest request = FriendRequestDeserializer.deserialize(object);
               if (!request.isExpired()) {
                  this.friendRequests.add(FriendRequestDeserializer.deserialize(object));
               }
            }
         }

         if (document.getString("friends") != null) {
            List<String> arrayList = (List)(new Gson()).fromJson(document.getString("friends"), (new TypeToken<List<String>>() {
            }).getType());
            Iterator var8 = arrayList.iterator();

            while(var8.hasNext()) {
               String string = (String)var8.next();
               this.friends.add(UUID.fromString(string));
            }
         }

         this.lastSeenOn = document.getLong("lastSeenOn");
         this.lastSeenServer = document.getString("lastSeenServer");
         if (Bukkit.getPlayer(this.uuid) != null) {
            this.online = true;
         }

      }
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public String getUsername() {
      return this.username;
   }

   public String getClub() {
      return this.club;
   }

   public List<FriendRequest> getFriendRequests() {
      return this.friendRequests;
   }

   public List<UUID> getFriends() {
      return this.friends;
   }

   public long getLastSeenOn() {
      return this.lastSeenOn;
   }

   public String getLastSeenServer() {
      return this.lastSeenServer;
   }

   public boolean isOnline() {
      return this.online;
   }

   public ChatChannel getChatChannel() {
      return this.chatChannel;
   }

   public void setUuid(UUID uuid) {
      this.uuid = uuid;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setClub(String club) {
      this.club = club;
   }

   public void setFriendRequests(List<FriendRequest> friendRequests) {
      this.friendRequests = friendRequests;
   }

   public void setFriends(List<UUID> friends) {
      this.friends = friends;
   }

   public void setLastSeenOn(long lastSeenOn) {
      this.lastSeenOn = lastSeenOn;
   }

   public void setLastSeenServer(String lastSeenServer) {
      this.lastSeenServer = lastSeenServer;
   }

   public void setOnline(boolean online) {
      this.online = online;
   }

   public void setChatChannel(ChatChannel chatChannel) {
      this.chatChannel = chatChannel;
   }
}
