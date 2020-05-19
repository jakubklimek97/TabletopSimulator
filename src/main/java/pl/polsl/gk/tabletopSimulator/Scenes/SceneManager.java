package pl.polsl.gk.tabletopSimulator.Scenes;

public class SceneManager {
    public SceneManager(long window){
        this.window = window;
        this.currentScene = null;
    }
    public void SwitchScene(SceneList newScene){
        if(currentScene != null){
            currentScene.UnInit();
        }
        switch(newScene){

            case LOADING:
                currentScene = new SceneLoading(this);
                break;
            case QUIT:
                currentScene = new SceneQuit();
                break;
            case TEST_FUNC_3:
                currentScene = new SceneFunctionality3(this);
                break;

        }
        currentScene.Init();
        currentScene.Run();
    }
    public long getWindow() {
        return window;
    }
    private long window;
    private IScene currentScene;


}
