package lol.yakut.clubs.papi;

import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.club.Club;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.util.CC;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ProfilePlaceholders extends PlaceholderExpansion {
   @NotNull
   public String getIdentifier() {
      return "yclub";
   }

   @NotNull
   public String getAuthor() {
      return "yakut";
   }

   @NotNull
   public String getVersion() {
      return Clubs.getInstance().getDescription().getVersion();
   }

   public String onPlaceholderRequest(Player player, @NotNull String params) {
      Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(player.getUniqueId());
      if (params.equalsIgnoreCase("friends")) {
         if (profile.getFriends().isEmpty()) {
            return CC.translate("&cNone");
         } else {
            int count = 0;
            Iterator var11 = profile.getFriends().iterator();

            while(var11.hasNext()) {
               UUID uuid = (UUID)var11.next();
               Profile profile1 = Clubs.getInstance().getProfileHandler().getProfileByUUID(uuid);
               if (profile1.isOnline()) {
                  ++count;
               }
            }

            return String.valueOf(count);
         }
      } else if (params.equalsIgnoreCase("club")) {
         if (profile.getClub().isEmpty()) {
            return CC.translate("&cNone");
         } else {
            Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(profile.getClub().toLowerCase());
            List<UUID> totalUUIDs = new ArrayList();
            totalUUIDs.add(club.getLeader());
            totalUUIDs.addAll(club.getAdmins());
            totalUUIDs.addAll(club.getMembers());
            totalUUIDs.remove(profile.getUuid());
            int count = 0;
            Iterator var7 = totalUUIDs.iterator();

            while(var7.hasNext()) {
               UUID uuid = (UUID)var7.next();
               Profile profile1 = Clubs.getInstance().getProfileHandler().getProfileByUUID(uuid);
               if (profile1.isOnline()) {
                  ++count;
               }
            }

            return String.valueOf(count);
         }
      } else {
         return null;
      }
   }
}
