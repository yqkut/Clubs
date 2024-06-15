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

public class ClubKickPacket extends Packet {
   private UUID uuid;
   private String kicker;
   private String name;

   public ClubKickPacket(UUID uuid, String kicker, String name) {
      this.uuid = uuid;
      this.kicker = kicker;
      this.name = name;
   }

   public void onReceive() {
      Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(this.name.toLowerCase());
      Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(this.uuid);
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

         player.sendMessage(CC.translate("&b&lClub &7» &b" + profile.getUsername() + " &fwas kicked by &b" + this.kicker + "&f."));
      }

      profile.setClub("");
      profile.save();
      club.getAdmins().remove(this.uuid);
      club.getMembers().remove(this.uuid);
      club.save();
   }

   public void onSend() {
      Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(this.name.toLowerCase());
      Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(this.uuid);
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

         player.sendMessage(CC.translate("&b&lClub &7» &b" + profile.getUsername() + " &fwas kicked by &b" + this.kicker + "&f."));
      }

      profile.setClub("");
      profile.save();
      club.getAdmins().remove(this.uuid);
      club.getMembers().remove(this.uuid);
      club.save();
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public String getKicker() {
      return this.kicker;
   }

   public String getName() {
      return this.name;
   }

   public void setUuid(UUID uuid) {
      this.uuid = uuid;
   }

   public void setKicker(String kicker) {
      this.kicker = kicker;
   }

   public void setName(String name) {
      this.name = name;
   }
}
