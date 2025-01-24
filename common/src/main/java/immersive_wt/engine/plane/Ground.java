package immersive_wt.engine.plane;

import immersive_wt.engine.Plane;
import immersive_wt.engine.Torque;
import immersive_wt.engine.TorqueModule;
import org.jetbrains.annotations.NotNull;

public class Ground implements TorqueModule {
    @Override
    public @NotNull Torque torque(@NotNull Plane plane) {
        if (!plane.onGround) return new Torque(0, 0, 0);

        return new Torque(
                -plane.pitch*0.1,
                0,
                -plane.roll*0.1
        );
    }
}
