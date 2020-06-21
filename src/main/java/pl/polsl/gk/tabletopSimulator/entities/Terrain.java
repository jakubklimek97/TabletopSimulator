package pl.polsl.gk.tabletopSimulator.entities;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL33C;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import pl.polsl.gk.tabletopSimulator.utility.GeometryShader;
import pl.polsl.gk.tabletopSimulator.utility.TerrainMouseoverShader;
import pl.polsl.gk.tabletopSimulator.utility.TerrainShader;

import static org.lwjgl.opengl.GL33C.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashSet;


public class Terrain {
    public Terrain(){
        fieldsSelectStatus = new Boolean[TERRAIN_DIM*TERRAIN_DIM];
        notFlatFields = new HashSet<Integer>();
        for(int i = 0; i < TERRAIN_DIM*TERRAIN_DIM; ++i){
            fieldsSelectStatus[i] = false;
        }
        selected = new ArrayList<Integer>();
        terrainArray = new float[(TERRAIN_DIM+1)*(TERRAIN_DIM+1)*7];
        FloatBuffer buff = FloatBuffer.wrap(terrainArray);
        for(int row = 0; row < TERRAIN_DIM+1; ++row){
            for(int col =0; col < TERRAIN_DIM+1; ++col){
                buff.put(row);             //posX
                buff.put(0.0f);             //posY
                buff.put(col);            //height
                buff.put((float)row/TERRAIN_DIM); //texture cordX
                buff.put((float)col/TERRAIN_DIM); //texture cordY
                buff.put(0.0f);            //isMouseovered
                buff.put(0.0f);            //isSelected
            }
        }
        buff.flip();

        IntBuffer indicesBuffer = IntBuffer.allocate(6*TERRAIN_DIM*TERRAIN_DIM);
        int col = 0;
        for(int pos = 0; pos < TERRAIN_DIM*(TERRAIN_DIM+1)-1; ++pos){
            if(col == TERRAIN_DIM)
                col = 0;
            else{
                col++;
                int[] arr = {
                        pos, pos+TERRAIN_DIM+1, pos+TERRAIN_DIM+2, //first triangle
                        pos, pos+TERRAIN_DIM+2, pos+1              //second triangle
                };
                indicesBuffer.put(arr);
            }
        }
        indicesBuffer.flip();
        indicesAmount = indicesBuffer.capacity();
        IntBuffer natIndicesBuffer;
        FloatBuffer natVertexBuffer;
        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer tmp = stack.callocInt(1);
            glGenVertexArrays(tmp);
            vao = tmp.get(0);
            glGenBuffers(tmp);
            vbo = tmp.get(0);
            glGenBuffers(tmp);
            ebo = tmp.get(0);
            glGenFramebuffers(tmp);
            mouseoverFbo = tmp.get(0);
            glGenTextures(tmp);
            glTextureId = tmp.get(0);
            natVertexBuffer = stack.mallocFloat(buff.capacity());
            natVertexBuffer.put(buff);
            natVertexBuffer.flip();
            nativeVertexBuffer = natVertexBuffer;
            natIndicesBuffer = stack.mallocInt(indicesBuffer.capacity());
            natIndicesBuffer.put(indicesBuffer);
            natIndicesBuffer.flip();
        }
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, natVertexBuffer, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false,7*4,0);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false,7*4,3*4);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false,7*4,5*4);
        glEnableVertexAttribArray(2);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, natIndicesBuffer, GL_STATIC_DRAW);
        glBindVertexArray(0);
        shader = new TerrainShader();
        moShader = new TerrainMouseoverShader();

    }
    public Terrain(String filePath){
        this();
        retrieveImage(filePath, 2048, 2048);
    }
    public void bindAndDraw(){
        glBindTexture(GL_TEXTURE_2D, glTextureId);
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, indicesAmount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public TerrainShader GetShader() {
        return shader;
    }
    public TerrainMouseoverShader GetMouseoverShader(){
        return moShader;
    }
    public void toggleFieldHighlight(){
        if(highlight){
            for(int field : selected){
                setNativeVertexBuffer(translateUserFieldPosToInternalArrayPos(field)*7+6, 0.0f);
            }
            setNativeVertexBuffer(translateUserFieldPosToInternalArrayPos(lastMouseOver)*7+5, 0.0f);
        }
        else{
            for(int field : selected){
                setNativeVertexBuffer(translateUserFieldPosToInternalArrayPos(field)*7+6, 1.0f);
            }
            setNativeVertexBuffer(translateUserFieldPosToInternalArrayPos(lastMouseOver)*7+5, 1.0f);
        }
        updateNativeVertexBuffer();
        highlight = !highlight;
    }
    public void updateMouseOver(float x, float z){

    }
    public void toggleSelected(int x, int z){
        if(x > TERRAIN_DIM || z > TERRAIN_DIM || x < 0 || z < 0){
            return;
        }
        Boolean status = fieldsSelectStatus[z*TERRAIN_DIM+x];
        if(highlight) {
            nativeVertexBuffer.position(getQuadPositionInArray(x, z) * 7 + 6);
            nativeVertexBuffer.put(status.booleanValue() ? 0.0f : 1.0f);
            nativeVertexBuffer.position(0);
            glBindVertexArray(vao);
            glBufferData(GL_ARRAY_BUFFER, nativeVertexBuffer, GL_DYNAMIC_DRAW);
            glBindVertexArray(0);
        }
        if(status.booleanValue() == true){
            selected.remove(selected.indexOf(z*TERRAIN_DIM+x));
        }
        else{
            selected.add(z*TERRAIN_DIM+x);
        }

        fieldsSelectStatus[z*TERRAIN_DIM+x] = status ? false : true;
    }
    private int getQuadPositionInArray(int x, int z){
        return z * (TERRAIN_DIM+1) + x;
    }
    public void deselectAll(){
        for(Object pos : selected.toArray()){
            int z = (Integer)pos / TERRAIN_DIM;
            int x = (Integer)pos % TERRAIN_DIM;
            toggleSelected(x,z);
        }
    }
    public void increaseSelectedHeight(){
        HashSet<Integer> modifiedVertices = new HashSet<Integer>();
        HashSet<Integer> madeUneven = new HashSet<Integer>();

        for(int field: selected){
            int z = field / TERRAIN_DIM;
            int x = field % TERRAIN_DIM;
            int pos = getQuadPositionInArray(x,z);
            int upper = pos + TERRAIN_DIM+1;
            if(!modifiedVertices.contains(pos)){
                nativeVertexBuffer.position(pos*7+1);
                nativeVertexBuffer.put(nativeVertexBuffer.get(0)+0.25f);
                modifiedVertices.add(pos);
            }
            pos++;
            if(!modifiedVertices.contains(pos)){
                nativeVertexBuffer.position(pos*7+1);
                nativeVertexBuffer.put(nativeVertexBuffer.get(0)+0.25f);
                modifiedVertices.add(pos);
            }
            if(!modifiedVertices.contains(upper)){
                nativeVertexBuffer.position(upper*7+1);
                nativeVertexBuffer.put(nativeVertexBuffer.get(0)+0.25f);
                modifiedVertices.add(upper);
            }
            upper++;
            if(!modifiedVertices.contains(upper)){
                nativeVertexBuffer.position(upper*7+1);
                nativeVertexBuffer.put(nativeVertexBuffer.get(0)+0.25f);
                modifiedVertices.add(upper);
            }

            madeUneven.add(field-1);
            madeUneven.add(field+1);
            madeUneven.add(field+TERRAIN_DIM-1);
            madeUneven.add(field+TERRAIN_DIM);
            madeUneven.add(field+TERRAIN_DIM+1);
            madeUneven.add(field-TERRAIN_DIM-1);
            madeUneven.add(field-TERRAIN_DIM);
            madeUneven.add(field-TERRAIN_DIM+1);
        }
        //at start everything is flat, so by ORing elements made uneven by this operation
        //and previously, we get actual list
        madeUneven.removeAll(selected);
        notFlatFields.addAll(madeUneven);

        nativeVertexBuffer.position(0);
        glBindVertexArray(vao);
        glBufferData(GL_ARRAY_BUFFER, nativeVertexBuffer, GL_DYNAMIC_DRAW);
        glBindVertexArray(0);
    }
    private void setNativeVertexBuffer(int position, float value){
        nativeVertexBuffer.position(position);
        nativeVertexBuffer.put(value);
    }
    private float getNativeVertexBuffer(int position){
        nativeVertexBuffer.position(position);
        return nativeVertexBuffer.get(0);
    }
    public void updateNativeVertexBuffer(){
        nativeVertexBuffer.position(0);
        glBindVertexArray(vao);
        glBufferData(GL_ARRAY_BUFFER, nativeVertexBuffer, GL_DYNAMIC_DRAW);
        int error = glGetError();
        if(error != 0)
            System.out.println(error);
        glBindVertexArray(0);
    }
    public void setSelectedHeightToLastSelected(){
        int last = selected.get(selected.size()-1);
        if(!checkIfFlat(last)){
            return;
        }
        int lastPos = translateUserFieldPosToInternalArrayPos(last);
        float newHeight = getNativeVertexBuffer(lastPos*7+1);
        HashSet<Integer> modifiedFields = new HashSet<Integer>();
        for(int field: selected){
            if (field == last)
                continue;

            int fieldPos = translateUserFieldPosToInternalArrayPos(field)*7;
            setNativeVertexBuffer(fieldPos+1,newHeight);
            setNativeVertexBuffer(fieldPos+8,newHeight);
            setNativeVertexBuffer(fieldPos+(TERRAIN_DIM+1)*7+1,newHeight);
            setNativeVertexBuffer(fieldPos+(TERRAIN_DIM+1)*7+8,newHeight);
            modifiedFields.add(field-1);
            modifiedFields.remove(field);
            modifiedFields.add(field+1);
            modifiedFields.add(field+TERRAIN_DIM+1);
            modifiedFields.add(field+TERRAIN_DIM);
            modifiedFields.add(field+TERRAIN_DIM-1);
            modifiedFields.add(field-TERRAIN_DIM+1);
            modifiedFields.add(field-TERRAIN_DIM);
            modifiedFields.add(field-TERRAIN_DIM-1);
        }
        for(Object field: modifiedFields.toArray()){
            if(checkIfFlat((int)field)){
                modifiedFields.remove(field);
            }
        }
        notFlatFields.addAll(modifiedFields);
        updateNativeVertexBuffer();
    }
    private boolean checkIfFlat(int pos){
        if(pos < 0 )
            return true;
        int arrPos = translateUserFieldPosToInternalArrayPos(pos)*7;
        int upperPos = arrPos+ (TERRAIN_DIM+1)*7;
        return (getNativeVertexBuffer(arrPos+1) == getNativeVertexBuffer(arrPos+8)
                && getNativeVertexBuffer(upperPos+1) == getNativeVertexBuffer(upperPos+8)
                && getNativeVertexBuffer(arrPos+1) == getNativeVertexBuffer(upperPos+1));
    }
    private int translateUserFieldPosToInternalArrayPos(int pos){
        return getQuadPositionInArray(pos % TERRAIN_DIM, pos / TERRAIN_DIM);
    }
    public void updateSelectedField(int x, int z){
        int pos = x + z * TERRAIN_DIM;
        if(lastMouseOver != pos){
        if(highlight){
            setNativeVertexBuffer(translateUserFieldPosToInternalArrayPos(lastMouseOver)*7+5, 0.0f);
            setNativeVertexBuffer(translateUserFieldPosToInternalArrayPos(pos)*7+5, 1.0f);
            updateNativeVertexBuffer();
        }
       lastMouseOver = pos;
        }
    }
    private void retrieveImage(String file, int width, int height) {
        File textureFile = new File("test2.bmp");
        BufferedImage img = null;
        try{
            img = ImageIO.read(textureFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(img == null){
            img = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_RGB);
            Graphics2D gImg = img.createGraphics();
            gImg.setColor(new Color(156, 245, 149));
            gImg.fillRect(0,0,2048, 2048);
            try {
                ImageIO.write(img, "BMP", textureFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int[] pixelArray = new int[img.getHeight()*img.getWidth()];
        img.getRGB(0,0,img.getWidth(),img.getHeight(), pixelArray, 0, img.getWidth() );
        ByteBuffer colorBuffer = ByteBuffer.allocateDirect(pixelArray.length*4);
        for(int col = 0; col < img.getHeight(); ++col){
            for(int row = 0; row < img.getWidth(); ++row){
                int pixel = pixelArray[row + col * img.getWidth()];
                colorBuffer.put((byte)((pixel >> 16) & 0xFF));
                colorBuffer.put((byte)((pixel >> 8) & 0xFF));
                colorBuffer.put((byte)(pixel        & 0xFF));
            }
        }
        colorBuffer.flip();
        glBindTexture(GL_TEXTURE_2D, glTextureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, 2048, 2048, 0, GL_RGB, GL_UNSIGNED_BYTE, colorBuffer);
        glGenerateMipmap(GL_TEXTURE_2D);
    }

    //structure: x y z selected
    private float[] terrainArray;
    private FloatBuffer nativeVertexBuffer;
    private BufferedImage terrainTextureImg;
    private BufferedImage heightMapImg;
    private String terrainName;
    private int vao, vbo, ebo, glTextureId, mouseoverFbo;
    private TerrainShader shader;
    private TerrainMouseoverShader moShader;
    private int indicesAmount;
    final int TERRAIN_DIM = 25;
    private int prevSelectedX = 0, prevSelectedZ = 0;
    private Boolean[] fieldsSelectStatus;
    private ArrayList<Integer> selected;
    private HashSet<Integer> notFlatFields;
    private boolean highlight = true; ///TODO: W wersji finalnej ma byc false i triggerowane przez ui
    private int lastMouseOver = 0;
}
