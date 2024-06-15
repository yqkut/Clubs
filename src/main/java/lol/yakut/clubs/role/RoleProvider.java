package lol.yakut.clubs.role;

import com.jonahseguin.drink.argument.CommandArg;
import com.jonahseguin.drink.exception.CommandExitMessage;
import com.jonahseguin.drink.parametric.DrinkProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class RoleProvider extends DrinkProvider<Role> {
   public boolean doesConsumeArgument() {
      return true;
   }

   public boolean isAsync() {
      return false;
   }

   @Nullable
   public Role provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
      String name = arg.get();
      Role[] var4 = Role.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Role role = var4[var6];
         if (role.getName().equalsIgnoreCase(name)) {
            return role;
         }
      }

      return null;
   }

   public List<String> getSuggestions(@Nonnull String prefix) {
      List<String> suggestions = new ArrayList();
      Role[] var3 = Role.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Role role = var3[var5];
         if (role.getName().toLowerCase().startsWith(prefix.toLowerCase())) {
            suggestions.add(role.getName());
         }
      }

      return suggestions;
   }

   public String argumentDescription() {
      return null;
   }
}
