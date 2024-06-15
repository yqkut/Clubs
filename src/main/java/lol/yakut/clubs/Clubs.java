package lol.yakut.clubs;

import com.jonahseguin.drink.command.DrinkCommandService;
import lol.yakut.clubs.club.ClubHandler;
import lol.yakut.clubs.club.command.*;
import lol.yakut.clubs.friend.command.*;
import lol.yakut.clubs.listener.ChatListener;
import lol.yakut.clubs.listener.GeneralListener;
import lol.yakut.clubs.mongo.MongoHandler;
import lol.yakut.clubs.papi.ProfilePlaceholders;
import lol.yakut.clubs.profile.Profile;
import lol.yakut.clubs.profile.ProfileHandler;
import lol.yakut.clubs.profile.ProfileListener;
import lol.yakut.clubs.profile.ProfileProvider;
import lol.yakut.clubs.redis.RedisHandler;
import lol.yakut.clubs.role.Role;
import lol.yakut.clubs.role.RoleProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;

public class Clubs extends JavaPlugin {
   public static Clubs instance;
   private MongoHandler mongoHandler;
   private RedisHandler redisHandler;
   private ProfileHandler profileHandler;
   private ClubHandler clubHandler;

   public void onEnable() {
      instance = this;
      if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
         (new ProfilePlaceholders()).register();
      }

      this.saveDefaultConfig();
      this.registerHandlers();
      this.registerListeners();
      this.registerCommands();
   }

   public void onDisable() {
      Iterator var1 = this.profileHandler.getProfiles().iterator();

      while(var1.hasNext()) {
         Profile profile = (Profile)var1.next();
         profile.setLastSeenOn(System.currentTimeMillis());
         profile.save();
         this.profileHandler.getProfiles().remove(profile);
      }

      instance = null;
   }

   private void setupMongoRedisHandler() {
      boolean uri = this.getConfig().getBoolean("MONGO.URI.ENABLED");
      String connectionString = this.getConfig().getString("MONGO.URI.CONNECTION-STRING");
      String host = this.getConfig().getString("MONGO.DEFAULT.HOST");
      int port = this.getConfig().getInt("MONGO.DEFAULT.PORT");
      String database = this.getConfig().getString("MONGO.DEFAULT.DATABASE");
      boolean authentication = this.getConfig().getBoolean("MONGO.DEFAULT.AUTHENTICATION.ENABLED");
      String username = this.getConfig().getString("MONGO.DEFAULT.AUTHENTICATION.USERNAME");
      String password = this.getConfig().getString("MONGO.DEFAULT.AUTHENTICATION.PASSWORD");
      this.mongoHandler = new MongoHandler(uri, connectionString, host, port, database, authentication, username, password);
      host = this.getConfig().getString("REDIS.HOST");
      port = this.getConfig().getInt("REDIS.PORT");
      String channel = this.getConfig().getString("REDIS.CHANNEL");
      password = this.getConfig().getString("REDIS.PASSWORD");
      this.redisHandler = new RedisHandler(host, port, channel, password);
      this.redisHandler.connect();
   }

   private void registerHandlers() {
      this.setupMongoRedisHandler();
      this.profileHandler = new ProfileHandler();
      this.clubHandler = new ClubHandler();
   }

   private void registerListeners() {
      Bukkit.getPluginManager().registerEvents(new GeneralListener(), this);
      Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
      Bukkit.getPluginManager().registerEvents(new ProfileListener(), this);
   }

   private void registerCommands() {
      DrinkCommandService drink = new DrinkCommandService(this);
      drink.bind(Profile.class).toProvider(new ProfileProvider());
      drink.bind(Role.class).toProvider(new RoleProvider());
      drink.register(new FriendsHelpCommand(), "friends", "friend", "fri", "fr", "f").registerSub(new FriendsListCommand()).registerSub(new FriendsAddCommand()).registerSub(new FriendsAcceptCommand()).registerSub(new FriendsRemoveCommand());
      drink.register(new ClubsHelpCommand(), "clubs", "club", "cl").registerSub(new ClubsCreateCommand()).registerSub(new ClubsLeaveCommand()).registerSub(new ClubsInviteCommand()).registerSub(new ClubsAcceptCommand()).registerSub(new ClubsRoleCommand()).registerSub(new ClubsKickCommand()).registerSub(new ClubsInformationCommand()).registerSub(new ClubsShowCommand()).registerSub(new ClubChatCommand()).registerSub(new ClubsDisbandCommand());
      drink.register(new CCCommand(), "cc");
      drink.register(new ClCommand(), "cl");
      drink.registerCommands();
   }

   public MongoHandler getMongoHandler() {
      return this.mongoHandler;
   }

   public RedisHandler getRedisHandler() {
      return this.redisHandler;
   }

   public ProfileHandler getProfileHandler() {
      return this.profileHandler;
   }

   public ClubHandler getClubHandler() {
      return this.clubHandler;
   }

   public void setMongoHandler(MongoHandler mongoHandler) {
      this.mongoHandler = mongoHandler;
   }

   public void setRedisHandler(RedisHandler redisHandler) {
      this.redisHandler = redisHandler;
   }

   public void setProfileHandler(ProfileHandler profileHandler) {
      this.profileHandler = profileHandler;
   }

   public void setClubHandler(ClubHandler clubHandler) {
      this.clubHandler = clubHandler;
   }

   public static Clubs getInstance() {
      return instance;
   }
}
