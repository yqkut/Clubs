package lol.yakut.clubs.listener;

import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.channel.ChatChannel;
import lol.yakut.clubs.club.packet.ClubMessagePacket;
import lol.yakut.clubs.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
      Player player = event.getPlayer();
      Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(player.getUniqueId());
      if (profile.getChatChannel() == ChatChannel.CLUB) {
         event.setCancelled(true);
         ClubMessagePacket packet = new ClubMessagePacket(player.getName(), profile.getClub(), event.getMessage());
         Clubs.getInstance().getRedisHandler().sendPacket(packet);
      }

   }
}
