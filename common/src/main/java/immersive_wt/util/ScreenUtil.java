package immersive_wt.util;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.lang.reflect.Method;

public class ScreenUtil {

    /**
     * 将世界坐标系中的方向向量转换为屏幕坐标
     * 使用相似三角形计算方向向量与视平面的交点
     *
     * @param worldDirection 世界坐标系中的单位方向向量
     * @param partialTicks 部分刻时间，用于插值计算
     * @return 屏幕坐标 Vector2f(x, y)，如果方向在视野外返回null
     */
    public static Vector2f directionToScreen(Vec3 worldDirection, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();

        // 将世界方向向量转换到相机坐标系
        Vector3f cameraSpaceDir = worldToCameraSpace(worldDirection, camera, minecraft, partialTicks);

        // 如果方向在相机后方，返回null
        if (cameraSpaceDir.z() <= 0) {
            return null;
        }

        // 获取屏幕尺寸和FOV
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        float fov = getFov(minecraft, camera, partialTicks);

        // 使用相似三角形计算屏幕坐标
        return calculateScreenPosition(cameraSpaceDir, screenWidth, screenHeight, fov);
    }

    /**
     * 将世界坐标系的方向向量转换到相机坐标系
     */
    private static Vector3f worldToCameraSpace(Vec3 worldDirection, Camera camera, Minecraft minecraft, float partialTicks) {
        // 将方向向量转换为Vector3f
        Vector3f dir = new Vector3f((float)worldDirection.x, (float)worldDirection.y, (float)worldDirection.z);

        // 应用相机旋转的逆变换（将世界方向转换到相机空间）
        Quaternionf cameraRotation = new Quaternionf(camera.rotation()).conjugate();
        dir.rotate(cameraRotation);

        // 补偿视图抖动（view bobbing）
        if (minecraft.options.bobView().get()) {
            applyViewBobbing(dir, minecraft, partialTicks);
        }

        return dir;
    }

    /**
     * 应用视图抖动补偿
     */
    private static void applyViewBobbing(Vector3f direction, Minecraft minecraft, float partialTicks) {
        LocalPlayer player = minecraft.player;
        if (player != null) {
            float f = player.walkDist - player.walkDistO;
            float f1 = -(player.walkDist + f * partialTicks);
            float f2 = Mth.lerp(partialTicks, player.oBob, player.bob);

            // X轴旋转补偿
            Quaternionf q2 = new Quaternionf().rotationX(Math.abs(Mth.cos(f1 * (float)Math.PI - 0.2F) * f2) * 5.0F);
            q2.conjugate();
            direction.rotate(q2);

            // Z轴旋转补偿
            Quaternionf q1 = new Quaternionf().rotationZ(Mth.sin(f1 * (float)Math.PI) * f2 * 3.0F);
            q1.conjugate();
            direction.rotate(q1);

            // 平移补偿（对于方向向量，我们只关心旋转，但为了完整性保留）
            // 注意：方向向量不应该应用平移，所以这里注释掉平移部分
            // Vector3f bobTranslation = new Vector3f(
            //     Mth.sin(f1 * (float)Math.PI) * f2 * 0.5F,
            //     -Math.abs(Mth.cos(f1 * (float)Math.PI) * f2),
            //     0.0f
            // );
            // bobTranslation.y = -bobTranslation.y();
            // direction.add(bobTranslation);
        }
    }

    /**
     * 使用相似三角形计算屏幕位置
     */
    private static Vector2f calculateScreenPosition(Vector3f cameraSpaceDir, int screenWidth, int screenHeight, float fov) {
        // 归一化方向向量（虽然输入应该是单位向量，但为了安全还是归一化）
        cameraSpaceDir.normalize();

        // 计算视平面上的交点
        // 假设视平面在z=1的位置（相机前方1个单位）
        float z = 1.0f;
        float scale = z / cameraSpaceDir.z();
        float x = cameraSpaceDir.x() * scale;
        float y = cameraSpaceDir.y() * scale;

        // 将视平面坐标转换为屏幕坐标
        // 计算FOV对应的缩放因子
        float fovRad = (float)Math.toRadians(fov);
        float tanHalfFov = (float)Math.tan(fovRad / 2.0f);

        // 计算屏幕坐标
        float screenX = (-x / tanHalfFov) * (screenHeight / 2.0f) + screenWidth / 2.0f;
        float screenY = (y / tanHalfFov) * (screenHeight / 2.0f) + screenHeight / 2.0f;

        return new Vector2f(screenX, screenY);
    }

    /**
     * 获取当前FOV（视野）
     */
    private static float getFov(Minecraft minecraft, Camera camera, float partialTicks) {
        try {
            Method getFovMethod = minecraft.gameRenderer.getClass().getDeclaredMethod("getFov", Camera.class, float.class, boolean.class);
            getFovMethod.setAccessible(true);
            return (float) getFovMethod.invoke(minecraft.gameRenderer, camera, partialTicks, true);
        } catch (Exception e) {
            // 如果无法访问方法，回退到默认FOV
            return 70.0f;
        }
    }

    /**
     * 检查屏幕坐标是否在屏幕范围内
     */
    public static boolean isOnScreen(Vector2f screenPos, int screenWidth, int screenHeight) {
        return screenPos != null &&
               screenPos.x >= 0 && screenPos.x <= screenWidth &&
               screenPos.y >= 0 && screenPos.y <= screenHeight;
    }

    /**
     * 计算从相机位置出发，指向世界坐标点的方向向量
     */
    public static Vec3 getDirectionToPoint(Vec3 worldPoint, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();

        return worldPoint.subtract(cameraPos).normalize();
    }

    /**
     * 便捷方法：直接计算指向世界坐标点的屏幕位置
     */
    public static Vector2f pointToScreen(Vec3 worldPoint, float partialTicks) {
        Vec3 direction = getDirectionToPoint(worldPoint, partialTicks);
        return directionToScreen(direction, partialTicks);
    }
}