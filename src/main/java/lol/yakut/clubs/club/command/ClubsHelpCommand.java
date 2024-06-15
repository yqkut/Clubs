package lol.yakut.clubs.club.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lol.yakut.clubs.ErrorMessage;
import lol.yakut.clubs.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClubsHelpCommand {
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
         sender.sendMessage(CC.translate("&f&lClub Help Menu"));
         sender.sendMessage(CC.translate(""));
         sender.sendMessage(CC.translate(" &f/club role &b<player> <role> &f- &7Changes a player's role in your club."));
         sender.sendMessage(CC.translate(" &f/club show &b<name> &f- &7View a specific club."));
         sender.sendMessage(CC.translate(" &f/club info &b[player] &f- &7View a player's club."));
         sender.sendMessage(CC.translate(" &f/club accept &b<player> &f- &7Accept an invitation."));
         sender.sendMessage(CC.translate(" &f/club kick &b<player> &f- &7Kick a player in your club."));
         sender.sendMessage(CC.translate(" &f/club chat &b[msg] &f- &7Enable club chat."));
         sender.sendMessage(CC.translate(" &f/club leave &f- &7Leave your club."));
         sender.sendMessage(CC.translate(" &f/club create &b<name> &f- &7Create a club."));
         sender.sendMessage(CC.translate(" &f/club invite &b<player> &f- &7Invite to your club."));
         sender.sendMessage("");
      }
   }
}
