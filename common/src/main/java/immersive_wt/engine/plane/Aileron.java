package immersive_wt.engine.plane;

import immersive_wt.engine.Plane;
import immersive_wt.engine.Torque;
import immersive_wt.engine.TorqueModule;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.*;

public class Aileron implements TorqueModule {
    protected double efficiency;
    protected double control;

    public Aileron(double efficiency) {
        this.efficiency = efficiency;
    }

    // 向右滚转为正
    public void setControl(double control) {
        this.control = Mth.clamp(control, -1, 1)* 0.2 + this.control * 0.8;
    }

    public @NotNull Torque torque(@NotNull Plane plane) {
        return new Torque(
                0, 0,
                sqrt(plane.getVelocity().length()) * efficiency * control
        );
    }
}
