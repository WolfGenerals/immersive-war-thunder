package immersive_wt.engine.plane;

import immersive_wt.engine.ForceModule;
import immersive_wt.engine.Plane;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Wing implements ForceModule {
    protected double forwardDrag;
    protected double sideDrag;

    public Wing(double forwardDrag, double sideDrag) {
        this.forwardDrag = forwardDrag;
        this.sideDrag = sideDrag;
    }
    @Override
    public @NotNull Vec3 force(@NotNull Plane plane) {
        double forwardVelocity = plane.getVelocity().dot(plane.getForwardDirection());
        if (forwardVelocity < 0) {
            forwardVelocity = 0;
        }
        Vec3 drag = plane.getForwardDirection().scale(forwardVelocity)
                .scale(-forwardDrag);
        Vec3 otherDrag = plane.getVelocity()
                .subtract(plane.getForwardDirection().scale(forwardVelocity))
                .scale(-sideDrag);
        return drag.add(otherDrag);
    }
}
