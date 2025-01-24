package immersive_wt.engine;

public class Torque {
    public double xRot, yRot, zRot;

    public Torque(double xRot, double yRot, double zRot) {
        this.xRot = xRot;
        this.yRot = yRot;
        this.zRot = zRot;
    }
    public Torque add(Torque torque) {
        return new Torque(xRot + torque.xRot, yRot + torque.yRot, zRot + torque.zRot);
    }
}