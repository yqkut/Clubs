package lol.yakut.clubs.club.packet;

import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.club.Club;
import lol.yakut.clubs.redis.packet.Packet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ClubMessagePacket extends Packet {
   private String sender;
   private String name;
   private String message;

   public ClubMessagePacket(String sender, String name, String message) {
      this.sender = sender;
      this.name = name;
      this.message = message;
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
         Player player = Bukkit.getPlayer(uuid);
         if (player == null) {
            return;
         }

         player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "Club " + ChatColor.GRAY + "» " + ChatColor.AQUA + this.sender + ChatColor.GRAY + ": " + ChatColor.WHITE + this.message);
      }

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
         Player player = Bukkit.getPlayer(uuid);
         if (player == null) {
            return;
         }

         player.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + "Club " + ChatColor.GRAY + "» " + ChatColor.AQUA + this.sender + ChatColor.GRAY + ": " + ChatColor.WHITE + this.message);
      }

   }

   public String getSender() {
      return this.sender;
   }

   public String getName() {
      return this.name;
   }

   public String getMessage() {
      return this.message;
   }

   public void setSender(String sender) {
      this.sender = sender;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
