package lol.yakut.clubs.friend.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.ErrorMessage;
import lol.yakut.clubs.friend.FriendRequest;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.util.CC;
import lol.yakut.clubs.util.fanciful.mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FriendsAddCommand {
   @Command(
      name = "add",
      desc = ""
   )
   public void execute(@Sender CommandSender sender, Player target) {
      if (!(sender instanceof Player)) {
         sender.sendMessage(ErrorMessage.IN_GAME_COMMAND_ONLY);
      } else {
         Player player = (Player)sender;
         Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(player.getUniqueId());
         if (profile == null) {
            sender.sendMessage(ErrorMessage.PROFILE_ERROR);
         } else if (target == player) {
            sender.sendMessage(CC.translate("&cYou can't add yourself."));
         } else if (profile.getFriends().contains(target.getUniqueId())) {
            sender.sendMessage(CC.translate("&b" + target.getName() + " &fis already your friend."));
         } else {
            List<UUID> friends = profile.getFriends();
            if (friends.size() + 1 > 300 && !player.hasPermission("friends.limit.ace")) {
               sender.sendMessage(CC.translate("&cYou've reached the maximum number of friends."));
            } else if (friends.size() + 1 > 200 && !player.hasPermission("friends.limit.mvp")) {
               sender.sendMessage(CC.translate("&cYou've reached the maximum number of friends."));
            } else if (friends.size() + 1 > 175 && !player.hasPermission("friends.limit.pro")) {
               sender.sendMessage(CC.translate("&cYou've reached the maximum number of friends."));
            } else if (friends.size() + 1 > 150 && !player.hasPermission("friends.limit.vip")) {
               sender.sendMessage(CC.translate("&cYou've reached the maximum number of friends."));
            } else {
               if (!profile.getFriendRequests().isEmpty()) {
                  Iterator var6 = profile.getFriendRequests().iterator();

                  while(var6.hasNext()) {
                     FriendRequest request = (FriendRequest)var6.next();
                     if (request.isExpired()) {
                        profile.getFriendRequests().remove(request);
                     }

                     if (!request.isExpired() && request.getTarget().equals(target.getUniqueId())) {
                        sender.sendMessage(CC.translate("&cYou've already sent a friend request to &b" + target.getName() + "&c, please wait."));
                        return;
                     }
                  }
               }

               profile.getFriendRequests().add(new FriendRequest(target.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60L)));
               profile.save();
               sender.sendMessage("");
               sender.sendMessage(CC.translate("&fYou've sent a friend request to &b" + target.getName() + "&f."));
               sender.sendMessage("");
               target.sendMessage("");
               (new FancyMessage("Friend Request\n")).color(ChatColor.WHITE).style(ChatColor.BOLD).then(" • ").style(ChatColor.BOLD).color(ChatColor.WHITE).then("From: ").color(ChatColor.WHITE).then(player.getName() + "\n").color(ChatColor.AQUA).then(" [CLICK TO ACCEPT]").color(ChatColor.GREEN).style(ChatColor.BOLD).tooltip(ChatColor.GREEN + "Click to accept").command("/friends accept " + sender.getName()).send(target);
               target.sendMessage("");
            }
         }
      }
   }
}
