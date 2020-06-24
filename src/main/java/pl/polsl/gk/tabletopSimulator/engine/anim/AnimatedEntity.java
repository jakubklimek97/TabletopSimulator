package pl.polsl.gk.tabletopSimulator.engine.anim;



import org.joml.Vector3f;
import pl.polsl.gk.tabletopSimulator.engine.assimp.Mesh;
import java.util.Map;
import java.util.Optional;

public class AnimatedEntity {

    private Map<String, Animation> animations;

    private Animation currentAnimation;

    public AnimatedEntity(Mesh[] meshes, Map<String, Animation> animations) {
        this(meshes[0]);
        this.animations = animations;
        Optional<Map.Entry<String, Animation>> entry = animations.entrySet().stream().findFirst();
        currentAnimation = entry.isPresent() ? entry.get().getValue() : null;

    }
    public AnimatedEntity(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f();
        scale = 1;
        rotation = new Vector3f();
    }
    public Animation getAnimation(String name) {
        return animations.get(name);
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(Animation currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    private final Mesh mesh;

    private final Vector3f position;

    private float scale;

    private final Vector3f rotation;

    private Vector3f pickColor;

    private String name;



    public pl.polsl.gk.tabletopSimulator.engine.assimp.Mesh getMesh() {
        return mesh;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public float getScale() {
        return scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getPickColor() {
        return pickColor;
    }

    public void setPickColor(Vector3f pickColor) {
        this.pickColor = pickColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
