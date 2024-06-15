package lol.yakut.clubs.friend.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lol.yakut.clubs.ErrorMessage;
import lol.yakut.clubs.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendsHelpCommand {
   @Command(
      name = "",
      aliases = {"help"},
      desc = ""
   )
   public void execute(@Sender CommandSender sender) {
      if (!(sender instanceof Player)) {
         sender.sendMessage(ErrorMessage.IN_GAME_COMMAND_ONLY);
      } else {
         sender.sendMessage("");
         sender.sendMessage(CC.translate("&f&lFriends Help Menu"));
         sender.sendMessage(CC.translate(""));
         sender.sendMessage(CC.translate(" &f/friend add &b<player> &b- &7Send a friend request."));
         sender.sendMessage(CC.translate(" &f/friend list &b[page] &b- &7View your current friends."));
         sender.sendMessage(CC.translate(" &f/friend remove &b<player> &b- &7Remove a friend."));
         sender.sendMessage(CC.translate(" &f/friend accept &b<player> &b- &7Accept a friend request."));
         sender.sendMessage("");
      }
   }
}
