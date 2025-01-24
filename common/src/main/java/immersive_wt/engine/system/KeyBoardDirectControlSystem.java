package immersive_wt.engine.system;

import immersive_wt.engine.Systems;
import org.jetbrains.annotations.NotNull;

public class KeyBoardDirectControlSystem implements Systems<Plane> {
    private double rollInput, pitchInput;

    public void input(double rollInput, double pitchInput){
//        设置死区
        if (Math.abs(rollInput)<0.01) rollInput = 0;
        if (Math.abs(pitchInput)<0.01) pitchInput = 0;

        this.rollInput = rollInput;
        this.pitchInput = pitchInput;
    }

    @Override
    public void progress(@NotNull Plane plane) {
        double r = Math.toRadians(plane.roll);
        double p = Math.toRadians(plane.pitch);
        double y = Math.toRadians(plane.yaw);

        if (pitchInput!=0 || rollInput!=0){
            plane.xRotTarget = plane.yRotTarget = plane.zRotTarget = 0;

            plane.xRotTarget += plane.TAIL_TORQUE*pitchInput*Math.cos(r);
            plane.yRotTarget -= plane.TAIL_TORQUE*pitchInput*Math.sin(r);
//            plane.zRotTarget -= plane.TAIL_TORQUE*pitchInput*Math.sin(r)*Math.sin(p*2);
            plane.zRotTarget -= plane.AILERON_TORQUE*rollInput;
        }
    }
}
