package lol.yakut.clubs.friend;

import com.google.gson.JsonObject;

public class FriendRequestSerializer {
   public static JsonObject serialize(FriendRequest friendRequest) {
      JsonObject object = new JsonObject();
      object.addProperty("target", friendRequest.getTarget().toString());
      object.addProperty("expiration", (Number)friendRequest.getExpiration());
      return object;
   }
}
