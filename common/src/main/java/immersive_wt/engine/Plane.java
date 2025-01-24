package immersive_wt.engine;

import net.minecraft.world.phys.Vec3;

@SuppressWarnings("unused")
public class Plane {
    public Vec3 position = Vec3.ZERO;
    public Vec3 velocity = Vec3.ZERO;
    public double roll, pitch, yaw;
    public boolean onGround;
    public Vec3 forwardDirection;
    public Vec3 topDirection;


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
}
