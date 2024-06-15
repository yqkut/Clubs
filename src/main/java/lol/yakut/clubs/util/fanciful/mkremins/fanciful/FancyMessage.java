package lol.yakut.clubs.util.fanciful.mkremins.fanciful;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import lol.yakut.clubs.util.fanciful.net.amoebaman.util.ArrayWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;

public class FancyMessage implements JsonRepresentedObject, Cloneable, Iterable<MessagePart>, ConfigurationSerializable {
   private List<MessagePart> messageParts;
   private String jsonString;
   private boolean dirty;
   private static JsonParser _stringParser;

   public FancyMessage clone() throws CloneNotSupportedException {
      FancyMessage instance = (FancyMessage)super.clone();
      instance.messageParts = new ArrayList<>(this.messageParts.size());

      for (int i = 0; i < this.messageParts.size(); ++i) {
         instance.messageParts.add(i, this.messageParts.get(i).clone());
      }

      instance.dirty = false;
      instance.jsonString = null;
      return instance;
   }

   public FancyMessage(String firstPartText) {
      this(TextualComponent.rawText(firstPartText));
   }

   public FancyMessage(TextualComponent firstPartText) {
      this.messageParts = new ArrayList<>();
      this.messageParts.add(new MessagePart(firstPartText));
      this.jsonString = null;
      this.dirty = false;
   }

   public FancyMessage() {
      this((TextualComponent)null);
   }

   public FancyMessage text(String text) {
      MessagePart latest = this.latest();
      latest.text = TextualComponent.rawText(text);
      this.dirty = true;
      return this;
   }

   public FancyMessage text(TextualComponent text) {
      MessagePart latest = this.latest();
      latest.text = text;
      this.dirty = true;
      return this;
   }

   public FancyMessage color(ChatColor color) {
      if (!color.isColor()) {
         throw new IllegalArgumentException(color.name() + " is not a color");
      } else {
         this.latest().color = color;
         this.dirty = true;
         return this;
      }
   }

   public FancyMessage style(ChatColor... styles) {
      for (ChatColor style : styles) {
         if (!style.isFormat()) {
            throw new IllegalArgumentException(style.name() + " is not a style");
         }
      }

      this.latest().styles.addAll(Arrays.asList(styles));
      this.dirty = true;
      return this;
   }

   public FancyMessage file(String path) {
      this.onClick("open_file", path);
      return this;
   }

   public FancyMessage link(String url) {
      this.onClick("open_url", url);
      return this;
   }

   public FancyMessage suggest(String command) {
      this.onClick("suggest_command", command);
      return this;
   }

   public FancyMessage insert(String command) {
      this.latest().insertionData = command;
      this.dirty = true;
      return this;
   }

   public FancyMessage command(String command) {
      this.onClick("run_command", command);
      return this;
   }

   public FancyMessage achievementTooltip(String name) {
      this.onHover("show_achievement", new JsonString("achievement." + name));
      return this;
   }

   public FancyMessage tooltip(String text) {
      this.onHover("show_text", new JsonString(text));
      return this;
   }

   public FancyMessage tooltip(Iterable<String> lines) {
      this.tooltip((String[]) ArrayWrapper.toArray(lines, String.class));
      return this;
   }

   public FancyMessage tooltip(String... lines) {
      StringBuilder builder = new StringBuilder();

      for (int i = 0; i < lines.length; ++i) {
         builder.append(lines[i]);
         if (i != lines.length - 1) {
            builder.append('\n');
         }
      }

      this.tooltip(builder.toString());
      return this;
   }

   public FancyMessage formattedTooltip(FancyMessage text) {
      for (MessagePart component : text.messageParts) {
         if (component.clickActionData != null && component.clickActionName != null) {
            throw new IllegalArgumentException("The tooltip text cannot have click data.");
         }
         if (component.hoverActionData != null && component.hoverActionName != null) {
            throw new IllegalArgumentException("The tooltip text cannot have a tooltip.");
         }
      }
      this.onHover("show_text", text);
      return this;
   }

   public FancyMessage formattedTooltip(FancyMessage... lines) {
      if (lines.length < 1) {
         this.onHover((String)null, (JsonRepresentedObject)null);
         return this;
      } else {
         FancyMessage result = new FancyMessage();
         result.messageParts.clear();

         for (int i = 0; i < lines.length; ++i) {
            try {
               for (MessagePart component : lines[i]) {
                  if (component.clickActionData != null && component.clickActionName != null) {
                     throw new IllegalArgumentException("The tooltip text cannot have click data.");
                  }

                  if (component.hoverActionData != null && component.hoverActionName != null) {
                     throw new IllegalArgumentException("The tooltip text cannot have a tooltip.");
                  }

                  if (component.hasText()) {
                     result.messageParts.add(component.clone());
                  }
               }

               if (i != lines.length - 1) {
                  result.messageParts.add(new MessagePart(TextualComponent.rawText("\n")));
               }
            } catch (CloneNotSupportedException var6) {
               Bukkit.getLogger().log(Level.WARNING, "Failed to clone object", var6);
               return this;
            }
         }

         return this.formattedTooltip(result.messageParts.isEmpty() ? null : result);
      }
   }

   public FancyMessage formattedTooltip(Iterable<FancyMessage> lines) {
      return this.formattedTooltip((FancyMessage[]) ArrayWrapper.toArray(lines, FancyMessage.class));
   }

   public FancyMessage translationReplacements(String... replacements) {
      for (String str : replacements) {
         this.latest().translationReplacements.add(new JsonString(str));
      }
      this.dirty = true;
      return this;
   }

   public FancyMessage translationReplacements(FancyMessage... replacements) {
      for (FancyMessage str : replacements) {
         this.latest().translationReplacements.add(str);
      }
      this.dirty = true;
      return this;
   }

   public FancyMessage translationReplacements(Iterable<FancyMessage> replacements) {
      return this.translationReplacements((FancyMessage[]) ArrayWrapper.toArray(replacements, FancyMessage.class));
   }

   public FancyMessage then(String text) {
      return this.then(TextualComponent.rawText(text));
   }

   public FancyMessage then(TextualComponent text) {
      if (!this.latest().hasText()) {
         throw new IllegalStateException("previous message part has no text");
      } else {
         this.messageParts.add(new MessagePart(text));
         this.dirty = true;
         return this;
      }
   }

   public FancyMessage then() {
      if (!this.latest().hasText()) {
         throw new IllegalStateException("previous message part has no text");
      } else {
         this.messageParts.add(new MessagePart());
         this.dirty = true;
         return this;
      }
   }

   public void writeJson(JsonWriter writer) throws IOException {
      if (this.messageParts.size() == 1) {
         this.latest().writeJson(writer);
      } else {
         writer.beginObject().name("text").value("").name("extra").beginArray();
         for (MessagePart part : this) {
            part.writeJson(writer);
         }
         writer.endArray().endObject();
      }
   }

   public String toJSONString() {
      if (!this.dirty && this.jsonString != null) {
         return this.jsonString;
      } else {
         StringWriter string = new StringWriter();
         JsonWriter json = new JsonWriter(string);

         try {
            this.writeJson(json);
            json.close();
         } catch (IOException var4) {
            throw new RuntimeException("invalid message");
         }

         this.jsonString = string.toString();
         this.dirty = false;
         return this.jsonString;
      }
   }

   public void send(Player player) {
      this.send(player, this.toJSONString());
   }

   private void send(CommandSender sender, String jsonString) {
      if (!(sender instanceof Player)) {
         sender.sendMessage(this.toOldMessageFormat());
      } else {
         Player player = (Player)sender;
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + jsonString);
      }
   }

   public void send(CommandSender sender) {
      this.send(sender, this.toJSONString());
   }

   public void send(Iterable<? extends CommandSender> senders) {
      String string = this.toJSONString();
      for (CommandSender sender : senders) {
         this.send(sender, string);
      }
   }

   public String toOldMessageFormat() {
      StringBuilder result = new StringBuilder();
      for (MessagePart part : this) {
         result.append(part.color == null ? "" : part.color);
         for (ChatColor formatSpecifier : part.styles) {
            result.append(formatSpecifier);
         }
         result.append(part.text);
      }
      return result.toString();
   }

   private MessagePart latest() {
      return this.messageParts.get(this.messageParts.size() - 1);
   }

   private void onClick(String name, String data) {
      MessagePart latest = this.latest();
      latest.clickActionName = name;
      latest.clickActionData = String.valueOf(new JsonString(data));
      this.dirty = true;
   }

   private void onHover(String name, JsonRepresentedObject data) {
      MessagePart latest = this.latest();
      latest.hoverActionName = name;
      latest.hoverActionData = data;
      this.dirty = true;
   }

   static {
      ConfigurationSerialization.registerClass(FancyMessage.class);
      _stringParser = new JsonParser();
   }

   public Map<String, Object> serialize() {
      Map<String, Object> map = new HashMap<>();
      map.put("messageParts", this.messageParts);
      return map;
   }

   public static FancyMessage deserialize(Map<String, Object> serialized) {
      FancyMessage msg = new FancyMessage();
      msg.messageParts = (List<MessagePart>)serialized.get("messageParts");
      return msg;
   }

   public Iterator<MessagePart> iterator() {
      return this.messageParts.iterator();
   }

   public static FancyMessage deserialize(String json) {
      FancyMessage returnVal = new FancyMessage();
      JsonObject serialized = _stringParser.parse(json).getAsJsonObject();
      JsonArray array = serialized.get("extra").getAsJsonArray();

      for (JsonElement element : array) {
         JsonObject object = element.getAsJsonObject();
         MessagePart component = new MessagePart();

         for (Entry<String, JsonElement> entry : object.entrySet()) {
            if (entry.getKey().equals("text")) {
               component.text = new TextualComponent() {
                  @Override
                  public String getKey() {
                     return "";
                  }

                  @Override
                  public String getReadableString() {
                     return "";
                  }

                  @Override
                  public TextualComponent clone() throws CloneNotSupportedException {
                     return null;
                  }

                  @Override
                  public void writeJson(JsonWriter writer) throws IOException {

                  }
               };
            } else if (entry.getKey().equals("color")) {
               component.color = ChatColor.valueOf(entry.getValue().getAsString().toUpperCase());
            } else if (entry.getKey().equals("bold")) {
               if (entry.getValue().getAsBoolean()) {
                  component.styles.add(ChatColor.BOLD);
               }
            } else if (entry.getKey().equals("italic")) {
               if (entry.getValue().getAsBoolean()) {
                  component.styles.add(ChatColor.ITALIC);
               }
            } else if (entry.getKey().equals("underlined")) {
               if (entry.getValue().getAsBoolean()) {
                  component.styles.add(ChatColor.UNDERLINE);
               }
            } else if (entry.getKey().equals("strikethrough")) {
               if (entry.getValue().getAsBoolean()) {
                  component.styles.add(ChatColor.STRIKETHROUGH);
               }
            } else if (entry.getKey().equals("obfuscated")) {
               if (entry.getValue().getAsBoolean()) {
                  component.styles.add(ChatColor.MAGIC);
               }
            } else if (entry.getKey().equals("clickEvent")) {
               JsonObject jsonObject = entry.getValue().getAsJsonObject();
               component.clickActionName = jsonObject.get("action").getAsString();
               component.clickActionData = String.valueOf(new JsonString(jsonObject.get("value").getAsString()));
            } else if (entry.getKey().equals("hoverEvent")) {
               JsonObject jsonObject = entry.getValue().getAsJsonObject();
               component.hoverActionName = jsonObject.get("action").getAsString();
               if (jsonObject.get("value").isJsonPrimitive()) {
                  component.hoverActionData = new JsonString(jsonObject.get("value").getAsString());
               } else {
                  component.hoverActionData = deserialize(jsonObject.get("value").toString());
               }
            } else if (entry.getKey().equals("with")) {
               Iterator<JsonElement> iterator = entry.getValue().getAsJsonArray().iterator();
               while (iterator.hasNext()) {
                  JsonElement jsonElement = iterator.next();
                  if (jsonElement.isJsonPrimitive()) {
                     component.translationReplacements.add(new JsonString(jsonElement.getAsString()));
                  } else {
                     component.translationReplacements.add(deserialize(jsonElement.toString()));
                  }
               }
            }
         }

         returnVal.messageParts.add(component);
      }

      return returnVal;
   }
}
