package immersive_wt.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import immersive_aircraft.entity.AirplaneEntity;
import immersive_wt.ImmersiveWarThunder;
import immersive_wt.util.LinearAlgebraUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
abstract public class MixinGui {
    @Unique
    private static final ResourceLocation CIRCLE_LOCATION = new ResourceLocation(ImmersiveWarThunder.MOD_ID, "textures/gui/circle.png");
    @Unique
    private static final ResourceLocation CROSSHAIR_LOCATION = new ResourceLocation(ImmersiveWarThunder.MOD_ID, "textures/gui/crosshair.png");

    @Final
    @Shadow
    private Minecraft minecraft;
    @Shadow
    private int screenWidth;
    @Shadow
    private int screenHeight;

    @Inject(method = "renderCrosshair(Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At("HEAD"))
    private void ic_air$renderInject(GuiGraphics guiGraphics, CallbackInfo ci) {
        // 画个圆在屏幕中间，如果是第三人称
        if (minecraft.options.getCameraType().isFirstPerson()) return;
        if (minecraft.gameMode == null || minecraft.player == null) return;
        if (!(minecraft.player.getRootVehicle() instanceof AirplaneEntity plane)) return;

        float tickDelta = minecraft.getFrameTime();

        Vec3 crosshairWorldPos = plane.getViewVector(tickDelta)
                .scale(50)
                .add(plane.getPosition(tickDelta));
        Vec3 circleWorldPos = minecraft.player.getForward()
                .scale(50)
                .add(plane.getPosition(tickDelta));

        Vector3f crosshairScreenPos = LinearAlgebraUtil.worldToScreenPoint(crosshairWorldPos, tickDelta);
        Vector3f circleScreenPos = LinearAlgebraUtil.worldToScreenPoint(circleWorldPos, tickDelta);


        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );

        int crossSize = 31;
        guiGraphics.blit(CROSSHAIR_LOCATION,
                (int) crosshairScreenPos.x() - crossSize / 2, (int) crosshairScreenPos.y() - crossSize / 2,//x,y
                0, 0,// u/v offset
                crossSize, crossSize,// width,height
                crossSize, crossSize//texture size
        );
        int circleSize = 31;
        guiGraphics.blit(CIRCLE_LOCATION,
                (int) circleScreenPos.x() - circleSize / 2, (int) circleScreenPos.y() - circleSize / 2,//x,y
                0, 0,// u/v offset
                circleSize, circleSize,// width,height
                circleSize, circleSize//texture size
        );

        RenderSystem.defaultBlendFunc();
    }
}

