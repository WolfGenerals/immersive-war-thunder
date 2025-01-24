package immersive_wt.engine.system;

import net.minecraft.world.phys.Vec3;

@SuppressWarnings("unused")
public class Plane {
    /** 重力加速度 m/gt^2 */
    public double GRAVITY = 9.8/20;
    /** 最大平飞速度 m/gt */
    public double MAX_SPEED = 1;
    /** 最小平飞速度 m/gt */
    public double MIN_SPEED = MAX_SPEED*0.33;
    public double TAIL_TORQUE = 5;
    public double AILERON_TORQUE = 7;
    public double GRAVITY_TORQUE = 0.5;



    public Vec3 position = Vec3.ZERO;
    public Vec3 velocity = Vec3.ZERO;
    public Vec3 direction = Vec3.ZERO;
    public double roll, pitch, yaw;
    public double xRot, yRot, zRot;
    public double enginePower;
    public boolean onGround;

    public double aerodynamicEfficiency;
    public double xRotTarget, yRotTarget, zRotTarget;

    public void resetControl() {
        xRot = yRot = zRot = 0;
        xRotTarget = yRotTarget = zRotTarget = 0;
    }
}
