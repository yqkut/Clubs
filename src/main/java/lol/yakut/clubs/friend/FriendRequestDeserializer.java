package lol.yakut.clubs.friend;

import com.google.gson.JsonObject;

import java.util.UUID;

public class FriendRequestDeserializer {
   public static FriendRequest deserialize(JsonObject object) {
      UUID target = UUID.fromString(object.get("target").getAsString());
      long expiration = object.get("expiration").getAsLong();
      return new FriendRequest(target, expiration);
   }
}
