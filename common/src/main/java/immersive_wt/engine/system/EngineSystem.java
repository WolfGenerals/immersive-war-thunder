package immersive_wt.engine.system;

import immersive_wt.engine.Systems;
import net.minecraft.world.phys.Vec3;

public class EngineSystem implements Systems<Plane> {
    @Override
    public void progress(Plane plane) {
        double speedForward = plane.velocity.dot(plane.forwardDirection);
        double efficacy = plane.MAX_SPEED*plane.enginePower - speedForward;
        if (efficacy < 0) efficacy = 0;
        if (efficacy > 1) efficacy = 1;

        Vec3 thrust = plane.forwardDirection
                .scale(efficacy)
                .scale(0.3)
                .scale(plane.ACCELERATION);

        plane.velocity = plane.velocity.add(thrust);
    }
}
