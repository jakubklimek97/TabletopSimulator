package pl.polsl.gk.tabletopSimulator.gui;

import java.util.ArrayList;

public class GuiElement {
    public GuiElement(float posX, float posY, float width, float height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        children = new ArrayList<GuiElement>();
    }

    public void addChild(GuiElement child) {
        children.add(child);
    }

    public void removeChildren() {
        for (GuiElement child : children
        ) {
            child.removeChildren();
        }
        children.clear();
    }

    float posX, posY, width, height;
    ArrayList<GuiElement> children;
}
