package immersive_wt.physics;

import immersive_wt.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlaneManager {
    public static Map<UUID, Plane> planes = new HashMap<>();

    public static Plane getPlane(@NotNull UUID uuid) {
        return planes.get(uuid);
    }

    public static boolean hasPlane(@NotNull UUID uuid) {
        return planes.containsKey(uuid);
    }

    public static void createPlane(@NotNull UUID uuid, @Nullable String name,@NotNull String type) {
//        if (name != null && Config.hasPlaneConfig(name)) {
//            planes.put(uuid, new Plane(Config.getPlaneConfig(name)));
//        }
        planes.put(uuid, new Plane(Config.getPlaneConfig(name,type)));
    }
}
