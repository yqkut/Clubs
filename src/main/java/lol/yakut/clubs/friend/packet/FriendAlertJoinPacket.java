package lol.yakut.clubs.friend.packet;

import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.redis.packet.Packet;
import lol.yakut.clubs.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class FriendAlertJoinPacket extends Packet {
   private Profile profile;
   private boolean join;

   public FriendAlertJoinPacket(Profile profile, boolean join) {
      this.profile = profile;
      this.join = join;
   }

   public void onReceive() {
      List<UUID> friends = this.profile.getFriends();
      Iterator var2 = friends.iterator();

      while(var2.hasNext()) {
         UUID uuid = (UUID)var2.next();
         Player player = Bukkit.getPlayer(uuid);
         if (player == null) {
            return;
         }

         if (this.join) {
            player.sendMessage(CC.translate("&b&lFriend &7&l» &b" + this.profile.getUsername() + " &fjoined."));
         } else {
            player.sendMessage(CC.translate("&b&lFriend &7&l» &b" + this.profile.getUsername() + " &fleft."));
         }
      }

   }

   public void onSend() {
      List<UUID> friends = this.profile.getFriends();
      Iterator var2 = friends.iterator();

      while(var2.hasNext()) {
         UUID uuid = (UUID)var2.next();
         Player player = Bukkit.getPlayer(uuid);
         if (player == null) {
            return;
         }

         if (this.join) {
            player.sendMessage(CC.translate("&b&lFriend &7&l» &b" + this.profile.getUsername() + " &fjoined."));
         } else {
            player.sendMessage(CC.translate("&b&lFriend &7&l» &b" + this.profile.getUsername() + " &fleft."));
         }
      }

   }

   public Profile getProfile() {
      return this.profile;
   }

   public boolean isJoin() {
      return this.join;
   }

   public void setProfile(Profile profile) {
      this.profile = profile;
   }

   public void setJoin(boolean join) {
      this.join = join;
   }
}
