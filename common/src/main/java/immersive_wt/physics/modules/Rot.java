package immersive_wt.physics.modules;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import immersive_wt.ImmersiveWarThunder;
import immersive_wt.physics.Module;
import immersive_wt.physics.Plane;
import immersive_wt.physics.Torque;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.*;

public abstract class Rot extends Module {
    protected double speed = 0;
    protected double inertial = 0;
    protected double max = 1;
    protected double min = -1;

    protected double control = 0;

    public Rot(@NotNull Plane plane, @NotNull JsonObject config) {
        super(plane, config);
        try {
            speed = config.get("speed").getAsDouble();
            inertial = config.get("inertial").getAsDouble();
            max = config.get("max").getAsDouble();
            min = config.get("min").getAsDouble();
        } catch (JsonSyntaxException e) {
            ImmersiveWarThunder.logger.warning("Rot module config error:" + config);
        }
        // limit range
        max = Mth.clamp(max, -1, 1);
        min = Mth.clamp(min, -1, 1);
        if (max < min) {
            max = min;
        }
    }

    public void input(double input) {
        double inputControl = Mth.clamp(input, min, max);
        control = control * inertial + inputControl * (1 - inertial);
    }

    protected double getAngularVelocity() {
        return speed * control * sqrt(plane.velocity.length());
    }

    public static class XRot extends Rot {
        public XRot(@NotNull Plane plane, @NotNull JsonObject config) {
            super(plane, config);
        }

        @Override
        public Torque getTorque() {
            return new Torque(getAngularVelocity(), 0, 0);
        }
    }

    public static class YRot extends Rot {
        public YRot(@NotNull Plane plane, @NotNull JsonObject config) {
            super(plane, config);
        }

        @Override
        public Torque getTorque() {
            return new Torque(0, getAngularVelocity(), 0);
        }
    }

    public static class ZRot extends Rot {
        public ZRot(@NotNull Plane plane, @NotNull JsonObject config) {
            super(plane, config);
        }

        @Override
        public Torque getTorque() {
            return new Torque(0, 0, getAngularVelocity());
        }
    }

    public static class Pitch extends Rot {
        public Pitch(@NotNull Plane plane, @NotNull JsonObject config) {
            super(plane, config);
        }

        @Override
        public Torque getTorque() {
            double r = Math.toRadians(plane.zRot);

            return new Torque(-getAngularVelocity() * cos(r), getAngularVelocity() * sin(r), 0);
        }
    }
}
