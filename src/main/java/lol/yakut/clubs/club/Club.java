package lol.yakut.clubs.club;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.invite.ClubInvite;
import lol.yakut.clubs.invite.ClubInviteDeserializer;
import lol.yakut.clubs.invite.ClubInviteSerializer;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Club {
   private String name;
   private String lowercaseName;
   private UUID leader;
   private List<UUID> admins;
   private List<UUID> members;
   private List<ClubInvite> clubInvites;
   private int limit;

   public Club(String name, String lowercaseName) {
      this.name = name;
      this.lowercaseName = lowercaseName;
      this.leader = UUID.randomUUID();
      this.admins = new ArrayList();
      this.members = new ArrayList();
      this.clubInvites = new ArrayList();
      this.limit = 0;
      this.load();
   }

   public Club(String name, String lowercaseName, UUID leader) {
      this.name = name;
      this.lowercaseName = lowercaseName;
      this.leader = leader;
      this.admins = new ArrayList();
      this.members = new ArrayList();
      this.clubInvites = new ArrayList();
      this.limit = 0;
      this.load();
   }

   public Club(String name, String lowercaseName, UUID leader, List<UUID> admins, List<UUID> members, List<ClubInvite> clubInvites, int limit) {
      this.name = name;
      this.lowercaseName = lowercaseName;
      this.leader = leader;
      this.admins = admins;
      this.members = members;
      this.clubInvites = clubInvites;
      this.limit = limit;
      this.load();
   }

   public void save() {
      Document document = new Document();
      document.append("name", this.name);
      document.append("lowercaseName", this.lowercaseName);
      document.append("leader", this.leader.toString());
      if (!this.admins.isEmpty()) {
         document.append("admins", this.admins.toString());
      }

      if (!this.members.isEmpty()) {
         document.append("members", this.members.toString());
      }

      if (!this.clubInvites.isEmpty()) {
         JsonArray jsonArray = new JsonArray();
         Iterator var3 = this.clubInvites.iterator();

         while(var3.hasNext()) {
            ClubInvite invite = (ClubInvite)var3.next();
            if (!invite.isExpired()) {
               jsonArray.add((JsonElement) ClubInviteSerializer.serialize(invite));
            }
         }

         document.append("clubInvites", jsonArray.toString());
      }

      document.append("limit", this.limit);
      Bson filter = Filters.eq("name", this.name);
      Clubs.getInstance().getClubHandler().getCollection().replaceOne((Bson)filter, (Document) document, (ReplaceOptions)(new ReplaceOptions()).upsert(true));
   }

   public void load() {
      Bson filter = Filters.eq("name", this.name);
      Document document = (Document) Clubs.getInstance().getClubHandler().getCollection().find(filter).first();
      if (document != null) {
         this.name = document.getString("name");
         this.lowercaseName = document.getString("lowercaseName");
         this.leader = UUID.fromString(document.getString("leader"));
         Gson gson = new Gson();
         Type type = (new TypeToken<List<String>>() {
         }).getType();
         List arrayList;
         Iterator var6;
         String string;
         if (document.getString("admins") != null) {
            arrayList = (List)gson.fromJson(document.getString("admins"), type);
            var6 = arrayList.iterator();

            while(var6.hasNext()) {
               string = (String)var6.next();
               this.admins.add(UUID.fromString(string));
            }

            arrayList.clear();
         }

         if (document.getString("members") != null) {
            arrayList = (List)gson.fromJson(document.getString("members"), type);
            var6 = arrayList.iterator();

            while(var6.hasNext()) {
               string = (String)var6.next();
               this.members.add(UUID.fromString(string));
            }
         }

         if (document.getString("clubInvites") != null) {
            var6 = (new JsonParser()).parse(document.getString("clubInvites")).getAsJsonArray().iterator();

            while(var6.hasNext()) {
               JsonElement element = (JsonElement)var6.next();
               JsonObject object = element.getAsJsonObject();
               ClubInvite invite = ClubInviteDeserializer.deserialize(object);
               if (!invite.isExpired()) {
                  this.clubInvites.add(ClubInviteDeserializer.deserialize(object));
               }
            }
         }

         this.limit = document.getInteger("limit");
      }
   }

   public String getName() {
      return this.name;
   }

   public String getLowercaseName() {
      return this.lowercaseName;
   }

   public UUID getLeader() {
      return this.leader;
   }

   public List<UUID> getAdmins() {
      return this.admins;
   }

   public List<UUID> getMembers() {
      return this.members;
   }

   public List<ClubInvite> getClubInvites() {
      return this.clubInvites;
   }

   public int getLimit() {
      return this.limit;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setLowercaseName(String lowercaseName) {
      this.lowercaseName = lowercaseName;
   }

   public void setLeader(UUID leader) {
      this.leader = leader;
   }

   public void setAdmins(List<UUID> admins) {
      this.admins = admins;
   }

   public void setMembers(List<UUID> members) {
      this.members = members;
   }

   public void setClubInvites(List<ClubInvite> clubInvites) {
      this.clubInvites = clubInvites;
   }

   public void setLimit(int limit) {
      this.limit = limit;
   }
}
