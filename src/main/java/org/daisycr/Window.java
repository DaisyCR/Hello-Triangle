package org.daisycr;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private long glfwWindow;
    private int width, height;
    private String title;
    private static Window window;
    private static Scene currentScene;

    public Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Hello Triangle";
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    private static void changeScene(SceneInitializer sceneInitializer) {
        if(currentScene != null){
            currentScene.destroy();
        }
        currentScene = new Scene(sceneInitializer);
        currentScene.init();
        currentScene.start();
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion());
        init();
        loop();
        clear();
    }

    private void init(){
        GLFWErrorCallback.createPrint(System.err).set();
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Unable to create GLFW window");
        }

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        glfwShowWindow(glfwWindow);
        GL.createCapabilities();

        //Window.changeScene(new Triangle());
    }

    private void loop(){
        float startTime = (float)glfwGetTime();
        float endTime;
        float deltaTime = -1;

        while(!glfwWindowShouldClose(glfwWindow)){
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

           /* if(deltaTime >= 0){
                currentScene.update(deltaTime);
                currentScene.render();
            }*/

            glfwSwapBuffers(glfwWindow);
            endTime = (float)glfwGetTime();
            deltaTime = endTime - startTime;
            startTime = endTime;
        }
    }

    private void clear(){
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
}
