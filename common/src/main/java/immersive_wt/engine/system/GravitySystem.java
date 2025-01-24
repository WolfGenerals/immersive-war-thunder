package immersive_wt.engine.system;

import immersive_wt.engine.Systems;

public class GravitySystem implements Systems<Plane> {
    @Override
    public void progress(Plane plane) {
        double p = Math.toRadians(plane.pitch);

        if (plane.onGround && plane.velocity.y < 0.5) {
            plane.xRot -= plane.pitch * 0.1;
            plane.zRot -= plane.roll * 0.1;
            plane.velocity = plane.velocity.add(0, -plane.velocity.y, 0);
            return;
        }
//        plane.xRot += plane.GRAVITY_TORQUE * Math.cos(p);
        plane.velocity = plane.velocity.add(0, -plane.GRAVITY, 0);
    }
}
