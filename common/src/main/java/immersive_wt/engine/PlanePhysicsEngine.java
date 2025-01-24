package immersive_wt.engine;

import immersive_wt.engine.system.*;

public class PlanePhysicsEngine {
    public Plane plane = new Plane();

    public KeyBoardDirectControlSystem keyBoardDirectControlSystem = new KeyBoardDirectControlSystem();
    public RotationalDynamicsSystem rotationalDynamicsSystem = new RotationalDynamicsSystem();
    public EngineSystem engineSystem = new EngineSystem();
    public WingSystem wingSystem = new WingSystem();
    public GravitySystem gravitySystem = new GravitySystem();



    public void emulate() {
        plane.resetControl();

        keyBoardDirectControlSystem.progress(plane);
        rotationalDynamicsSystem.progress(plane);
        gravitySystem.progress(plane);
        engineSystem.progress(plane);
        wingSystem.progress(plane);
    }
}
