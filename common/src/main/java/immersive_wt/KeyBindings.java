package immersive_wt;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedList;
import java.util.List;

public class KeyBindings {
    public static final List<KeyMapping> list = new LinkedList<>();

    public static final KeyMapping freeView = new KeyMapping(
            "key.immersive_wt.freeView",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "key.immersive_wt.category"
    );

    static {
        list.add(freeView);
    }
}
