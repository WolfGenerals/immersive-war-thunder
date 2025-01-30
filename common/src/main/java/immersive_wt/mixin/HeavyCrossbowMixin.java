package immersive_wt.mixin;

import immersive_aircraft.entity.VehicleEntity;
import immersive_aircraft.entity.misc.WeaponMount;
import immersive_aircraft.entity.weapon.BulletWeapon;
import immersive_aircraft.entity.weapon.HeavyCrossbow;
import immersive_wt.config.Config;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HeavyCrossbow.class)
public abstract class HeavyCrossbowMixin extends BulletWeapon {

    @Shadow
    private float cooldown;

    public HeavyCrossbowMixin(VehicleEntity entity, ItemStack stack, WeaponMount mount, int slot) {
        super(entity, stack, mount, slot);
    }

    @Override
    public void tick() {
        float rateOfFire = Config.config.get("heavyCrossbowRateOfFire").getAsFloat();
        cooldown -= rateOfFire/20f;
    }
}
