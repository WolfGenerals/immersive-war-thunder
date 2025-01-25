package immersive_wt.engine.plane;

import immersive_wt.engine.ForceModule;
import immersive_wt.engine.Plane;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Wheel implements ForceModule {
    protected double fraction;

    public Wheel(double fraction) {
        this.fraction = fraction;
    }

    @Override
    public @NotNull Vec3 force(@NotNull Plane plane) {
        if (!plane.onGround) return Vec3.ZERO;
        Vec3 horizontalVelocity = plane.getVelocity()
                .subtract(0, plane.getVelocity().y, 0);
        return horizontalVelocity.scale(-fraction);
    }
}
