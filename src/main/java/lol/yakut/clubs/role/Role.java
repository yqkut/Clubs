package lol.yakut.clubs.role;

public enum Role {
   LEADER("Leader"),
   ADMIN("Admin"),
   MEMBER("Member");

   private final String name;

   private Role(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }
}
