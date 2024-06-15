package lol.yakut.clubs.friend.command;

import com.google.common.collect.Lists;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.OptArg;
import com.jonahseguin.drink.annotation.Sender;
import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.ErrorMessage;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.util.CC;
import lol.yakut.clubs.util.TimeUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class FriendsListCommand {
   @Command(
           name = "list",
           desc = ""
   )
   public void execute(@Sender CommandSender sender, @OptArg String pageNumber) {
      int page = 1;
      try {
         if (pageNumber != null) {
            page = Integer.parseInt(pageNumber);
         }
      } catch (NumberFormatException e) {
         sender.sendMessage(CC.translate("&cInvalid page number."));
         return;
      }

      if (!(sender instanceof Player)) {
         sender.sendMessage(ErrorMessage.IN_GAME_COMMAND_ONLY);
         return;
      }

      Player player = (Player)sender;
      Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(player.getUniqueId());
      if (profile == null) {
         sender.sendMessage(ErrorMessage.PROFILE_ERROR);
         return;
      }

      List<UUID> friends = profile.getFriends();
      if (friends.isEmpty()) {
         sender.sendMessage(CC.translate("&cThis friends list is empty."));
         return;
      }

      if (page < 1) {
         page = 1;
      }

      List<List<UUID>> pages = Lists.partition(friends, 10);
      if (page > pages.size()) {
         sender.sendMessage(CC.translate("&cInvalid page number."));
         return;
      }

      List<UUID> selectedPage = pages.get(page - 1);
      sender.sendMessage("");
      sender.sendMessage(CC.translate("&f&lFriends List (&b&l" + friends.size() + "&f&l)"));
      sender.sendMessage("");
      for (UUID uuid : selectedPage) {
         Profile friendProfile = Clubs.getInstance().getProfileHandler().getProfileByUUID(uuid);
         String message = friendProfile.isOnline() ? " &a&l• &b{player} &fis on {server}" : " &c&l• &b{player} &fwas on {ago} ago.";
         sender.sendMessage(CC.translate(message.replace("{player}", friendProfile.getUsername()).replace("{server}", friendProfile.getLastSeenServer()).replace("{ago}", TimeUtil.convertLongToString(System.currentTimeMillis() - friendProfile.getLastSeenOn()))));
      }

      sender.sendMessage("");
      String pageInfo = " &fPage &b{page} &fof &b{pages}";
      sender.sendMessage(CC.translate(pageInfo.replace("{page}", String.valueOf(page)).replace("{pages}", String.valueOf(pages.size()))));
      sender.sendMessage("");
   }
}
