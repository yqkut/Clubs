package lol.yakut.clubs.club.packet;

import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.club.Club;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.redis.packet.Packet;
import lol.yakut.clubs.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ClubDisbandPacket extends Packet {
   private String name;

   public ClubDisbandPacket(String name) {
      this.name = name;
   }

   public void onReceive() {
      Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(this.name.toLowerCase());
      List<UUID> totalMembers = new ArrayList();
      totalMembers.add(club.getLeader());
      totalMembers.addAll(club.getAdmins());
      totalMembers.addAll(club.getMembers());
      Iterator var3 = totalMembers.iterator();

      while(var3.hasNext()) {
         UUID uuid = (UUID)var3.next();
         Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(uuid);
         Player player = Bukkit.getPlayer(uuid);
         if (club.getLeader() == uuid) {
            player.sendMessage(CC.translate("&cYou've disbanded your club."));
         } else {
            if (player == null) {
               return;
            }

            player.sendMessage(CC.translate("&b&lClub &7» &b" + club.getName() + " &fhas been disbanded."));
         }

         profile.setClub("");
         profile.save();
      }

      Clubs.getInstance().getClubHandler().deleteClubByLowercaseName(club.getLowercaseName());
   }

   public void onSend() {
      Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(this.name.toLowerCase());
      List<UUID> totalMembers = new ArrayList();
      totalMembers.add(club.getLeader());
      totalMembers.addAll(club.getAdmins());
      totalMembers.addAll(club.getMembers());
      Iterator var3 = totalMembers.iterator();

      while(var3.hasNext()) {
         UUID uuid = (UUID)var3.next();
         Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(uuid);
         Player player = Bukkit.getPlayer(uuid);
         if (club.getLeader() == uuid) {
            player.sendMessage(CC.translate("&cYou've disbanded your club."));
         } else {
            if (player == null) {
               return;
            }

            player.sendMessage(CC.translate("&b&lClub &7» &b" + club.getName() + " &fhas been disbanded."));
         }

         profile.setClub("");
         profile.save();
      }

      Clubs.getInstance().getClubHandler().deleteClubByLowercaseName(club.getLowercaseName());
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
