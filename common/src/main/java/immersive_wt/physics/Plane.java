package immersive_wt.physics;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import immersive_wt.ImmersiveWarThunder;
import immersive_wt.physics.modules.*;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Plane {
    public Vec3 position = Vec3.ZERO;
    public Vec3 velocity = Vec3.ZERO;
    public double xRot, yRot, zRot;
    public Vec3 forwardDirection;
    public Vec3 topDirection;
    public Vec3 force = Vec3.ZERO;
    public Torque torque = Torque.ZERO;
    private int onGround;

    public Rot tail;
    public Rot aileron;
    public Rot fineTurningX;
    public Rot fineTurningY;
    public Wheel wheel;
    public Gravity gravity;
    public Thrust engine;
    public Thrust booster;
    public Wing wing;
    public Module[] modules;

    public Plane(@NotNull JsonObject config) {
        try {
            tail = new Rot.Pitch(this, config.getAsJsonObject("tail"));
            aileron = new Rot.ZRot(this, config.getAsJsonObject("aileron"));
            fineTurningX = new Rot.XRot(this, config.getAsJsonObject("fineTurningX"));
            fineTurningY = new Rot.YRot(this, config.getAsJsonObject("fineTurningY"));
            wheel = new Wheel(this, config.getAsJsonObject("wheel"));
            gravity = new Gravity(this, config.getAsJsonObject("gravity"));
            engine = new Thrust(this, config.getAsJsonObject("engine"));
            booster = new Thrust(this, config.getAsJsonObject("booster"));
            wing = new Wing(this, config.getAsJsonObject("wing"));
        } catch (JsonSyntaxException e) {
            ImmersiveWarThunder.logger.warning("Fail to construct plane");
            ImmersiveWarThunder.logger.warning(e.getMessage());
        }
        modules = new Module[]{tail, aileron, fineTurningX, fineTurningY, wheel, gravity, engine, booster, wing};
    }

    public void update() {
        force = Vec3.ZERO;
        torque = Torque.ZERO;
        for (Module module : modules) {
            module.update();
            force = force.add(module.getForce());
            torque = torque.add(module.getTorque());
        }
    }

    public Vec3 getForwardDirection() {
        return new Vec3(forwardDirection.x(), forwardDirection.y(), forwardDirection.z());
    }

    public Vec3 getTopDirection() {
        return new Vec3(topDirection.x(), topDirection.y(), topDirection.z());
    }

    public Vec3 getVelocity() {
        return new Vec3(velocity.x(), velocity.y(), velocity.z());
    }

    public Vec3 getPosition() {
        return new Vec3(position.x(), position.y(), position.z());
    }

    public boolean isOnGround() {
        return onGround > 0;
    }

    public void setOnGround(boolean onGround) {
        if (onGround) {
            this.onGround = 5;
        } else {
            this.onGround--;
        }
    }
}
