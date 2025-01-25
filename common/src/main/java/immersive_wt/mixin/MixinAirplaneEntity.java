package immersive_wt.mixin;

import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.AirplaneEntity;
import immersive_aircraft.entity.misc.PositionDescriptor;
import immersive_wt.EngineVehicleAccessor;
import immersive_wt.engine.EngineManager;
import immersive_wt.engine.PlanePhysicsEngine;
import immersive_wt.engine.Torque;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(AirplaneEntity.class)
abstract public class MixinAirplaneEntity extends AircraftEntity {

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
        float delta = roll - rot;
        float loops = (float) (Math.floor((rot + 180f) / 360f) * 360f);
        rot -= loops;
        roll = rot;
        prevRoll = roll + delta;
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
        planePhysicsEngine.tail.setControl(pressingInterpolatedZ.getSmooth());
        planePhysicsEngine.aileron.setControl(pressingInterpolatedX.getSmooth());
        planePhysicsEngine.engine.setPower(getEnginePower());

        if (movementY != 0) {
            setEngineTarget(Math.max(0.0f, Math.min(1.0f, getEngineTarget() + 0.1f * movementY)));
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    protected int onGroundTimeRemaining = 0;

    @Override
    public void tick() {
        EngineVehicleAccessor engineVehicle = (EngineVehicleAccessor) this;
        engineVehicle.engineVehicle$tick();

        PlanePhysicsEngine planePhysicsEngine = EngineManager.getPlane(uuid);


        if (onGround())
            onGroundTimeRemaining = 10;
        onGroundTimeRemaining--;
        // sync
        planePhysicsEngine.plane.position = getPosition(1);
        planePhysicsEngine.plane.velocity = getDeltaMovement();
        planePhysicsEngine.plane.forwardDirection = toVec3d(getForwardDirection()).normalize();
        planePhysicsEngine.plane.topDirection = toVec3d(getTopDirection()).normalize();
        planePhysicsEngine.plane.roll = getRoll();
        planePhysicsEngine.plane.pitch = getXRot();
        planePhysicsEngine.plane.yaw = getYRot();
//        planePhysicsEngine.plane.onGround = onGround();
        planePhysicsEngine.plane.onGround = onGroundTimeRemaining > 0;

        Torque torque = planePhysicsEngine.torque();
        Vec3 force = planePhysicsEngine.force();
        setXRot(getXRot() + (float) torque.xRot);
        setYRot(getYRot() + (float) torque.yRot);
        setZRot(getRoll() + (float) torque.zRot);
        setDeltaMovement(getDeltaMovement().add(force));
    }
}