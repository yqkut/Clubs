package lol.yakut.clubs.invite;

import java.util.UUID;

public class ClubInvite {
   private UUID target;
   private long expiration;

   public ClubInvite(UUID target, long expiration) {
      this.target = target;
      this.expiration = expiration;
   }

   public boolean isExpired() {
      return System.currentTimeMillis() > this.expiration;
   }

   public UUID getTarget() {
      return this.target;
   }

   public long getExpiration() {
      return this.expiration;
   }

   public void setTarget(UUID target) {
      this.target = target;
   }

   public void setExpiration(long expiration) {
      this.expiration = expiration;
   }
}
