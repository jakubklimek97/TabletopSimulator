package pl.polsl.gk.tabletopSimulator.engine.managers;

public class OrthogonalCoordsManager {

    public OrthogonalCoordsManager(float left, float right, float bottom, float top, float near, float far){
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
        this.near = near;
        this.far = far;
    }
    public OrthogonalCoordsManager(OrthogonalCoordsManager orthogonalCoordsManager){
        this(orthogonalCoordsManager.left, orthogonalCoordsManager.right, orthogonalCoordsManager.bottom,
                orthogonalCoordsManager.top, orthogonalCoordsManager.near, orthogonalCoordsManager.far);
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getNear() {
        return near;
    }

    public void setNear(float near) {
        this.near = near;
    }

    public float getFar() {
        return far;
    }

    public void setFar(float far) {
        this.far = far;
    }

    private float left;
    private float right;
    private float bottom;
    private float top;
    private float near;
    private float far;

}
