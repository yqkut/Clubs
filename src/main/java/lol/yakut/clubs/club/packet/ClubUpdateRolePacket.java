package lol.yakut.clubs.club.packet;

import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.club.Club;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.redis.packet.Packet;
import lol.yakut.clubs.role.Role;
import lol.yakut.clubs.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ClubUpdateRolePacket extends Packet {
   private UUID uuid;
   private Role role;
   private String name;

   public ClubUpdateRolePacket(UUID uuid, Role role, String name) {
      this.uuid = uuid;
      this.role = role;
      this.name = name;
   }

   public void onReceive() {
      Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(this.name.toLowerCase());
      club.getAdmins().remove(this.uuid);
      club.getMembers().remove(this.uuid);
      if (this.role == Role.LEADER) {
         UUID leaderUUID = club.getLeader();
         club.setLeader(this.uuid);
         club.getMembers().add(leaderUUID);
      } else if (this.role == Role.ADMIN) {
         club.getAdmins().add(this.uuid);
      } else {
         club.getMembers().add(this.uuid);
      }

      club.save();
      List<UUID> totalMembers = new ArrayList();
      totalMembers.add(club.getLeader());
      totalMembers.addAll(club.getAdmins());
      totalMembers.addAll(club.getMembers());
      Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(this.uuid);
      Iterator var4 = totalMembers.iterator();

      while(var4.hasNext()) {
         UUID uuid = (UUID)var4.next();
         Player player = Bukkit.getPlayer(uuid);
         if (player == null) {
            return;
         }

         player.sendMessage(CC.translate("&b&lClub &7» &b" + profile.getUsername() + "&f's role was set to &b" + this.role.getName() + "&f."));
      }

   }

   public void onSend() {
      Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(this.name.toLowerCase());
      club.getAdmins().remove(this.uuid);
      club.getMembers().remove(this.uuid);
      if (this.role == Role.LEADER) {
         UUID leaderUUID = club.getLeader();
         club.setLeader(this.uuid);
         club.getMembers().add(leaderUUID);
      } else if (this.role == Role.ADMIN) {
         club.getAdmins().add(this.uuid);
      } else {
         club.getMembers().add(this.uuid);
      }

      club.save();
      List<UUID> totalMembers = new ArrayList();
      totalMembers.add(club.getLeader());
      totalMembers.addAll(club.getAdmins());
      totalMembers.addAll(club.getMembers());
      Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(this.uuid);
      Iterator var4 = totalMembers.iterator();

      while(var4.hasNext()) {
         UUID uuid = (UUID)var4.next();
         Player player = Bukkit.getPlayer(uuid);
         if (player == null) {
            return;
         }

         player.sendMessage(CC.translate("&b&lClub &7» &b" + profile.getUsername() + "&f's role was set to &b" + this.role.getName().toLowerCase() + "&f."));
      }

   }

   public UUID getUuid() {
      return this.uuid;
   }

   public Role getRole() {
      return this.role;
   }

   public String getName() {
      return this.name;
   }

   public void setUuid(UUID uuid) {
      this.uuid = uuid;
   }

   public void setRole(Role role) {
      this.role = role;
   }

   public void setName(String name) {
      this.name = name;
   }
}
