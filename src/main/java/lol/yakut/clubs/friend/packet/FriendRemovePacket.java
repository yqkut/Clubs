package lol.yakut.clubs.friend.packet;

import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.redis.packet.Packet;

import java.util.UUID;

public class FriendRemovePacket extends Packet {
   private UUID uuid;
   private UUID uuid2;

   public FriendRemovePacket(UUID uuid, UUID uuid2) {
      this.uuid = uuid;
      this.uuid2 = uuid2;
   }

   public void onReceive() {
      Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(this.uuid);
      Profile target = Clubs.getInstance().getProfileHandler().getProfileByUUID(this.uuid2);
      profile.getFriends().remove(target.getUuid());
      target.getFriends().remove(profile.getUuid());
      profile.save();
      target.save();
   }

   public void onSend() {
      Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(this.uuid);
      Profile target = Clubs.getInstance().getProfileHandler().getProfileByUUID(this.uuid2);
      profile.getFriends().remove(target.getUuid());
      target.getFriends().remove(profile.getUuid());
      profile.save();
      target.save();
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public UUID getUuid2() {
      return this.uuid2;
   }

   public void setUuid(UUID uuid) {
      this.uuid = uuid;
   }

   public void setUuid2(UUID uuid2) {
      this.uuid2 = uuid2;
   }
}
