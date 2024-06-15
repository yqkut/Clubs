package lol.yakut.clubs.profile;

import com.jonahseguin.drink.argument.CommandArg;
import com.jonahseguin.drink.exception.CommandExitMessage;
import com.jonahseguin.drink.parametric.DrinkProvider;
import lol.yakut.clubs.Clubs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProfileProvider extends DrinkProvider<Profile> {
   public boolean doesConsumeArgument() {
      return true;
   }

   public boolean isAsync() {
      return false;
   }

   public boolean allowNullArgument() {
      return true;
   }

   @Nullable
   public Profile defaultNullValue() {
      return null;
   }

   public List<String> getSuggestions(@Nonnull String prefix) {
      List<String> suggestions = new ArrayList();
      Iterator var3 = Clubs.getInstance().getProfileHandler().getProfiles().iterator();

      while(var3.hasNext()) {
         Profile profile = (Profile)var3.next();
         if (profile.getUsername().toLowerCase().startsWith(prefix.toLowerCase())) {
            suggestions.add(profile.getUsername());
         }
      }

      return suggestions;
   }

   @Nullable
   public Profile provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
      String name = arg.get();
      return name == null ? null : Clubs.getInstance().getProfileHandler().getProfileByUsername(name);
   }

   public String argumentDescription() {
      return "player";
   }
}
