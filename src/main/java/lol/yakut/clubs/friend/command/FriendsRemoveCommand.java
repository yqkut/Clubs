package lol.yakut.clubs.friend.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import lol.yakut.clubs.Clubs;
import lol.yakut.clubs.ErrorMessage;
import lol.yakut.clubs.friend.packet.FriendRemovePacket;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendsRemoveCommand {
   @Command(
      name = "remove",
      desc = ""
   )
   public void execute(@Sender CommandSender sender, Profile target) {
      if (!(sender instanceof Player)) {
         sender.sendMessage(ErrorMessage.IN_GAME_COMMAND_ONLY);
      } else if (target == null) {
         sender.sendMessage(ErrorMessage.COULDNT_RESOLVE_PROFILE);
      } else {
         Player player = (Player)sender;
         Profile profile = Clubs.getInstance().getProfileHandler().getProfileByUUID(player.getUniqueId());
         if (profile == null) {
            sender.sendMessage(ErrorMessage.PROFILE_ERROR);
         } else if (target.getUuid() == player.getUniqueId()) {
            sender.sendMessage(CC.translate("&cYou can't remove yourself."));
         } else if (!profile.getFriends().contains(target.getUuid())) {
            sender.sendMessage(CC.translate("&b" + target.getUsername() + " &cis not on your friends list."));
         } else {
            FriendRemovePacket packet = new FriendRemovePacket(player.getUniqueId(), target.getUuid());
            Clubs.getInstance().getRedisHandler().sendPacket(packet);
            sender.sendMessage("");
            sender.sendMessage(CC.translate("&aYou've removed &b" + target.getUsername() + " &afrom your friends list."));
            sender.sendMessage("");
         }
      }
   }
}
