package lol.yakut.clubs.club.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import com.jonahseguin.drink.annotation.Text;
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

public class ClubsShowCommand {
   @Command(
      name = "show",
      desc = ""
   )
   public void execute(@Sender CommandSender sender, @Text String name) {
      if (!(sender instanceof Player)) {
         sender.sendMessage(ErrorMessage.IN_GAME_COMMAND_ONLY);
      } else {
         Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(name.toLowerCase());
         if (club == null) {
            sender.sendMessage(ErrorMessage.COULDNT_RESOLVE_CLUB);
         } else {
            sender.sendMessage("");
            sender.sendMessage(CC.translate("&f&l" + club.getName()));
            Profile leaderProfile = Clubs.getInstance().getProfileHandler().getProfileByUUID(club.getLeader());
            sender.sendMessage(CC.translate("&f&l• &fLeader: &b" + leaderProfile.getUsername() + (leaderProfile.isOnline() ? " &a&l•" : " &c&l•")));
            ArrayList memberProfiles;
            Iterator var6;
            UUID uuid;
            Profile profile1;
            ArrayList memberStrings;
            Iterator var10;
            if (!club.getAdmins().isEmpty()) {
               memberProfiles = new ArrayList();
               var6 = club.getAdmins().iterator();

               while(var6.hasNext()) {
                  uuid = (UUID)var6.next();
                  memberProfiles.add(Clubs.getInstance().getProfileHandler().getProfileByUUID(uuid));
               }

               memberStrings = new ArrayList();
               var10 = memberProfiles.iterator();

               while(var10.hasNext()) {
                  profile1 = (Profile)var10.next();
                  memberStrings.add("&b" + profile1.getUsername() + (profile1.isOnline() ? " &a&l•" : " &c&l•"));
               }

               sender.sendMessage(CC.translate("&f&l• &fAdmins (&b" + club.getAdmins().size() + "&f): &r" + StringUtils.join((Iterable)memberStrings, "&f,")));
            }

            if (!club.getMembers().isEmpty()) {
               memberProfiles = new ArrayList();
               var6 = club.getMembers().iterator();

               while(var6.hasNext()) {
                  uuid = (UUID)var6.next();
                  memberProfiles.add(Clubs.getInstance().getProfileHandler().getProfileByUUID(uuid));
               }

               memberStrings = new ArrayList();
               var10 = memberProfiles.iterator();

               while(var10.hasNext()) {
                  profile1 = (Profile)var10.next();
                  memberStrings.add("&b" + profile1.getUsername() + (profile1.isOnline() ? " &a&l•" : " &c&l•"));
               }

               sender.sendMessage(CC.translate("&f&l• &fMembers (&b" + club.getMembers().size() + "&f): &r" + StringUtils.join((Iterable)memberStrings, "&f,")));
            }

            sender.sendMessage("");
         }
      }
   }
}
