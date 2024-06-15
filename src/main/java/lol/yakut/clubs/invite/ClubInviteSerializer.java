package lol.yakut.clubs.invite;

import com.google.gson.JsonObject;

public class ClubInviteSerializer {
   public static JsonObject serialize(ClubInvite clubInvite) {
      JsonObject object = new JsonObject();
      object.addProperty("target", clubInvite.getTarget().toString());
      object.addProperty("expiration", (Number)clubInvite.getExpiration());
      return object;
   }
}
