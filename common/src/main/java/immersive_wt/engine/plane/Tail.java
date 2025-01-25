package immersive_wt.engine.plane;

import immersive_wt.engine.Plane;
import immersive_wt.engine.Torque;
import immersive_wt.engine.TorqueModule;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.*;

public class Tail implements TorqueModule {
    protected double efficiency;
    protected double control;

    public Tail(double efficiency) {
        this.efficiency = efficiency;
    }

    // 拉杆为正
    public void setControl(double control) {
        this.control = Mth.clamp(control, -0.2, 1)* 0.2 + this.control * 0.8;
    }

    public @NotNull Torque torque(@NotNull Plane plane) {
        double r = Math.toRadians(plane.roll);

        return new Torque(
                -sqrt(plane.getVelocity().length()) * efficiency * control * cos(r),
                sqrt(plane.getVelocity().length()) * efficiency * control * sin(r),
                0
        );
    }
}
