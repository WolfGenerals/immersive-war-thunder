package immersive_wt.fabric;

import immersive_wt.ImmersiveWarThunder;
import net.fabricmc.api.ModInitializer;

public class ImmersiveWarThunderFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ImmersiveWarThunder.init();
    }
}
