package lol.yakut.clubs.club.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.OptArg;
import com.jonahseguin.drink.annotation.Sender;
import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.ErrorMessage;
import lol.yakut.clubs.club.Club;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.util.CC;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class ClCommand {
   @Command(
      name = "",
      desc = ""
   )
   public void execute(@Sender CommandSender sender, @OptArg Profile target) {
      if (!(sender instanceof Player)) {
         sender.sendMessage(ErrorMessage.IN_GAME_COMMAND_ONLY);
      } else {
         Player player = (Player)sender;
         Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(player.getUniqueId());
         if (target == null) {
            target = profile;
         }

         if (target.getClub().isEmpty()) {
            sender.sendMessage(CC.translate("&b" + target.getUsername() + " &cisn't in a club."));
         } else {
            Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(target.getClub().toLowerCase());
            if (club == null) {
               sender.sendMessage(CC.translate("&b" + target.getUsername() + " &cisn't in a club."));
            } else {
               sender.sendMessage("");
               sender.sendMessage(CC.translate("&f&l" + club.getName()));
               Profile leaderProfile = Clubs.getInstance().getProfileHandler().getProfileByUUID(club.getLeader());
               sender.sendMessage(CC.translate("&f&l• &fLeader: &b" + leaderProfile.getUsername() + (leaderProfile.isOnline() ? " &a&l•" : " &c&l•")));
               ArrayList memberProfiles;
               Iterator var8;
               UUID uuid;
               Profile profile1;
               ArrayList memberStrings;
               Iterator var12;
               if (!club.getAdmins().isEmpty()) {
                  memberProfiles = new ArrayList();
                  var8 = club.getAdmins().iterator();

                  while(var8.hasNext()) {
                     uuid = (UUID)var8.next();
                     memberProfiles.add(Clubs.getInstance().getProfileHandler().getProfileByUUID(uuid));
                  }

                  memberStrings = new ArrayList();
                  var12 = memberProfiles.iterator();

                  while(var12.hasNext()) {
                     profile1 = (Profile)var12.next();
                     memberStrings.add("&b" + profile1.getUsername() + (profile1.isOnline() ? " &a&l•" : " &c&l•"));
                  }

                  sender.sendMessage(CC.translate("&f&l• &fAdmins (&b" + club.getAdmins().size() + "&f): &r" + StringUtils.join((Iterable)memberStrings, "&f,")));
               }

               if (!club.getMembers().isEmpty()) {
                  memberProfiles = new ArrayList();
                  var8 = club.getMembers().iterator();

                  while(var8.hasNext()) {
                     uuid = (UUID)var8.next();
                     memberProfiles.add(Clubs.getInstance().getProfileHandler().getProfileByUUID(uuid));
                  }

                  memberStrings = new ArrayList();
                  var12 = memberProfiles.iterator();

                  while(var12.hasNext()) {
                     profile1 = (Profile)var12.next();
                     memberStrings.add("&b" + profile1.getUsername() + (profile1.isOnline() ? " &a&l•" : " &c&l•"));
                  }

                  sender.sendMessage(CC.translate("&f&l• &fMembers (&b" + club.getMembers().size() + "&f): &r" + StringUtils.join((Iterable)memberStrings, "&f,")));
               }

               sender.sendMessage("");
            }
         }
      }
   }
}
