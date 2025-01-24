package immersive_wt.engine.system;

import immersive_wt.engine.Systems;
import org.jetbrains.annotations.NotNull;

public class ForwardSystem implements Systems<Plane> {
    public void progress(@NotNull Plane plane) {
        plane.velocity = plane.forwardDirection.normalize().scale(plane.enginePower);
    }
}
