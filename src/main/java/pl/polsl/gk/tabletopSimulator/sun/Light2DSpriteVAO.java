package pl.polsl.gk.tabletopSimulator.sun;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

    public class Light2DSpriteVAO {

        public final int idVao;
        private Light2DSpriteVBO dataVbo;

        public static Light2DSpriteVAO create() {
            int id = GL30.glGenVertexArrays();
            return new Light2DSpriteVAO(id);
        }

        private Light2DSpriteVAO(int idVao) {
            this.idVao = idVao;
        }

        public void bind() {
            GL30.glBindVertexArray(idVao);
        }

        public void bind(int... attributes) {
            bind();
            for (int i : attributes) {
                GL20.glEnableVertexAttribArray(i);
            }
        }

        public void unbind() {
            GL30.glBindVertexArray(0);
        }

        public void unbind(int... attributes) {
            for (int i : attributes) {
                GL20.glDisableVertexAttribArray(i);
            }
            unbind();
        }

        public void delete() {
            GL30.glDeleteVertexArrays(idVao);
            dataVbo.delete();
        }

        public void storeData(int vertexCount, float[]... data) {
            float[] interleavedData = interleaveFloatData(vertexCount, data);
            int[] lengths = getAttributeLengths(data, vertexCount);
            storeInterleavedData(interleavedData, lengths);
        }


        private int[] getAttributeLengths(float[][] data, int vertexCount) {
            int[] lengths = new int[data.length];
            for (int i = 0; i < data.length; i++) lengths[i] = data[i].length / vertexCount;
            return lengths;
        }

        private void storeInterleavedData(float[] data, int... lengths) {
            dataVbo = Light2DSpriteVBO.create(GL15.GL_ARRAY_BUFFER);
            dataVbo.bind();
            dataVbo.storeDataIntoBuffer(data);
            int bytesPerVertex = calculateBytesPerVertex(lengths);
            linkVboDataToAttributes(lengths, bytesPerVertex);
            dataVbo.unbind();
        }

        private void linkVboDataToAttributes(int[] lengths, int bytesPerVertex) {
            int total = 0;
            for (int i = 0; i < lengths.length; i++) {
                GL20.glVertexAttribPointer(i, lengths[i], GL11.GL_FLOAT, false, bytesPerVertex, 4 * total);
                total += lengths[i];
            }
        }

        private int calculateBytesPerVertex(int[] lengths) {
            int total = 0;
            for (int length : lengths) total += length;
            return 4 * total;
        }

        private float[] interleaveFloatData(int count, float[]... data) {
            int totalSize = 0;
            int[] lengths = new int[data.length];
            for (int i = 0; i < data.length; i++) {
                int elementLength = data[i].length / count;
                lengths[i] = elementLength;
                totalSize += data[i].length;
            }
            float[] interleavedBuffer = new float[totalSize];
            int pointer = 0;
            for (int i = 0; i < count; i++) {
                for (int j = 0; j < data.length; j++) {
                    int elementLength = lengths[j];
                    for (int k = 0; k < elementLength; k++) {
                        interleavedBuffer[pointer++] = data[j][i * elementLength + k];
                    }
                }
            }
            return interleavedBuffer;
        }

    }
