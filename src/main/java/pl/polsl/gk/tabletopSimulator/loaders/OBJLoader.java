package pl.polsl.gk.tabletopSimulator.loaders;

import org.joml.Vector2f;
import org.joml.Vector3f;
import pl.polsl.gk.tabletopSimulator.entities.Mesh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;




public class OBJLoader  {
    public static List<Vector3f> normalsPosition = new ArrayList<>();
    public static List<Vector2f> texturesPosition = new ArrayList<>();
    public static List<Vector3f> vertices = new ArrayList<>();
    public static List<Faces> faces = new ArrayList<>();



    public static Mesh load(String fileName) {

        List<String> txtObjLines = readAllLines(fileName);

        for (String txtObjLine : txtObjLines) {
            String[] dataType = txtObjLine.split("\\s+");
            switch (dataType[0]) {
                case "v":
                    Vector3f vec3Geometry = new Vector3f(
                            Float.parseFloat(dataType[1]),
                            Float.parseFloat(dataType[2]),
                            Float.parseFloat(dataType[3]));
                    vertices.add(vec3Geometry);
                    break;
                case "vt":

                    Vector2f vec2TextureCoords = new Vector2f(
                            Float.parseFloat(dataType[1]),
                            Float.parseFloat(dataType[2]));
                    texturesPosition.add(vec2TextureCoords);
                    break;

                case "vn":

                    Vector3f vec3Normals = new Vector3f(
                            Float.parseFloat(dataType[1]),
                            Float.parseFloat(dataType[2]),
                            Float.parseFloat(dataType[3]));
                    normalsPosition.add(vec3Normals);
                    break;
                case "f":
                    Faces face = new Faces(dataType[1], dataType[2], dataType[3]);
                    faces.add(face);
                    break;

                default:
                    // In the future more data from obj
                    break;
            }

        }
        return  reorderList(vertices,texturesPosition,normalsPosition,faces);
    }


    private static Mesh reorderList(List<Vector3f> positionList, List<Vector2f> textureCoordsList,
                                    List<Vector3f> normalList, List<Faces> facesList){

        List<Integer> indices = new ArrayList<>();

        float[] positionsArray = new float[positionList.size() * 3];
        int i = 0;
        for(Vector3f position : positionList){
            positionsArray[i * 3] = position.x;
            positionsArray[i * 3 + 1] = position.y;
            positionsArray[i * 3 + 2] = position.z;
            i++;
        }
        float[] textureCoordsArr = new float[positionList.size() * 2];
        float[] textureNormalsArr = new float[positionList.size() * 3];

        for(Faces face : facesList){

            Indexes[] faceVertexIdx = face.getIndexes();
            for(Indexes indxValue: faceVertexIdx){
                processFaceVertex(indxValue, textureCoordsList, normalList,
                        indices,textureCoordsArr,textureNormalsArr);
            }
        }

        int[] indicesArr;
        indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
        return new Mesh(indicesArr, positionsArray, textureCoordsArr, textureNormalsArr,texturesPosition,vertices,facesList);


    }

    public static List<String> readAllLines(String fileName)  {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Class.forName(OBJLoader.class.getName()).getResourceAsStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static void processFaceVertex(Indexes indices, List<Vector2f> textCoordList,
                                          List<Vector3f> normList, List<Integer> indicesList,
                                          float[] texCoordArr, float[] normArr) {

        // Set index for vertex coordinates
        int posIndex = indices.indexPos;
        indicesList.add(posIndex);

        // Reorder texture coordinates
        if (indices.indexTextureCoords >= 0) {
            Vector2f textCoord = textCoordList.get(indices.indexTextureCoords);
            texCoordArr[posIndex * 2] = textCoord.x;
            texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
        }
        if (indices.indexVectorNormals >= 0) {
            // Reorder vectornormals
            Vector3f vecNorm = normList.get(indices.indexVectorNormals);
            normArr[posIndex * 3] = vecNorm.x;
            normArr[posIndex * 3 + 1] = vecNorm.y;
            normArr[posIndex * 3 + 2] = vecNorm.z;
        }
    }

    public static class Faces {

        private final Indexes[] indexes;

        public Faces(String vertex1, String vertex2, String vertex3){
            indexes = new Indexes[3];

            indexes[0] = parseIndexes(vertex1);
            indexes[1] = parseIndexes(vertex2);
            indexes[2] = parseIndexes(vertex3);
        }
        private Indexes parseIndexes(String txtLine){
            Indexes indexes = new Indexes();

            String[] dataTypeLines = txtLine.split("/");
            int length = dataTypeLines.length;
            indexes.indexPos = Integer.parseInt(dataTypeLines[0]) - 1;

            if(length > 1){ // if obj file is empty ( not given textCoords)
                String textCoords = dataTypeLines[1];
                indexes.indexTextureCoords = textCoords.length() > 0 ? Integer.parseInt(textCoords) - 1 : -1;
                if(length > 2){
                    indexes.indexVectorNormals = Integer.parseInt(dataTypeLines[2]) - 1;

                }
            }
            return indexes;
        }

        public Indexes[] getIndexes(){
            return indexes;
        }
    }


    private static class Indexes {

        public int indexPos;

        public int indexTextureCoords;

        public int indexVectorNormals;

        public Indexes(){
            indexPos = -1;
            indexTextureCoords = -1;
            indexVectorNormals = -1;
        }

    }

}
