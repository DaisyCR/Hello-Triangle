package org.daisycr;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();

    public static Shader getShader(String source) {
        File file = new File(source);
        if(!AssetPool.shaders.containsKey(file.getAbsolutePath())){
            Shader shader = new Shader(source);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
        return AssetPool.shaders.get(file.getAbsolutePath());
    }
}
