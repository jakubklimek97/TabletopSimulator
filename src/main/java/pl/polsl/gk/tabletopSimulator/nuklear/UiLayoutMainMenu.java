package pl.polsl.gk.tabletopSimulator.nuklear;

import org.lwjgl.nuklear.*;
import org.lwjgl.system.MemoryStack;
import pl.polsl.gk.tabletopSimulator.scenes.SceneList;
import pl.polsl.gk.tabletopSimulator.scenes.SceneManager;

import static org.lwjgl.nuklear.Nuklear.*;

public class UiLayoutMainMenu {
    private SceneManager sceneManager;
    public UiLayoutMainMenu(SceneManager sceneManager){
        this.sceneManager = sceneManager;
        nextScene = SceneList.SCENE_SELECT;
    }
    public SceneList nextScene;
    public void layout(NkContext ctx, int x, int y){
        try(MemoryStack stack = MemoryStack.stackPush()){
            NkRect rect = NkRect.mallocStack(stack);

            if(
                    nk_begin(
                    ctx,
                    "Scene Selection",
                    nk_rect(x,y, 250, 186, rect),
                            NK_WINDOW_NO_SCROLLBAR | NK_WINDOW_TITLE
                    )
            ){
                nk_layout_row_static(ctx, 45, 233, 1);
                if(nk_button_label(ctx, "Terrain + Skeletal Animation")){
                    nextScene = SceneList.SKELETAL;
                }
                nk_layout_row_static(ctx, 45, 233, 1);
                if(nk_button_label(ctx, "Rest")){
                    nextScene = SceneList.TEST_FUNC_3;
                }
                nk_layout_row_static(ctx, 45, 233, 1);
                if(nk_button_label(ctx, "Quit")){
                    nextScene = SceneList.QUIT;
                }
            }
            nk_end(ctx);
        }
    }
}
