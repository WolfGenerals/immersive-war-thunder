package immersive_wt.physics.modules;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import immersive_wt.ImmersiveWarThunder;
import immersive_wt.physics.Module;
import immersive_wt.physics.Plane;
import immersive_wt.physics.Torque;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.min;

public class Wheel extends Module {
    protected double friction = 0;
    protected double turning = 0;
    protected double xRot = 0;

    protected boolean brake = false;
    protected double truingControl = 0;

    public Wheel(@NotNull Plane plane, @NotNull JsonObject config) {
        super(plane, config);
        try {
            friction = config.get("friction").getAsDouble();
            turning = config.get("turning").getAsDouble();
            xRot = config.get("xRot").getAsDouble();
        } catch (JsonSyntaxException e) {
            ImmersiveWarThunder.logger.warning("Wheel config error:" + config);
        }
    }

    @Override
    protected void updateTorque() {
        if (!plane.isOnGround()) {return;}
        torque = new Torque(xRot - plane.xRot, turning*truingControl, -plane.zRot).multiply(0.5);
    }

    @Override
    protected void updateForce() {
        if (!plane.isOnGround()) {return;}
        if (brake) {
            force = plane.getVelocity().normalize().scale(-min(friction, plane.getVelocity().length()));
        }
    }

    public void setBrake(boolean brake) {
        this.brake = brake;
    }

    public void setTruingControl(double truingControl) {
        truingControl = Mth.clamp(truingControl, -1, 1);
        this.truingControl = truingControl;
    }
}
