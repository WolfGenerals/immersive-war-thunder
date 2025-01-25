package immersive_wt.engine.plane;

import immersive_wt.engine.Plane;
import immersive_wt.engine.Torque;
import immersive_wt.engine.TorqueModule;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.sqrt;

public class FineTuningTorque implements TorqueModule {
    protected double x, y;
    protected double efficiency;

    public FineTuningTorque(double efficiency) {
        this.efficiency = efficiency;
    }

    public void setControl(double x, double y) {
        this.x = Mth.clamp(x, -1, 1);
        this.y = Mth.clamp(y, -1, 1);
    }

    @Override
    public @NotNull Torque torque(@NotNull Plane plane) {
        return new Torque(
                sqrt(plane.getVelocity().length()) * efficiency * x,
                sqrt(plane.getVelocity().length()) * efficiency * y,
                0
        );
    }
}
