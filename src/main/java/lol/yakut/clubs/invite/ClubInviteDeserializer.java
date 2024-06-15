package lol.yakut.clubs.invite;

import com.google.gson.JsonObject;

import java.util.UUID;

public class ClubInviteDeserializer {
   public static ClubInvite deserialize(JsonObject object) {
      UUID target = UUID.fromString(object.get("target").getAsString());
      long expiration = object.get("expiration").getAsLong();
      return new ClubInvite(target, expiration);
   }
}
