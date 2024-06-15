package lol.yakut.clubs.club.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.ErrorMessage;
import lol.yakut.clubs.club.Club;
import lol.yakut.clubs.club.packet.ClubUpdateRolePacket;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.role.Role;
import lol.yakut.clubs.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClubsRoleCommand {
   @Command(
      name = "role",
      desc = ""
   )
   public void execute(@Sender CommandSender sender, Profile target, Role role) {
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
            sender.sendMessage(CC.translate("&cYou can't update your club role."));
         } else {
            Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(profile.getClub().toLowerCase());
            if (!player.getUniqueId().equals(club.getLeader())) {
               sender.sendMessage(CC.translate("&cSwitching &b" + target.getUsername() + "&c's role requires &bLeader &crole."));
            } else if (role == null) {
               sender.sendMessage(CC.translate("&cAvailable roles: &bAdmin &cor &bMember"));
            } else {
               List<UUID> totalMembers = new ArrayList();
               totalMembers.add(club.getLeader());
               totalMembers.addAll(club.getAdmins());
               totalMembers.addAll(club.getMembers());
               if (!totalMembers.contains(target.getUuid())) {
                  sender.sendMessage(CC.translate("&b" + target.getUsername() + " &cisn't in your club."));
               } else {
                  ClubUpdateRolePacket packet = new ClubUpdateRolePacket(target.getUuid(), role, club.getName());
                  Clubs.getInstance().getRedisHandler().sendPacket(packet);
               }
            }
         }
      }
   }
}
