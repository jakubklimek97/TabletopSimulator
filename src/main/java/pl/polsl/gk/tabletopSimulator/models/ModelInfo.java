package pl.polsl.gk.tabletopSimulator.models;

public class ModelInfo {

    private final int vertexCount;
    private final int vaoID;


    public ModelInfo(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }
}
