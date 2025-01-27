package immersive_wt.engine.plane;

import immersive_wt.engine.ForceModule;
import immersive_wt.engine.Plane;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Engine implements ForceModule {
    protected double thrust;
    /** 引擎的功率 0-1 */
    protected double power;

    public Engine(double thrust) {
        this.thrust = thrust;
    }

    public void setPower(double power) {
//        this.power = Math.min(1, Math.max(0, power));
        this.power = Math.max(0, power);
    }
    @SuppressWarnings("unused")
    public double getPower() {
        return power;
    }

    public @NotNull Vec3 force(@NotNull Plane plane){
        Vec3 forwardDirection = plane.getForwardDirection();
        return forwardDirection.scale(thrust * power);
    }
}
