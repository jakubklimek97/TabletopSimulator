package pl.polsl.gk.tabletopSimulator.EngineManagers;

import pl.polsl.gk.tabletopSimulator.gui.Font;
import pl.polsl.gk.tabletopSimulator.utility.GeometryShader;

import java.util.HashMap;

public class FontManager {
    private FontManager(){
        this.shader = new GeometryShader("fontShader");
        this.shader.createUniform("textColor");
    }
    public static FontManager GetManager(){
        if(fontManager == null){
            fontManager = new FontManager();
        }

        return fontManager;
    }

    public Font GetFont(String name){
        if(loadedFonts.containsKey(name)){
            return loadedFonts.get(name);
        }
        else{
            Font requestedFont = new Font(name, 72.0f, this.shader);
            if(requestedFont.isGood()){
                loadedFonts.put(name, requestedFont);
                return requestedFont;
            }
            else{
                return null;
            }
        }
    }
    private static FontManager fontManager = null;
    private HashMap<String, Font> loadedFonts = new HashMap<String, Font>();
    private GeometryShader shader;
}
