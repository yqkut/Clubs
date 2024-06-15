package lol.yakut.clubs.club.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.OptArg;
import com.jonahseguin.drink.annotation.Sender;
import com.jonahseguin.drink.annotation.Text;
import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.ErrorMessage;
import lol.yakut.clubs.channel.ChatChannel;
import lol.yakut.clubs.club.packet.ClubMessagePacket;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClubChatCommand {
   @Command(
      name = "chat",
      desc = ""
   )
   public void execute(@Sender CommandSender sender, @OptArg @Text String message) {
      if (!(sender instanceof Player)) {
         sender.sendMessage(ErrorMessage.IN_GAME_COMMAND_ONLY);
      } else {
         Player player = (Player)sender;
         Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(player.getUniqueId());
         if (profile == null) {
            sender.sendMessage(ErrorMessage.PROFILE_ERROR);
         } else if (profile.getClub().isEmpty()) {
            sender.sendMessage(CC.translate("&cYou're not in a club."));
         } else if (!message.isEmpty()) {
            ClubMessagePacket packet = new ClubMessagePacket(player.getName(), profile.getClub(), message);
            Clubs.getInstance().getRedisHandler().sendPacket(packet);
         } else {
            if (profile.getChatChannel() == ChatChannel.NORMAL) {
               profile.setChatChannel(ChatChannel.CLUB);
               player.sendMessage(CC.translate("&aYou've enabled club chat."));
            } else {
               profile.setChatChannel(ChatChannel.NORMAL);
               player.sendMessage(CC.translate("&cYou've disabled club chat."));
            }

         }
      }
   }
}
