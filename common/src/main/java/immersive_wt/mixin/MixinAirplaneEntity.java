package immersive_wt.mixin;

import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.AirplaneEntity;
import immersive_wt.EngineVehicleAccessor;
import immersive_wt.engine.EngineManager;
import immersive_wt.engine.PlanePhysicsEngine;
import immersive_wt.engine.Torque;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AirplaneEntity.class)
abstract public class MixinAirplaneEntity extends AircraftEntity {

    public MixinAirplaneEntity(EntityType<? extends AircraftEntity> entityType, Level world, boolean canExplodeOnCrash) {
        super(entityType, world, canExplodeOnCrash);
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
        prevRoll = getRoll();
        setXRot(getXRot() + (float) torque.xRot);
        setYRot(getYRot() + (float) torque.yRot);
        setZRot(getRoll() + (float) torque.zRot);
        setDeltaMovement(getDeltaMovement().add(force));
    }
}