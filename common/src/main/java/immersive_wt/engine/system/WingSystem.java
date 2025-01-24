package immersive_wt.engine.system;

import immersive_wt.engine.Systems;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class WingSystem implements Systems<Plane> {
    public void progress(@NotNull Plane plane) {
        double attackAngle = Math.acos(plane.velocity.dot(plane.forwardDirection)/plane.velocity.length());
        Vec3 drag = plane.velocity
                .scale(-plane.DRAG).scale(Math.sin(attackAngle)*5+1);
        Vec3 lift = plane.topDirection
                .scale(drag.length())
                .scale(5);

        plane.velocity = plane.velocity.add(drag).add(lift);
    }
}
