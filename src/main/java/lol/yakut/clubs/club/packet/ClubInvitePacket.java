package lol.yakut.clubs.club.packet;

import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.club.Club;
import lol.yakut.clubs.redis.packet.Packet;
import lol.yakut.clubs.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ClubInvitePacket extends Packet {
   private String sender;
   private String inviting;
   private String lowercaseName;

   public ClubInvitePacket(String sender, String inviting, String lowercaseName) {
      this.sender = sender;
      this.inviting = inviting;
      this.lowercaseName = lowercaseName;
   }

   public void onReceive() {
      Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(this.lowercaseName);
      List<UUID> totalMembers = new ArrayList();
      totalMembers.add(club.getLeader());
      totalMembers.addAll(club.getAdmins());
      totalMembers.addAll(club.getMembers());
      Iterator var3 = totalMembers.iterator();

      while(var3.hasNext()) {
         UUID uuid = (UUID)var3.next();
         Player player = Bukkit.getPlayer(uuid);
         if (player == null) {
            return;
         }

         player.sendMessage(CC.translate("&b&lClub &7» &b" + this.inviting + " &fwas invited by &b" + this.sender + "&f."));
      }

   }

   public void onSend() {
      Club club = Clubs.getInstance().getClubHandler().getClubByLowercaseName(this.lowercaseName);
      List<UUID> totalMembers = new ArrayList();
      totalMembers.add(club.getLeader());
      totalMembers.addAll(club.getAdmins());
      totalMembers.addAll(club.getMembers());
      Iterator var3 = totalMembers.iterator();

      while(var3.hasNext()) {
         UUID uuid = (UUID)var3.next();
         Player player = Bukkit.getPlayer(uuid);
         if (player == null) {
            return;
         }

         player.sendMessage(CC.translate("&b&lClub &7» &b" + this.inviting + " &fwas invited by &b" + this.sender + "&f."));
      }

   }

   public String getSender() {
      return this.sender;
   }

   public String getInviting() {
      return this.inviting;
   }

   public String getLowercaseName() {
      return this.lowercaseName;
   }

   public void setSender(String sender) {
      this.sender = sender;
   }

   public void setInviting(String inviting) {
      this.inviting = inviting;
   }

   public void setLowercaseName(String lowercaseName) {
      this.lowercaseName = lowercaseName;
   }
}
