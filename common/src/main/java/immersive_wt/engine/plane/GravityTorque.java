package immersive_wt.engine.plane;

import immersive_wt.engine.ForceModule;
import immersive_wt.engine.Plane;
import immersive_wt.engine.Torque;
import immersive_wt.engine.TorqueModule;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.*;

public class GravityTorque implements TorqueModule {

    protected double torque;

    public GravityTorque(double torque) {
        this.torque = torque;
    }

    @Override
    public @NotNull Torque torque(@NotNull Plane plane) {
        if (plane.onGround) return new Torque(0, 0, 0);
        return new Torque(torque *cos(toRadians(plane.pitch)), 0, 0);
    }
}
