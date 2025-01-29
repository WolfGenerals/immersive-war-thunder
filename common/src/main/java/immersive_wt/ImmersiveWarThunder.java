package immersive_wt;


import immersive_wt.config.Config;

import java.util.logging.Logger;

public class ImmersiveWarThunder {
    public static final String MOD_ID = "immersive_wt";
    public static Logger logger = Logger.getLogger("immersive_wt");

    public static void init() {
        Config.init();
        Config.load();
    }
}
