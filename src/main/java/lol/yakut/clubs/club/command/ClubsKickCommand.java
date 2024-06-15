package lol.yakut.clubs.club.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.ErrorMessage;
import lol.yakut.clubs.club.Club;
import lol.yakut.clubs.club.packet.ClubKickPacket;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClubsKickCommand {
   @Command(
      name = "kick",
      desc = ""
   )
   public void execute(@Sender CommandSender sender, Profile target) {
      if (!(sender instanceof Player)) {
         sender.sendMessage(ErrorMessage.IN_GAME_COMMAND_ONLY);
      } else if (target == null) {
         sender.sendMessage(ErrorMessage.COULDNT_RESOLVE_PROFILE);
      } else {
         Player player = (Player)sender;
         Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(player.getUniqueId());
         if (profile == null) {
            sender.sendMessage(ErrorMessage.PROFILE_ERROR);
         } else if (profile.getClub().isEmpty()) {
            sender.sendMessage(CC.translate("&cYou're not in a club."));
         } else if (player.getUniqueId().equals(target.getUuid())) {
            sender.sendMessage(CC.translate("&cYou kick yourself from your club."));
         } else {
            Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(profile.getClub().toLowerCase());
            if (target.getUuid().equals(club.getLeader())) {
               sender.sendMessage(CC.translate("&cYou can't kick the leader &b" + target.getUsername() + "&c."));
            } else if (!club.getMembers().contains(player.getUniqueId()) && !club.getAdmins().contains(player.getUniqueId())) {
               List<UUID> totalMembers = new ArrayList();
               totalMembers.add(club.getLeader());
               totalMembers.addAll(club.getAdmins());
               totalMembers.addAll(club.getMembers());
               if (!totalMembers.contains(target.getUuid())) {
                  sender.sendMessage(CC.translate("&b" + target.getUsername() + " &cisn't in your club."));
               } else {
                  ClubKickPacket packet = new ClubKickPacket(target.getUuid(), player.getName(), club.getName());
                  Clubs.getInstance().getRedisHandler().sendPacket(packet);
               }
            } else {
               sender.sendMessage(CC.translate("&cKicking &b" + target.getUsername() + " &crequires &bLeader &crole."));
            }
         }
      }
   }
}
