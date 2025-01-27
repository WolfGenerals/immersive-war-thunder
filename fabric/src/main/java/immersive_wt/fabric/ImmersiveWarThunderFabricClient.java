package immersive_wt.fabric;

import immersive_wt.KeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class ImmersiveWarThunderFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBindings.list.forEach(KeyBindingHelper::registerKeyBinding);

    }
}
