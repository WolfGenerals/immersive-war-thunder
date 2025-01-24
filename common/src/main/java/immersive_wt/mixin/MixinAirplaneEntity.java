package immersive_wt.mixin;

import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.AirplaneEntity;
import immersive_wt.EngineVehicleAccessor;
import immersive_wt.engine.PlanePhysicsEngine;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
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

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public PlanePhysicsEngine planePhysicsEngine = new PlanePhysicsEngine();

    @Override
    protected void updateController() {
        planePhysicsEngine.keyBoardDirectControlSystem.input(movementX, movementZ);
        if (movementY != 0) {
            setEngineTarget(Math.max(0.0f, Math.min(1.0f, getEngineTarget() + 0.1f * movementY)));
            if (movementY < 0) {
                setDeltaMovement(getDeltaMovement().scale(0.95));
            }
        }
    }


    @Override
    public void tick() {
        EngineVehicleAccessor engineVehicle = (EngineVehicleAccessor) this;
        engineVehicle.engineVehicle$tick();

        // sync
        planePhysicsEngine.plane.position = getPosition(1);
        planePhysicsEngine.plane.velocity = getDeltaMovement();
        planePhysicsEngine.plane.direction = toVec3d(getForwardDirection()).normalize();
        planePhysicsEngine.plane.roll = getRoll();
        planePhysicsEngine.plane.pitch = getXRot();
        planePhysicsEngine.plane.yaw = getYRot();
        planePhysicsEngine.plane.enginePower = getEnginePower();
        planePhysicsEngine.plane.onGround = onGround();


        planePhysicsEngine.emulate();
        prevRoll = getRoll();
        setXRot(getXRot() + (float) planePhysicsEngine.plane.xRot);
        setYRot(getYRot() + (float) planePhysicsEngine.plane.yRot);
        setZRot(getRoll() + (float) planePhysicsEngine.plane.zRot);
//        setDeltaMovement(getDeltaMovement().add(planePhysicsEngine.plane.velocity));
        setDeltaMovement(planePhysicsEngine.plane.velocity);
    }
}