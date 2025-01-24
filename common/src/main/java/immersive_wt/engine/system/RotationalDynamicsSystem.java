package immersive_wt.engine.system;

import immersive_wt.engine.Systems;
import org.jetbrains.annotations.NotNull;

public class RotationalDynamicsSystem implements Systems<Plane> {
    @Override
    public void progress(@NotNull Plane plane) {
        double r = Math.toRadians(plane.roll);
        double p = Math.toRadians(plane.pitch);
        double y = Math.toRadians(plane.yaw);
        plane.xRot = plane.yRot = plane.zRot = 0;

        plane.aerodynamicEfficiency = Math.sqrt(plane.velocity.length() / plane.MAX_SPEED);
        double xRotAbility = Math.abs(
                plane.TAIL_TORQUE * plane.aerodynamicEfficiency
                        * Math.cos(r)
        );
        double yRotAbility = Math.abs(
                plane.TAIL_TORQUE * plane.aerodynamicEfficiency
                        * Math.sin(r)
        );
        double zRotAbility = Math.abs(
                plane.AILERON_TORQUE * plane.aerodynamicEfficiency
        );

        plane.xRot += Math.max(-xRotAbility, Math.min(xRotAbility, plane.xRotTarget));
        plane.yRot += Math.max(-yRotAbility, Math.min(yRotAbility, plane.yRotTarget));
        plane.zRot += Math.max(-zRotAbility, Math.min(zRotAbility, plane.zRotTarget));


        plane.xRot += plane.GRAVITY_TORQUE * Math.cos(p);


        if (plane.onGround) {
            plane.xRot -= plane.pitch * 0.05;
            plane.zRot -= plane.roll * 0.05;
        }

        plane.zRot -= plane.roll*Math.sin(p)*Math.sin(p)*Math.sin(p)*Math.sin(p)*0.1;
        plane.yRot += plane.roll*Math.sin(p)*Math.sin(p)*Math.sin(p)*Math.sin(p)*0.1*(p>0?1:-1);

    }
}
