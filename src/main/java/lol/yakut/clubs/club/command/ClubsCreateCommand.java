package lol.yakut.clubs.club.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import com.jonahseguin.drink.annotation.Text;
import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.ErrorMessage;
import lol.yakut.clubs.club.Club;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClubsCreateCommand {
   @Command(
      name = "create",
      desc = ""
   )
   @Require("clubs.create")
   public void execute(@Sender CommandSender sender, @Text String name) {
      if (!(sender instanceof Player)) {
         sender.sendMessage(ErrorMessage.IN_GAME_COMMAND_ONLY);
      } else {
         Player player = (Player)sender;
         Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(player.getUniqueId());
         if (profile == null) {
            sender.sendMessage(ErrorMessage.PROFILE_ERROR);
         } else if (!profile.getClub().isEmpty()) {
            sender.sendMessage(CC.translate("&cYou're already in a club."));
         } else {
            Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(name.toLowerCase());
            if (club != null) {
               sender.sendMessage(CC.translate("&cThe name &b" + name + " &chas been taken."));
            } else {
               Club newClub = new Club(name, name.toLowerCase(), player.getUniqueId());
               if (player.hasPermission("friends.limit.pro")) {
                  newClub.setLimit(25);
               }

               if (player.hasPermission("friends.limit.mvp")) {
                  newClub.setLimit(35);
               }

               if (player.hasPermission("friends.limit.ace")) {
                  newClub.setLimit(50);
               }

               newClub.save();
               profile.setClub(name);
               profile.save();
               sender.sendMessage(CC.translate("&aYou've created a club with the name &2" + name + "&a."));
            }
         }
      }
   }
}
