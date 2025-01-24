package immersive_wt.engine;

import immersive_wt.engine.system.ForwardSystem;
import immersive_wt.engine.system.KeyBoardDirectControlSystem;
import immersive_wt.engine.system.Plane;
import immersive_wt.engine.system.RotationalDynamicsSystem;

public class PlanePhysicsEngine {
    public Plane plane = new Plane();

    public KeyBoardDirectControlSystem keyBoardDirectControlSystem = new KeyBoardDirectControlSystem();
    public RotationalDynamicsSystem rotationalDynamicsSystem = new RotationalDynamicsSystem();
    public ForwardSystem forwardSystem = new ForwardSystem();



    public void emulate() {
        plane.resetControl();

        keyBoardDirectControlSystem.progress(plane);
        rotationalDynamicsSystem.progress(plane);
        forwardSystem.progress(plane);
    }
}
