package immersive_wt.engine;

import org.jetbrains.annotations.NotNull;

public interface TorqueModule {
    @NotNull Torque torque(@NotNull Plane plane);
}
