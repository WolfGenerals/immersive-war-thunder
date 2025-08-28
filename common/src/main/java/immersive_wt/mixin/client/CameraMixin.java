package immersive_wt.mixin.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import immersive_aircraft.entity.AirplaneEntity;
import immersive_wt.config.Config;
import net.minecraft.client.Camera;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Inject(method = "setup", at = @At("TAIL"))
    public void ia$setup(BlockGetter area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (thirdPerson && entity.getVehicle() instanceof AirplaneEntity airplane) {
            String name = "";
            if (airplane.hasCustomName()) {
                name = Objects.requireNonNull(airplane.getCustomName()).getString();
            }
            String type = BuiltInRegistries.ITEM.getKey(airplane.asItem()).toString();
            JsonObject planeConfig = Config.getPlaneConfig(name, type);
            JsonElement cameraConfig = planeConfig.get("camera");
            float zoomX=1.2F;
            float zoomY=1.2F;
            if(cameraConfig!=null && cameraConfig.isJsonObject()){
                JsonObject cameraConfigObject = cameraConfig.getAsJsonObject();
                if(cameraConfigObject.has("zoomX")){
                    zoomX = cameraConfigObject.get("zoomX").getAsFloat();
                }
                if(cameraConfigObject.has("zoomY")){
                    zoomY = cameraConfigObject.get("zoomY").getAsFloat();
                }
            }
            move(-getMaxZoom(airplane.getZoom())*zoomX, getMaxZoom(airplane.getZoom())*zoomY, 0.0);
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