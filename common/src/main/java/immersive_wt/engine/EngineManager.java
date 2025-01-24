package immersive_wt.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EngineManager {
    private static final Map<UUID,PlanePhysicsEngine> planeMap = new HashMap<>();

    public static PlanePhysicsEngine getPlane(UUID uuid) {
        if (!planeMap.containsKey(uuid)) {
            planeMap.put(uuid, new PlanePhysicsEngine());
        }
        return planeMap.get(uuid);
    }
}
