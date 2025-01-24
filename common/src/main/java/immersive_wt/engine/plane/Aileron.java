package immersive_wt.engine.plane;

import immersive_wt.engine.Plane;
import immersive_wt.engine.Torque;
import immersive_wt.engine.TorqueModule;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.*;

public class Aileron implements TorqueModule {
    protected double efficiency;
    protected double control;

    public Aileron(double efficiency) {
        this.efficiency = efficiency;
    }

    public void setControl(double control) {
        this.control = Math.max(-1, Math.min(1, control));
    }

    public @NotNull Torque torque(@NotNull Plane plane) {
        return new Torque(
                0, 0,
                -sqrt(plane.getVelocity().length()) * efficiency * control
        );
    }
}
