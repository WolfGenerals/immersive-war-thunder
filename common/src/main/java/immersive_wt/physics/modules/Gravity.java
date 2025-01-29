package immersive_wt.physics.modules;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import immersive_wt.ImmersiveWarThunder;
import immersive_wt.physics.Module;
import immersive_wt.physics.Plane;
import immersive_wt.physics.Torque;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.cos;
import static java.lang.Math.toRadians;

public class Gravity extends Module {
    public static final double DEFAULT_GRAVITY = 0.02;
    protected double gravity = DEFAULT_GRAVITY;
    protected double gravityTorque = 0;

    public Gravity(@NotNull Plane plane, @NotNull JsonObject config) {
        super(plane, config);
        try {
            gravity = config.get("gravity").getAsDouble();
            gravityTorque = config.get("gravityTorque").getAsDouble();
        }catch (JsonSyntaxException e){
            ImmersiveWarThunder.logger.warning("Gravity config error:" + config);
        }
    }

    @Override
    protected void updateTorque() {
        torque = new Torque(gravityTorque * cos(toRadians(plane.xRot)), 0, 0);
    }

    @Override
    protected void updateForce() {
        force = new Vec3(0, -gravity, 0);
    }
}
