package lol.yakut.clubs.friend.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.ErrorMessage;
import lol.yakut.clubs.friend.FriendRequest;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class FriendsAcceptCommand {
   @Command(
      name = "accept",
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
         } else {
            Profile targetProfile = Clubs.getInstance().getProfileHandler().getProfileByUUID(target.getUniqueId());
            if (target == player) {
               sender.sendMessage(CC.translate("&cYou can't accept yourself."));
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
                  FriendRequest lookingForRequest = null;
                  Iterator var8 = targetProfile.getFriendRequests().iterator();

                  while(var8.hasNext()) {
                     FriendRequest request = (FriendRequest)var8.next();
                     if (request.isExpired()) {
                        targetProfile.getFriendRequests().remove(request);
                        return;
                     }

                     if (request.getTarget().equals(player.getUniqueId())) {
                        lookingForRequest = request;
                        if (request.isExpired()) {
                           targetProfile.getFriendRequests().remove(request);
                           sender.sendMessage(CC.translate("&cYou don't have a friend request from &b" + target.getName() + "&c."));
                           return;
                        }

                        profile.getFriends().add(target.getUniqueId());
                        targetProfile.getFriends().add(player.getUniqueId());
                        sender.sendMessage("");
                        sender.sendMessage(CC.translate("&aYou've accepted &b" + target.getName() + "&a's friend request."));
                        sender.sendMessage("");
                        target.sendMessage("");
                        target.sendMessage(CC.translate("&b" + sender.getName() + " &ahas accepted your friend request."));
                        target.sendMessage("");
                        profile.save();
                        targetProfile.save();
                     }
                  }

                  if (lookingForRequest == null) {
                     sender.sendMessage(CC.translate("&cYou don't have a friend request from &b" + target.getName() + "&c."));
                  }

               }
            }
         }
      }
   }
}
