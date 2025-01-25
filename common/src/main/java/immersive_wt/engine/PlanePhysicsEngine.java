package immersive_wt.engine;

import immersive_wt.engine.plane.*;
import net.minecraft.world.phys.Vec3;

public class PlanePhysicsEngine {
    public Plane plane = new Plane();

    public Tail tail = new Tail(4);
    public Aileron aileron = new Aileron(6);
    public FineTuningTorque fineTuningTorque = new FineTuningTorque(0.1);
    public Engine engine = new Engine(0.02);

    protected TorqueModule[] torqueModules = new TorqueModule[]{
            tail,
            aileron,
            new Ground(),
            fineTuningTorque,
            new GravityTorque(0.2)
    };
    protected ForceModule[] forceModules = new ForceModule[]{
            engine,
            new Wing(0.01, 0.1),
            new Gravity(),
            new Wheel(0.01),
    };


    public Torque torque() {
        Torque result = new Torque(0, 0, 0);
        for (TorqueModule torqueModule : torqueModules)
            result = result.add(torqueModule.torque(plane));
        return result;
    }

    public Vec3 force() {
        Vec3 result = Vec3.ZERO;
        for (ForceModule forceModule : forceModules)
            result = result.add(forceModule.force(plane));


        return result;
    }
}
