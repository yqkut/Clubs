package lol.yakut.clubs.util.fanciful.mkremins.fanciful;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableBiMap.Builder;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

final class MessagePart implements JsonRepresentedObject, ConfigurationSerializable, Cloneable {
   ChatColor color;
   ArrayList<ChatColor> styles;
   String clickActionName;
   String clickActionData;
   String hoverActionName;
   JsonRepresentedObject hoverActionData;
   TextualComponent text;
   String insertionData;
   ArrayList<JsonRepresentedObject> translationReplacements;
   static final BiMap<ChatColor, String> stylesToNames;

   MessagePart(TextualComponent text) {
      this.color = ChatColor.WHITE;
      this.styles = new ArrayList();
      this.clickActionName = null;
      this.clickActionData = null;
      this.hoverActionName = null;
      this.hoverActionData = null;
      this.text = null;
      this.insertionData = null;
      this.translationReplacements = new ArrayList();
      this.text = text;
   }

   MessagePart() {
      this.color = ChatColor.WHITE;
      this.styles = new ArrayList();
      this.clickActionName = null;
      this.clickActionData = null;
      this.hoverActionName = null;
      this.hoverActionData = null;
      this.text = null;
      this.insertionData = null;
      this.translationReplacements = new ArrayList();
      this.text = null;
   }

   boolean hasText() {
      return this.text != null;
   }

   public MessagePart clone() throws CloneNotSupportedException {
      MessagePart obj = (MessagePart)super.clone();
      obj.styles = (ArrayList)this.styles.clone();
      if (this.hoverActionData instanceof JsonString) {
         obj.hoverActionData = new JsonString(((JsonString)this.hoverActionData).getValue());
      } else if (this.hoverActionData instanceof FancyMessage) {
         obj.hoverActionData = ((FancyMessage)this.hoverActionData).clone();
      }

      obj.translationReplacements = (ArrayList)this.translationReplacements.clone();
      return obj;
   }

   public void writeJson(JsonWriter json) {
      try {
         json.beginObject();
         this.text.writeJson(json);
         json.name("color").value(this.color.name().toLowerCase());
         Iterator var2 = this.styles.iterator();

         while(var2.hasNext()) {
            ChatColor style = (ChatColor)var2.next();
            json.name((String)stylesToNames.get(style)).value(true);
         }

         if (this.clickActionName != null && this.clickActionData != null) {
            json.name("clickEvent").beginObject().name("action").value(this.clickActionName).name("value").value(this.clickActionData).endObject();
         }

         if (this.hoverActionName != null && this.hoverActionData != null) {
            json.name("hoverEvent").beginObject().name("action").value(this.hoverActionName).name("value");
            this.hoverActionData.writeJson(json);
            json.endObject();
         }

         if (this.insertionData != null) {
            json.name("insertion").value(this.insertionData);
         }

         if (this.translationReplacements.size() > 0 && this.text != null && TextualComponent.isTranslatableText(this.text)) {
            json.name("with").beginArray();
            var2 = this.translationReplacements.iterator();

            while(var2.hasNext()) {
               JsonRepresentedObject obj = (JsonRepresentedObject)var2.next();
               obj.writeJson(json);
            }

            json.endArray();
         }

         json.endObject();
      } catch (IOException var4) {
         Bukkit.getLogger().log(Level.WARNING, "A problem occured during writing of JSON string", var4);
      }

   }

   public Map<String, Object> serialize() {
      HashMap<String, Object> map = new HashMap();
      map.put("text", this.text);
      map.put("styles", this.styles);
      map.put("color", this.color.getChar());
      map.put("hoverActionName", this.hoverActionName);
      map.put("hoverActionData", this.hoverActionData);
      map.put("clickActionName", this.clickActionName);
      map.put("clickActionData", this.clickActionData);
      map.put("insertion", this.insertionData);
      map.put("translationReplacements", this.translationReplacements);
      return map;
   }

   public static MessagePart deserialize(Map<String, Object> serialized) {
      MessagePart part = new MessagePart((TextualComponent)serialized.get("text"));
      part.styles = (ArrayList)serialized.get("styles");
      part.color = ChatColor.getByChar(serialized.get("color").toString());
      part.hoverActionName = (String)serialized.get("hoverActionName");
      part.hoverActionData = (JsonRepresentedObject)serialized.get("hoverActionData");
      part.clickActionName = (String)serialized.get("clickActionName");
      part.clickActionData = (String)serialized.get("clickActionData");
      part.insertionData = (String)serialized.get("insertion");
      part.translationReplacements = (ArrayList)serialized.get("translationReplacements");
      return part;
   }

   static {
      Builder<ChatColor, String> builder = ImmutableBiMap.builder();
      ChatColor[] var1 = ChatColor.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ChatColor style = var1[var3];
         if (style.isFormat()) {
            String styleName;
            switch(style) {
            case MAGIC:
               styleName = "obfuscated";
               break;
            case UNDERLINE:
               styleName = "underlined";
               break;
            default:
               styleName = style.name().toLowerCase();
            }

            builder.put(style, styleName);
         }
      }

      stylesToNames = builder.build();
      ConfigurationSerialization.registerClass(MessagePart.class);
   }
}
