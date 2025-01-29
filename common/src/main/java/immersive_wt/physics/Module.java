package immersive_wt.physics;

import com.google.gson.JsonObject;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class Module {
    protected @NotNull Vec3 force = Vec3.ZERO;
    protected @NotNull Torque torque = Torque.ZERO;
    protected @NotNull Plane plane;


    public Module(@NotNull Plane plane,@NotNull JsonObject config) {
        this.plane = plane;
    }

    protected void updateForce() {
    }

    protected void updateTorque() {
    }

    public final void update() {
        reset();
        updateForce();
        updateTorque();
    }

    private void reset() {
        force = Vec3.ZERO;
        torque = Torque.ZERO;
    }

    public Torque getTorque() {
        //check finite
        if (!Double.isFinite(torque.x()) || !Double.isFinite(torque.y()) || !Double.isFinite(torque.z())) {
            return Torque.ZERO;
        }
        return torque;
    }

    public Vec3 getForce() {
        return force;
    }

}
