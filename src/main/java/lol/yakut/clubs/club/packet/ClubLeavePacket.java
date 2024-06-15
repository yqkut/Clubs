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

public class ClubLeavePacket extends Packet {
   private String name;
   private UUID uuid;

   public ClubLeavePacket(String name, UUID uuid) {
      this.name = name;
      this.uuid = uuid;
   }

   public void onReceive() {
      Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(this.name.toLowerCase());
      Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(this.uuid);
      profile.setClub("");
      profile.save();
      club.getAdmins().remove(this.uuid);
      club.getMembers().remove(this.uuid);
      club.save();
      Player player = Bukkit.getPlayer(this.uuid);
      if (player != null) {
         player.sendMessage(CC.translate("&cYou've left the club."));
      }

      List<UUID> totalMembers = new ArrayList();
      totalMembers.add(club.getLeader());
      totalMembers.addAll(club.getAdmins());
      totalMembers.addAll(club.getMembers());
      Iterator var5 = totalMembers.iterator();

      while(var5.hasNext()) {
         UUID uuid = (UUID)var5.next();
         Player player1 = Bukkit.getPlayer(uuid);
         if (player1 == null) {
            return;
         }

         player1.sendMessage(CC.translate("&b&lClub &7» &b" + player.getName() + " &fleft your club."));
      }

   }

   public void onSend() {
      Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(this.name.toLowerCase());
      Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(this.uuid);
      profile.setClub("");
      profile.save();
      club.getAdmins().remove(this.uuid);
      club.getMembers().remove(this.uuid);
      club.save();
      Player player = Bukkit.getPlayer(this.uuid);
      if (player != null) {
         player.sendMessage(CC.translate("&cYou've left the club."));
      }

      List<UUID> totalMembers = new ArrayList();
      totalMembers.add(club.getLeader());
      totalMembers.addAll(club.getAdmins());
      totalMembers.addAll(club.getMembers());
      Iterator var5 = totalMembers.iterator();

      while(var5.hasNext()) {
         UUID uuid = (UUID)var5.next();
         Player player1 = Bukkit.getPlayer(uuid);
         if (player1 == null) {
            return;
         }

         player1.sendMessage(CC.translate("&b&lClub &7» &b" + player.getName() + " &fleft your club."));
      }

   }

   public String getName() {
      return this.name;
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setUuid(UUID uuid) {
      this.uuid = uuid;
   }
}
