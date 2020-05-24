package pl.polsl.gk.tabletopSimulator.gui;

import pl.polsl.gk.tabletopSimulator.engine.managers.TextureManager;

public class TexturedButton {
    public TexturedButton(TextureManager normal, TextureManager hover, TextureManager active, ButtonFunction onClick){
        onClickFunction = onClick;
        textureNormal = normal;
        textureActive = active;
        textureHover = hover;
    }

    private ButtonFunction onClickFunction;
    private TextureManager textureNormal, textureHover, textureActive;

    public ButtonState getBtnState() {
        return btnState;
    }

    public void setBtnState(ButtonState btnState) {
        this.btnState = btnState;
    }
    public int getCurrentTextureId(){
        switch(btnState){
            case NORMAL:{
                return textureNormal.getTextureId();
            }
            case HOVER:{
                return textureHover.getTextureId();
            }
            case ACTIVE:{
                return textureActive.getTextureId();
            }
        }
        return 0;
    }
    public void Invoke(){
        onClickFunction.runOnClick();
    }
    public void Render(){
    }
    public enum ButtonState{
        NORMAL,
        ACTIVE,
        HOVER
    }

    private ButtonState btnState;
    private int vao, vbo, ebo;
}
