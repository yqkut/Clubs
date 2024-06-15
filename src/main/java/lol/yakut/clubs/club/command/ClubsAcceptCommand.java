package lol.yakut.clubs.club.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.ErrorMessage;
import lol.yakut.clubs.club.Club;
import lol.yakut.clubs.club.packet.ClubJoinPacket;
import lol.yakut.clubs.invite.ClubInvite;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ClubsAcceptCommand {
   @Command(
      name = "accept",
      desc = ""
   )
   public void execute(@Sender CommandSender sender, String name) {
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
            if (club == null) {
               sender.sendMessage(ErrorMessage.COULDNT_RESOLVE_CLUB);
            } else if (!profile.getClub().isEmpty()) {
               sender.sendMessage(CC.translate("&cYou're already in a club."));
            } else {
               List<UUID> totalMembers = new ArrayList();
               totalMembers.add(club.getLeader());
               totalMembers.addAll(club.getAdmins());
               totalMembers.addAll(club.getMembers());
               if (totalMembers.size() + 1 > club.getLimit()) {
                  sender.sendMessage(CC.translate("&cThis club has reached the maximum number of players."));
               } else {
                  ClubInvite lookingforInvite = null;
                  Iterator var8 = club.getClubInvites().iterator();

                  while(var8.hasNext()) {
                     ClubInvite invite = (ClubInvite)var8.next();
                     if (invite.isExpired()) {
                        club.getClubInvites().remove(invite);
                        return;
                     }

                     if (invite.getTarget().equals(player.getUniqueId())) {
                        lookingforInvite = invite;
                        if (invite.isExpired()) {
                           club.getClubInvites().remove(invite);
                           sender.sendMessage(CC.translate("&cYou don't have a club invite from &b" + club.getName() + "&c."));
                           return;
                        }

                        sender.sendMessage("");
                        sender.sendMessage(CC.translate("&aYou've accepted &b" + club.getName() + "&a's club invitation."));
                        sender.sendMessage("");
                        ClubJoinPacket packet = new ClubJoinPacket(player.getUniqueId(), name);
                        Clubs.getInstance().getRedisHandler().sendPacket(packet);
                     }
                  }

                  if (lookingforInvite == null) {
                     sender.sendMessage(CC.translate("&cYou don't have a club invite from &b" + club.getName() + "&c."));
                  }

               }
            }
         }
      }
   }
}
