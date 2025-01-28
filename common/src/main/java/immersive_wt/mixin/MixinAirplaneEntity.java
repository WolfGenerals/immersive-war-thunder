package immersive_wt.mixin;

import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.AirplaneEntity;
import immersive_aircraft.entity.misc.PositionDescriptor;
import immersive_wt.EngineVehicleAccessor;
import immersive_wt.KeyBindings;
import immersive_wt.engine.EngineManager;
import immersive_wt.engine.PlanePhysicsEngine;
import immersive_wt.engine.Torque;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

import static org.joml.Math.*;

@Mixin(AirplaneEntity.class)
abstract public class MixinAirplaneEntity extends AircraftEntity {

    @Unique
    private float immersive_war_thunder$playerXRot;
    @Unique
    private float immersive_war_thunder$playerYRot;

    public MixinAirplaneEntity(EntityType<? extends AircraftEntity> entityType, Level world, boolean canExplodeOnCrash) {
        super(entityType, world, canExplodeOnCrash);
    }

    // 解除视角角度限制
    @Override
    public void copyEntityData(@NotNull Entity entity) {
        entity.setYBodyRot(getYRot());
        entity.setYRot(entity.getYRot());
        entity.setYHeadRot(entity.getYRot());
    }

    // 防止视角随着飞机改变
    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull MoveFunction positionUpdater) {
        if (!hasPassenger(passenger)) {
            return;
        }

        Matrix4f transform = getVehicleTransform();

        int size = getPassengers().size() - 1;
        List<List<PositionDescriptor>> positions = getPassengerPositions();
        if (size < positions.size()) {
            int i = getPassengers().indexOf(passenger);
            if (i >= 0 && i < positions.get(size).size()) {
                PositionDescriptor positionDescriptor = positions.get(size).get(i);

                float x = positionDescriptor.x();
                float y = positionDescriptor.y();
                float z = positionDescriptor.z();

                //animals are thicc
                if (passenger instanceof Animal) {
                    z += 0.2f;
                }

                y += (float) passenger.getMyRidingOffset();

                Vector4f worldPosition = transformPosition(transform, x, y, z);
                passenger.setPos(worldPosition.x, worldPosition.y, worldPosition.z);
                positionUpdater.accept(passenger, worldPosition.x, worldPosition.y, worldPosition.z);

                copyEntityData(passenger);
                if (passenger instanceof Animal animal && size > 1) {
                    int angle = passenger.getId() % 2 == 0 ? 90 : 270;
                    passenger.setYBodyRot(animal.yBodyRot + (float) angle);
                    passenger.setYHeadRot(passenger.getYHeadRot() + (float) angle);
                }
            }
        }
    }

    @Override
    public void setZRot(float rot) {
        float loops = (float) (Math.floor((rot + 180f) / 360f) * 360f);
        rot -= loops;
        prevRoll -= loops;
        roll = rot;
    }

    @Override
    public void setYRot(float yaw) {
        float loops = (float) (Math.floor((yaw + 180f) / 360f) * 360f);
        yaw -= loops;
        yRotO -= loops;
        super.setYRot(yaw);
    }

    @Override
    protected void updateVelocity() {
    }

    @Override
    protected float getGravity() {
        return -1;
    }

    @Override
    protected void updateController() {
        PlanePhysicsEngine planePhysicsEngine = EngineManager.getPlane(uuid);
        if (movementY != 0) {
            setEngineTarget(Math.max(0.0f, Math.min(1.0f, getEngineTarget() + 0.1f * movementY)));
        }
        planePhysicsEngine.engine.setPower(getEnginePower() + getBoost() > 0 ? 1.5 : 0);
        // 获得乘客
        LivingEntity passenger = getControllingPassenger();

        // 无玩家控制
        if (!(passenger instanceof Player player)) {
            // 没有玩家 舵面归零
            planePhysicsEngine.tail.setControl(0);
            planePhysicsEngine.aileron.setControl(0);
            planePhysicsEngine.fineTuningTorque.setControl(0, 0);
            return;
        }
        // 键盘有输入或自由视角
        if (movementX != 0 || movementZ != 0) {
            // 键盘控制
            planePhysicsEngine.tail.setControl(-movementZ);
            planePhysicsEngine.aileron.setControl(-movementX);
            planePhysicsEngine.fineTuningTorque.setControl(0, 0);
            return;
        }

        // 玩家视线控制
        if (!KeyBindings.getFreeView()) {
            // 只在不是自由视角时才更新控制
            immersive_war_thunder$playerXRot = player.getXRot();
            immersive_war_thunder$playerYRot = player.getYRot();
        }
        float planeXRot = getXRot();
        float planeYRot = getYRot();

        float deltaX = Mth.degreesDifference(planeXRot, immersive_war_thunder$playerXRot); //mc自带的pitch向下
        float deltaY = Mth.degreesDifference(planeYRot, immersive_war_thunder$playerYRot);//mc自带的yaw向右

        double controlPitch = 0;
        double controlRoll = 0;

        float r = toRadians(getRoll());
        controlPitch += (-deltaX * cos(r) + deltaY * sin(r)) / 45; //
        controlPitch = Mth.clamp(controlPitch, -1, 1);
        controlPitch *= 1 - Mth.clamp(abs(deltaX * sin(r) + deltaY * cos(r)) / 45, 0, 1);// 非pitch方向惩罚

        double targetRoll = toDegrees(atan2(deltaY, -deltaX + 5));
        if (!Double.isFinite(targetRoll)) targetRoll = 0;
        controlRoll += (targetRoll - getRoll()) / 45;
        controlRoll = Mth.clamp(controlRoll, -1, 1);
        controlRoll *= Mth.clamp(sqrt(deltaX * deltaX + deltaY * deltaY) / 30, 0.3, 1);

        if (planePhysicsEngine.plane.isOnGround())
            controlRoll = deltaY / 30;

        planePhysicsEngine.tail.setControl(controlPitch);
        planePhysicsEngine.aileron.setControl(controlRoll);
        planePhysicsEngine.fineTuningTorque.setControl(deltaX, deltaY);


    }


    @Override
    public void tick() {
        prevRoll = getRoll();
        EngineVehicleAccessor engineVehicle = (EngineVehicleAccessor) this;
        engineVehicle.engineVehicle$tick();

        PlanePhysicsEngine planePhysicsEngine = EngineManager.getPlane(uuid);

        // sync
        planePhysicsEngine.plane.position = getPosition(1);
        planePhysicsEngine.plane.velocity = getDeltaMovement();
        planePhysicsEngine.plane.forwardDirection = toVec3d(getForwardDirection()).normalize();
        planePhysicsEngine.plane.topDirection = toVec3d(getTopDirection()).normalize();
        planePhysicsEngine.plane.roll = getRoll();
        planePhysicsEngine.plane.pitch = getXRot();
        planePhysicsEngine.plane.yaw = getYRot();
        planePhysicsEngine.plane.setOnGround(onGround());

        Torque torque = planePhysicsEngine.torque();
        Vec3 force = planePhysicsEngine.force();
        //检查力是否有效
        if (Double.isFinite(torque.xRot) && Double.isFinite(torque.yRot) && Double.isFinite(torque.zRot)
                && Double.isFinite(force.x) && Double.isFinite(force.y) && Double.isFinite(force.z)) {
            setXRot(getXRot() + (float) torque.xRot);
            setYRot(getYRot() + (float) torque.yRot);
            setZRot(getRoll() + (float) torque.zRot);
            setDeltaMovement(getDeltaMovement().add(force));
        }
    }
}