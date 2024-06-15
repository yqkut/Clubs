package lol.yakut.clubs.util;

import lol.yakut.clubs.Clubs;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {
   public static void runTaskAsync(Runnable runnable) {
      Clubs.getInstance().getServer().getScheduler().runTaskAsynchronously(Clubs.getInstance(), runnable);
   }

   public static void runTaskLater(Runnable runnable, long delay) {
      Clubs.getInstance().getServer().getScheduler().runTaskLater(Clubs.getInstance(), runnable, delay);
   }

   public static void runTaskTimer(BukkitRunnable runnable, long delay, long timer) {
      runnable.runTaskTimer(Clubs.getInstance(), delay, timer);
   }

   public static void runTaskTimer(Runnable runnable, long delay, long timer) {
      Clubs.getInstance().getServer().getScheduler().runTaskTimer(Clubs.getInstance(), runnable, delay, timer);
   }

   public static void runTask(Runnable runnable) {
      Clubs.getInstance().getServer().getScheduler().runTask(Clubs.getInstance(), runnable);
   }
}
