package org.daisycr;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private Renderer renderer;
    private List<GameObject> gameObjectList;

    public Scene(SceneInitializer sceneInitializer){
        this.renderer = new Renderer();
        this.gameObjectList = new ArrayList<>();
    }


    public void init() {
    }

    public void start(){
        for (GameObject go : gameObjectList){
            this.renderer.add(go);
            go.start();
        }
    }

    public void update(float deltaTime) {
        for (GameObject go : gameObjectList){
            go.update(deltaTime);
            if(go.isDead()){
                gameObjectList.remove(go);
                this.renderer.destroyGameObject(go);
            }
        }
    }

    public void render() {
        this.renderer.render();
    }

    public void destroy() {
        for (GameObject go : gameObjectList){
            go.destroy();
        }
    }
}
