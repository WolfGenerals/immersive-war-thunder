package immersive_wt.physics.modules;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import immersive_wt.ImmersiveWarThunder;
import immersive_wt.physics.Module;
import immersive_wt.physics.Plane;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class Thrust extends Module {
    protected double thrust = 0;
    protected double power = 0;


    public Thrust(@NotNull Plane plane, @NotNull JsonObject config) {
        super(plane, config);
        try {
            thrust = config.get("thrust").getAsDouble();
        }catch (JsonSyntaxException e){
            ImmersiveWarThunder.logger.warning("Thrust config error:" + config);
        }
    }

    @Override
    protected void updateForce() {
        force = plane.getForwardDirection().scale(thrust * power);
    }

    public void setPower(double power){
        power = Mth.clamp(power,0,1);
        this.power = power;
    }
}
