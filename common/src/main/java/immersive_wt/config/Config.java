package immersive_wt.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import immersive_wt.ImmersiveWarThunder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Path CONFIG_PATH = Path.of("config", ImmersiveWarThunder.MOD_ID + ".json");
    private static final URL DEFAULT_CONFIG = Config.class.getResource("/assets/immersive_wt/config.json");
    public static @NotNull JsonObject config = new JsonObject();

    public static void load() {
        if (CONFIG_PATH.toFile().exists()) {
            try {
                String json = Files.readString(CONFIG_PATH);
                config = GSON.fromJson(json, JsonObject.class);
            } catch (Exception e) {
                ImmersiveWarThunder.logger.warning("fail to load config");
                ImmersiveWarThunder.logger.warning(e.getMessage());
            }
        } else {
            //write
            try {
                Files.writeString(CONFIG_PATH, GSON.toJson(config));
            } catch (IOException e) {
                ImmersiveWarThunder.logger.warning("fail to write config");
                ImmersiveWarThunder.logger.warning(e.getMessage());
            }
        }
    }

    public static void init() {
        if (DEFAULT_CONFIG == null) {
            ImmersiveWarThunder.logger.warning("fail found default config");
            return;
        }

        String json;
        try (InputStream inputStream = DEFAULT_CONFIG.openStream()) {
            json = new String(inputStream.readAllBytes());
        } catch (IOException e) {
            ImmersiveWarThunder.logger.warning("Failed to load default config");
            ImmersiveWarThunder.logger.warning(e.getMessage());
            return;
        }
        config = GSON.fromJson(json, JsonObject.class);
    }

    public static JsonObject getPlaneConfig(String name, String type) {
        JsonObject planes = config.getAsJsonObject("planes");
        if (planes.has(name)) {
            return planes.getAsJsonObject(name);
        }
        if (planes.has(type)) {
            return planes.getAsJsonObject(type);
        }
        ImmersiveWarThunder.logger.info("fail to find plane config for: (" + name + ", " + type + "), using default " +
                                        "config");

        return planes.getAsJsonObject("default");
    }

    public static boolean hasPlaneConfig(String id) {
        JsonObject planes = config.getAsJsonObject("planes");
        return planes.has(id);
    }

}
