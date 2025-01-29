package immersive_wt.physics.modules;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import immersive_wt.ImmersiveWarThunder;
import immersive_wt.physics.Module;
import immersive_wt.physics.Plane;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Wing extends Module {
    protected double frictionForward;
    protected double frictionOtherSide;

    public Wing(@NotNull Plane plane, @NotNull JsonObject config) {
        super(plane, config);
        try {
            frictionForward = config.get("frictionForward").getAsDouble();
            frictionOtherSide = config.get("frictionOtherSide").getAsDouble();
        } catch (JsonSyntaxException e) {
            ImmersiveWarThunder.logger.warning("Wing config error:" + config);
        }
    }

    @Override
    protected void updateForce() {
        double forwardVelocity = plane.getVelocity().dot(plane.getForwardDirection());
        forwardVelocity = forwardVelocity < 0 ? 0 : forwardVelocity;
        Vec3 drag = plane.getForwardDirection().scale(forwardVelocity)
                .scale(-frictionForward);
        Vec3 otherDrag = plane.getVelocity()
                .subtract(plane.getForwardDirection().scale(forwardVelocity))
                .scale(-frictionOtherSide);
        force = drag.add(otherDrag);
    }
}
