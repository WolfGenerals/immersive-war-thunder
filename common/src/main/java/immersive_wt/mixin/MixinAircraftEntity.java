package immersive_wt.mixin;

import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.EngineVehicle;
import immersive_wt.EngineVehicleAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AircraftEntity.class)
public abstract class MixinAircraftEntity extends EngineVehicle implements EngineVehicleAccessor {
    public MixinAircraftEntity(EntityType<? extends EngineVehicle> entityType, Level world, boolean canExplodeOnCrash) {
        super(entityType, world, canExplodeOnCrash);
    }

    @Override
    public void engineVehicle$tick() {
        super.tick();
    }
}

