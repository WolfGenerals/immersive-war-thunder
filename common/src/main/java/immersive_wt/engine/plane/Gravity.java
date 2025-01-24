package immersive_wt.engine.plane;

import immersive_wt.engine.ForceModule;
import immersive_wt.engine.Plane;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Gravity implements ForceModule {
    @Override
    public @NotNull Vec3 force(@NotNull Plane plane) {
        if (plane.onGround)
            return Vec3.ZERO;
        return new Vec3(0, -0.02, 0);
    }
}
