package lol.yakut.clubs.util.fanciful.mkremins.fanciful;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.stream.JsonWriter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class TextualComponent implements Cloneable {

   public TextualComponent() {
   }

   @Override
   public String toString() {
      return this.getReadableString();
   }

   public abstract String getKey();
   public abstract String getReadableString();
   public abstract TextualComponent clone() throws CloneNotSupportedException;
   public abstract void writeJson(JsonWriter writer) throws IOException;

   static TextualComponent deserialize(Map<String, Object> map) {
      if (map.containsKey("key") && map.size() == 2 && map.containsKey("value")) {
         return ArbitraryTextTypeComponent.deserialize(map);
      } else if (map.size() >= 2 && map.containsKey("key") && !map.containsKey("value")) {
         return ComplexTextTypeComponent.deserialize(map);
      }
      return null;
   }

   static boolean isTextKey(String key) {
      return key.equals("translate") || key.equals("text") || key.equals("score") || key.equals("selector");
   }

   static boolean isTranslatableText(TextualComponent component) {
      return component instanceof ComplexTextTypeComponent && ((ComplexTextTypeComponent) component).getKey().equals("translate");
   }

   public static TextualComponent rawText(String textValue) {
      return new ArbitraryTextTypeComponent("text", textValue);
   }

   public static TextualComponent localizedText(String translateKey) {
      return new ArbitraryTextTypeComponent("translate", translateKey);
   }

   private static void throwUnsupportedSnapshot() {
      throw new UnsupportedOperationException("This feature is only supported in snapshot releases.");
   }

   public static TextualComponent objectiveScore(String scoreboardObjective) {
      return objectiveScore("*", scoreboardObjective);
   }

   public static TextualComponent objectiveScore(String playerName, String scoreboardObjective) {
      throwUnsupportedSnapshot();
      Map<String, String> map = new HashMap<>();
      map.put("name", playerName);
      map.put("objective", scoreboardObjective);
      return new ComplexTextTypeComponent("score", map);
   }

   public static TextualComponent selector(String selector) {
      throwUnsupportedSnapshot();
      return new ArbitraryTextTypeComponent("selector", selector);
   }

   static {
      ConfigurationSerialization.registerClass(ArbitraryTextTypeComponent.class);
      ConfigurationSerialization.registerClass(ComplexTextTypeComponent.class);
   }

   private static final class ComplexTextTypeComponent extends TextualComponent implements ConfigurationSerializable {
      private String key;
      private ImmutableMap<String, String> value;

      public ComplexTextTypeComponent(String key, Map<String, String> values) {
          super();
          this.setKey(key);
         this.setValue(values);
      }

      @Override
      public String getKey() {
         return this.key;
      }

      public void setKey(String key) {
         Preconditions.checkArgument(key != null && !key.isEmpty(), "The key must be specified.");
         this.key = key;
      }

      public ImmutableMap<String, String> getValue() {
         return this.value;
      }

      public void setValue(Map<String, String> value) {
         Preconditions.checkArgument(value != null, "The value must be specified.");
         this.value = ImmutableMap.copyOf(value);
      }

      @Override
      public TextualComponent clone() throws CloneNotSupportedException {
         return new ComplexTextTypeComponent(this.getKey(), this.getValue());
      }

      @Override
      public void writeJson(JsonWriter writer) throws IOException {
         writer.name(this.getKey());
         writer.beginObject();
         for (Entry<String, String> entry : this.value.entrySet()) {
            writer.name(entry.getKey()).value(entry.getValue());
         }
         writer.endObject();
      }

      @Override
      public Map<String, Object> serialize() {
         Map<String, Object> map = new HashMap<>();
         map.put("key", this.getKey());
         for (Entry<String, String> entry : this.getValue().entrySet()) {
            map.put("value." + entry.getKey(), entry.getValue());
         }
         return map;
      }

      public static ComplexTextTypeComponent deserialize(Map<String, Object> map) {
         String key = null;
         Map<String, String> value = new HashMap<>();
         for (Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals("key")) {
               key = (String) entry.getValue();
            } else if (entry.getKey().startsWith("value.")) {
               value.put(entry.getKey().substring(6), entry.getValue().toString());
            }
         }
         return new ComplexTextTypeComponent(key, value);
      }

      @Override
      public String getReadableString() {
         return this.getKey();
      }
   }

   private static final class ArbitraryTextTypeComponent extends TextualComponent implements ConfigurationSerializable {
      private String key;
      private String value;

      public ArbitraryTextTypeComponent(String key, String value) {
         this.setKey(key);
         this.setValue(value);
      }

      @Override
      public String getKey() {
         return this.key;
      }

      public void setKey(String key) {
         Preconditions.checkArgument(key != null && !key.isEmpty(), "The key must be specified.");
         this.key = key;
      }

      public String getValue() {
         return this.value;
      }

      public void setValue(String value) {
         Preconditions.checkArgument(value != null, "The value must be specified.");
         this.value = value;
      }

      @Override
      public TextualComponent clone() throws CloneNotSupportedException {
         return new ArbitraryTextTypeComponent(this.getKey(), this.getValue());
      }

      @Override
      public void writeJson(JsonWriter writer) throws IOException {
         writer.name(this.getKey()).value(this.getValue());
      }

      @Override
      public Map<String, Object> serialize() {
         Map<String, Object> map = new HashMap<>();
         map.put("key", this.getKey());
         map.put("value", this.getValue());
         return map;
      }

      public static ArbitraryTextTypeComponent deserialize(Map<String, Object> map) {
         return new ArbitraryTextTypeComponent(map.get("key").toString(), map.get("value").toString());
      }

      @Override
      public String getReadableString() {
         return this.getValue();
      }
   }
}
