package immersive_wt.engine;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public interface ForceModule {
    @NotNull Vec3 force(@NotNull Plane plane);
}
