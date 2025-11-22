package immersive_wt.mixin.client;

import immersive_aircraft.entity.AirplaneEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Shadow
    private double accumulatedDX;

    @Shadow
    private double accumulatedDY;

    @Inject(method = "turnPlayer", at = @At("HEAD"))
    private void ic_air$modifyMouseInputForPlaneRoll(double movementTime, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();

        LocalPlayer player = minecraft.player;
        if (player == null || !(player.getRootVehicle() instanceof AirplaneEntity plane)) {
            return;
        }
        if (!minecraft.options.getCameraType().isFirstPerson()) return;

        float partialTicks = minecraft.getTimer().getGameTimeDeltaTicks();

        // 获取飞机的完整旋转信息
        Vec3 planeViewVector = plane.getViewVector(partialTicks);
        float planePitch = plane.getXRot();
        float planeYaw = plane.getYRot();
        float planeRoll = plane.getRoll();
        Vec3 playerViewVector = player.getViewVector(partialTicks);
        float playerPitch = player.getXRot();
        float playerYaw = player.getYRot();

        float relativePitch =  Mth.wrapDegrees(playerPitch-planePitch);
        float relativeYaw = Mth.wrapDegrees(playerYaw-planeYaw);


        // 应用滚转变换矩阵
        immersive_war_thunder$transformMouseInput(planeRoll);
    }

    @Unique
    private void immersive_war_thunder$transformMouseInput(float roll) {
        double dx = this.accumulatedDX;
        double dy = this.accumulatedDY;

        // 将角度转换为弧度
        float rollRad = (float) Math.toRadians(roll);

        // 滚转变换矩阵
        // [ cos θ  -sin θ ]
        // [ sin θ   cos θ ]
        double cos = Mth.cos(rollRad);
        double sin = Mth.sin(rollRad);

        // 应用变换
        double transformedDX = dx * cos - dy * sin;
        double transformedDY = dx * sin + dy * cos;

        // 应用控制效率因子
        this.accumulatedDX = transformedDX;
        this.accumulatedDY = transformedDY;
    }
    @Unique
    private double immersive_war_thunder$calculateAlpha(double longitude, double latitude) {
        // 转换为弧度
        double lambdaRad = Math.toRadians(longitude);
        double phiRad = Math.toRadians(latitude);

        // 避免除零错误
        if (Math.abs(Math.sin(lambdaRad)) < 1e-10) {
            // 当sin(λ)接近0时，根据纬度确定α
            if (Math.abs(phiRad) < 1e-10) {
                return 0.0; // 赤道上，α=0
            } else {
                // 在极点附近，α接近±90度
                return (phiRad > 0) ? 90.0 : -90.0;
            }
        }

        // 计算α：tan(α) = -tan(φ) / sin(λ)
        double tanPhi = Math.tan(phiRad);
        double sinLambda = Math.sin(lambdaRad);

        double tanAlpha = -tanPhi / sinLambda;
        double alphaRad = Math.atan(tanAlpha);

        // 转换为度
        double alphaDeg = Math.toDegrees(alphaRad);

        // 处理特殊情况，确保结果在[-180, 180]范围内
        return Mth.wrapDegrees(alphaDeg);
    }

}