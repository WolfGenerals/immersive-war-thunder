package immersive_wt.mixin.client;

import immersive_aircraft.entity.AirplaneEntity;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Inject(method = "setup", at = @At("TAIL"))
    public void ia$setup(BlockGetter area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (thirdPerson && entity.getVehicle() instanceof AirplaneEntity airplane) {
            move(-getMaxZoom(airplane.getZoom())*1.2, getMaxZoom(airplane.getZoom())*1.2, 0.0);
        }
    }

    @Shadow
    protected abstract void move(double x, double y, double z);

    @Shadow
    public abstract Vec3 getPosition();
    @Shadow
    protected abstract void setPosition(Vec3 pos);

    @Shadow
    protected abstract double getMaxZoom(double desiredCameraDistance);
}