package lol.yakut.clubs.profile;

import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.ErrorMessage;
import lol.yakut.clubs.friend.packet.FriendAlertJoinPacket;
import lol.yakut.clubs.util.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListener implements Listener {
   ProfileHandler profileHandler = Clubs.getInstance().getProfileHandler();

   @EventHandler
   public void onPlayerJoinEvent(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      Profile profile = new Profile(player.getUniqueId());
      profile.setUsername(player.getName());
      profile.setLastSeenOn(System.currentTimeMillis());
      if (profile.getLastSeenServer().equalsIgnoreCase("!@#$%^&*()")) {
         if (profile.getFriends().isEmpty()) {
            return;
         }

         FriendAlertJoinPacket packet = new FriendAlertJoinPacket(profile, true);
         Clubs.getInstance().getRedisHandler().sendPacket(packet);
      }

      profile.setLastSeenServer(Bukkit.getServerName());
      if (this.profileHandler.getProfileByUUID(player.getUniqueId()) == null) {
         player.kickPlayer(ErrorMessage.PROFILE_ERROR);
      } else {
         profile.save();
         this.profileHandler.getProfiles().add(profile);
      }
   }

   @EventHandler
   public void onPlayerQuitEvent(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      Profile profile = this.profileHandler.getProfileByUUID(player.getUniqueId());
      long lastSeenOn = System.currentTimeMillis();
      profile.setLastSeenOn(lastSeenOn);
      profile.save();
      TaskUtil.runTaskLater(() -> {
         Profile newProfile = this.profileHandler.getProfileByUUID(player.getUniqueId());
         if (newProfile.getLastSeenOn() == lastSeenOn) {
            newProfile.setLastSeenServer("!@#$%^&*()");
            newProfile.save();
            if (!newProfile.getFriends().isEmpty()) {
               FriendAlertJoinPacket packet = new FriendAlertJoinPacket(profile, false);
               Clubs.getInstance().getRedisHandler().sendPacket(packet);
            }
         }
      }, 60L);
      this.profileHandler.getProfiles().remove(profile);
   }

   @EventHandler
   public void onPlayerKickEvent(PlayerKickEvent event) {
      Player player = event.getPlayer();
      Profile profile = this.profileHandler.getProfileByUUID(player.getUniqueId());
      long lastSeenOn = System.currentTimeMillis();
      profile.setLastSeenOn(lastSeenOn);
      profile.setLastSeenServer("!@#$%^&*()");
      profile.save();
      this.profileHandler.getProfiles().remove(profile);
   }
}
