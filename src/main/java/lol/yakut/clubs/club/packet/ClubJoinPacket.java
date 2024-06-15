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

public class ClubJoinPacket extends Packet {
   private UUID uuid;
   private String name;

   public ClubJoinPacket(UUID uuid, String name) {
      this.uuid = uuid;
      this.name = name;
   }

   public void onReceive() {
      Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(this.name.toLowerCase());
      Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(this.uuid);
      profile.setClub(this.name);
      profile.save();
      club.getMembers().add(this.uuid);
      club.save();
      List<UUID> totalMembers = new ArrayList();
      totalMembers.add(club.getLeader());
      totalMembers.addAll(club.getAdmins());
      totalMembers.addAll(club.getMembers());
      Iterator var4 = totalMembers.iterator();

      while(var4.hasNext()) {
         UUID uuid = (UUID)var4.next();
         Player player = Bukkit.getPlayer(uuid);
         if (player == null) {
            return;
         }

         player.sendMessage(CC.translate("&b&lClub &7» &b" + profile.getUsername() + " &fjoined the club."));
      }

   }

   public void onSend() {
      Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(this.name.toLowerCase());
      Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(this.uuid);
      profile.setClub(this.name);
      profile.save();
      club.getMembers().add(this.uuid);
      club.save();
      List<UUID> totalMembers = new ArrayList();
      totalMembers.add(club.getLeader());
      totalMembers.addAll(club.getAdmins());
      totalMembers.addAll(club.getMembers());
      Iterator var4 = totalMembers.iterator();

      while(var4.hasNext()) {
         UUID uuid = (UUID)var4.next();
         Player player = Bukkit.getPlayer(uuid);
         if (player == null) {
            return;
         }

         player.sendMessage(CC.translate("&b&lClub &7» &b" + profile.getUsername() + " &fjoined the club."));
      }

   }

   public UUID getUuid() {
      return this.uuid;
   }

   public String getName() {
      return this.name;
   }

   public void setUuid(UUID uuid) {
      this.uuid = uuid;
   }

   public void setName(String name) {
      this.name = name;
   }
}
