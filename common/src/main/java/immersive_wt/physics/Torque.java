package immersive_wt.physics;

import java.util.Objects;

public record Torque(double x, double y, double z) {
    public static final Torque ZERO = new Torque(0, 0, 0);
    public Torque add(Torque torque) {
        return new Torque(x + torque.x, y + torque.y, z + torque.z);
    }
    public Torque multiply(double multiplier) {
        return new Torque(x * multiplier, y * multiplier, z * multiplier);
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Torque torque)) {
            return false;
        }
        return Double.compare(x, torque.x) == 0 && Double.compare(y, torque.y) == 0 && Double.compare(z, torque.z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
