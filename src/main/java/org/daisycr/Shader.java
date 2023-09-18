package org.daisycr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int shaderProgramID;
    private String filepath;
    private String vertexSource;
    private String fragmentSource;
    private boolean isBeingUsed = false;

    public Shader(String filepath){
        this.filepath = filepath;
        try{
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitStr = source.split("(#type)( )+([a-zA-Z]+)");

            int index = source.indexOf("#type") + 6;
            int endOfLine = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, endOfLine).trim();

            index = source.indexOf("#type", endOfLine) + 6;
            endOfLine = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, endOfLine).trim();

            if(firstPattern.equals("vertex")){
                vertexSource = splitStr[1];
            } else if(firstPattern.equals("fragment")){
                fragmentSource = splitStr[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            if(secondPattern.equals("vertex")){
                vertexSource = splitStr[2];
            } else if(secondPattern.equals("fragment")){
                fragmentSource = splitStr[2];
            } else {
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }

        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }

    }

    public void compile() {
        int vertexID, fragmentID;

        //Compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        if(glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE){
            System.out.println("ERROR: '" + filepath + "'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, glGetShaderi(vertexID, GL_INFO_LOG_LENGTH)));
            assert false : "";
        }

        //Compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        if(glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE){
            System.out.println("ERROR: '" + filepath + "'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH)));
            assert false : "";
        }

        //Link shaders
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        if(glGetProgrami(shaderProgramID, GL_LINK_STATUS) == GL_FALSE){
            System.out.println("ERROR: '" + filepath + "'\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID, glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH)));
            assert false : "";
        }
    }

    public void use(){
        if(!isBeingUsed){
            glUseProgram(shaderProgramID);
            isBeingUsed = true;
        }
    }

    public void detach(){
        glUseProgram(0);
        isBeingUsed = false;
    }


}
