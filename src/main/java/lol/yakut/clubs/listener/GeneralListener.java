package lol.yakut.clubs.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GeneralListener implements Listener {
   @EventHandler
   public void onPlayerJoinEvent(PlayerJoinEvent event) {
      event.setJoinMessage((String)null);
   }

   @EventHandler
   public void onPlayerQuitEvent(PlayerQuitEvent event) {
      event.setQuitMessage((String)null);
   }
}
