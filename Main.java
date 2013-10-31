package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.math.ColorRGBA;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.scene.Spatial;

/**
 * Sample 5 - how to map keys and mousebuttons to actions
 */
public class Main extends SimpleApplication {
    static Vector3f direction;
    static float focusPointDistance;
    static Vector3f cameraLocation;
    static Vector3f up;

    public static void main(String[] args) {
        cameraLocation = new Vector3f(0, 20, 20);
        up = new Vector3f(0, 1, 0);
        direction = new Vector3f(0,0,1);
        focusPointDistance = 10;
        
        Main app = new Main();
        app.start();
    }
    protected Spatial player;
    Boolean isRunning = true;

    @Override
    public void simpleInitApp() {
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        player = new Geometry("Player", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //mat.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        mat.setColor("Color", ColorRGBA.Blue);
        player.setMaterial(mat);
        rootNode.attachChild(player);
        initKeys(); // load my custom keybinding
    }

    /**
     * Custom Keybinding: Map named actions to inputs.
     */
    private void initKeys() {
        // You can map one or several inputs to one named action
        inputManager.addMapping("MoveX", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping("MoveX", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping("MoveY", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping("MoveY", new MouseAxisTrigger(MouseInput.AXIS_Y, false));


        // Add the names to the action listener.
        inputManager.addListener(analogListener, "MoveX", "MoveY");

    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {
                isRunning = !isRunning;
            }
        }
    };
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            inputManager.setCursorVisible(true);

            System.out.println("locationCam" + cam.getLocation().x + " " + cam.getLocation().y + " " + cam.getLocation().z);
            if (isRunning) {
                if (name.equals("Rotate")) {
                    player.rotate(0, value * speed, 0);
                }
                if (name.equals("Right")) {
                    Vector3f v = player.getLocalTranslation();
                    player.setLocalTranslation(v.x + value * speed, v.y, v.z);
                }
                if (name.equals("MoveX")) {
                    Vector2f position = inputManager.getCursorPosition();
                    int reversedXOffset = (int)position.x;
                    int centeredXOffset = reversedXOffset - getContext().getSettings().getWidth()/2;
                    cameraLocation.x = centeredXOffset / 50;

                    if(cameraLocation.x < -100)cameraLocation.x = -100;
                    if(cameraLocation.x > 100)cameraLocation.x = 100;
                    cam.setLocation(cameraLocation);
                }
                if (name.equals("MoveY")) {
                    Vector2f position = inputManager.getCursorPosition();
                    int reversedYOffset = getContext().getSettings().getHeight() - (int)position.y;
                    int centeredYOffset = reversedYOffset - getContext().getSettings().getHeight()/2;
                    cameraLocation.y = centeredYOffset / 50;
//                    cameraLocation.y = (int) ((getContext().getSettings().getHeight() - ((float) getContext().getSettings().getHeight()/2 - position.y)) / 50);
                    
                    if(cameraLocation.y < -100)cameraLocation.y = -100;
                    if(cameraLocation.y > 100)cameraLocation.y = 100;
                    cam.setLocation(cameraLocation);
                }
                if (name.equals("Left")) {
                    Vector3f v = player.getLocalTranslation();
                    player.setLocalTranslation(v.x - value * speed, v.y, v.z);
                }
            } else {
                System.out.println("Press P to unpause.");
            }
            cam.lookAt(player.getLocalTranslation(), up);
        }
    };
}