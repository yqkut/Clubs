package lol.yakut.clubs.club.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.ErrorMessage;
import lol.yakut.clubs.club.Club;
import lol.yakut.clubs.club.packet.ClubDisbandPacket;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClubsDisbandCommand {
   @Command(
      name = "disband",
      desc = ""
   )
   public void execute(@Sender CommandSender sender) {
      if (!(sender instanceof Player)) {
         sender.sendMessage(ErrorMessage.IN_GAME_COMMAND_ONLY);
      } else {
         Player player = (Player)sender;
         Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(player.getUniqueId());
         if (profile == null) {
            sender.sendMessage(ErrorMessage.PROFILE_ERROR);
         } else if (profile.getClub().isEmpty()) {
            sender.sendMessage(CC.translate("&cYou're not in a club."));
         } else {
            Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(profile.getClub().toLowerCase());
            if (!player.getUniqueId().equals(club.getLeader())) {
               sender.sendMessage(CC.translate("&cDisbanding requires &aLeader &crole."));
            } else {
               ClubDisbandPacket packet = new ClubDisbandPacket(club.getName());
               Clubs.getInstance().getRedisHandler().sendPacket(packet);
            }
         }
      }
   }
}
