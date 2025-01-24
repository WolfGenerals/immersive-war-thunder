package immersive_wt.engine.plane;

import immersive_wt.engine.Plane;
import immersive_wt.engine.Torque;
import immersive_wt.engine.TorqueModule;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.pow;

public class RollToYaw implements TorqueModule {
    @Override
    public @NotNull Torque torque(@NotNull Plane plane) {
        double conversionSpeed = 0.1 * pow(plane.pitch / 90, 4) * plane.roll;

        return new Torque(
                0,
                conversionSpeed*  (plane.pitch > 0 ? 1 : -1),
                -conversionSpeed
        );
    }
}
