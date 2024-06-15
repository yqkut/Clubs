package lol.yakut.clubs.club.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.ErrorMessage;
import lol.yakut.clubs.club.Club;
import lol.yakut.clubs.club.packet.ClubInvitePacket;
import lol.yakut.clubs.invite.ClubInvite;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.util.CC;
import lol.yakut.clubs.util.fanciful.mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ClubsInviteCommand {
   @Command(
      name = "invite",
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
         } else if (profile.getClub().isEmpty()) {
            sender.sendMessage(CC.translate("&cYou're not in a club."));
         } else {
            Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(profile.getClub().toLowerCase());
            Profile targetProfile = Clubs.getInstance().getProfileHandler().getProfileByUUID(target.getUniqueId());
            if (!targetProfile.getClub().isEmpty()) {
               sender.sendMessage(CC.translate("&b" + target.getName() + " &cis already in a club."));
            } else if (club.getMembers().contains(player.getUniqueId())) {
               sender.sendMessage(CC.translate("&cInviting &b" + target.getName() + " &crequires &bLeader &cor &bAdmin &crole."));
            } else {
               List<UUID> totalMembers = new ArrayList();
               totalMembers.add(club.getLeader());
               totalMembers.addAll(club.getAdmins());
               totalMembers.addAll(club.getMembers());
               if (totalMembers.size() + 1 > club.getLimit()) {
                  sender.sendMessage(CC.translate("&cThis club has reached the maximum number of players."));
               } else {
                  if (!club.getClubInvites().isEmpty()) {
                     Iterator var8 = club.getClubInvites().iterator();

                     while(var8.hasNext()) {
                        ClubInvite invite = (ClubInvite)var8.next();
                        if (invite.isExpired()) {
                           club.getClubInvites().remove(invite);
                        }

                        if (!invite.isExpired() && invite.getTarget().equals(target.getUniqueId())) {
                           sender.sendMessage(CC.translate("&cYou've already sent a club invitation to &b" + target.getName() + "&c, please wait."));
                           return;
                        }
                     }
                  }

                  club.getClubInvites().add(new ClubInvite(target.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60L)));
                  club.save();
                  ClubInvitePacket packet = new ClubInvitePacket(player.getName(), target.getName(), club.getLowercaseName());
                  Clubs.getInstance().getRedisHandler().sendPacket(packet);
                  target.sendMessage("");
                  (new FancyMessage("Club Invitation\n")).color(ChatColor.WHITE).style(ChatColor.BOLD).then(" • ").style(ChatColor.BOLD).color(ChatColor.WHITE).then("From: ").color(ChatColor.WHITE).then(club.getName() + "\n").color(ChatColor.AQUA).then(" [CLICK TO ACCEPT]").color(ChatColor.GREEN).style(ChatColor.BOLD).tooltip(ChatColor.GREEN + "Click to accept").command("/clubs accept " + club.getName()).send(target);
                  target.sendMessage("");
               }
            }
         }
      }
   }
}
