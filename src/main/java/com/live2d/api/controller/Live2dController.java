package com.live2d.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.live2d.api.model.ModelConfig;
import com.live2d.api.service.ModelListService;
import com.live2d.api.service.ModelTexturesService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Live2D API 控制器
 * 提供所有REST API端点
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class Live2dController {
    
    @Value("${live2d.model.path:model}")
    private String modelPath;
    
    private final ModelListService modelListService;
    private final ModelTexturesService modelTexturesService;
    private final ObjectMapper objectMapper;
    
    public Live2dController(ModelListService modelListService, 
                           ModelTexturesService modelTexturesService) {
        this.modelListService = modelListService;
        this.modelTexturesService = modelTexturesService;
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * 获取模型配置
     * GET /api/get?id=1-23
     */
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getModel(@RequestParam("id") String id) {
        try {
            String[] parts = id.split("-");
            int modelId = Integer.parseInt(parts[0]);
            int textureId = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
            
            Object modelName = modelListService.getIdToName(modelId);
            if (modelName == null) {
                return ResponseEntity.badRequest().body("Model not found");
            }
            
            ModelConfig config = loadModelConfig(modelName, textureId);
            if (config == null) {
                return ResponseEntity.badRequest().body("Model config not found");
            }
            
            // 处理路径
            processModelPaths(config, modelName.toString());
            
            return ResponseEntity.ok(config);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * 随机切换模型
     * GET /api/rand?id=1
     */
    @GetMapping(value = "/rand", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> randomModel(@RequestParam("id") int currentId) {
        try {
            int modelCount = modelListService.getModelCount();
            if (modelCount == 0) {
                return ResponseEntity.badRequest().body("No models available");
            }
            
            int newId;
            do {
                newId = new Random().nextInt(modelCount) + 1;
            } while (newId == currentId && modelCount > 1);
            
            Object modelName = modelListService.getIdToName(newId);
            String message = modelListService.getMessageByIndex(newId - 1);
            
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> model = new HashMap<>();
            model.put("id", newId);
            model.put("name", modelName);
            model.put("message", message);
            response.put("model", model);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * 顺序切换模型
     * GET /api/switch?id=1
     */
    @GetMapping(value = "/switch", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> switchModel(@RequestParam("id") int currentId) {
        try {
            int modelCount = modelListService.getModelCount();
            if (modelCount == 0) {
                return ResponseEntity.badRequest().body("No models available");
            }
            
            int newId = currentId + 1;
            if (newId > modelCount) {
                newId = 1;
            }
            
            Object modelName = modelListService.getIdToName(newId);
            String message = modelListService.getMessageByIndex(newId - 1);
            
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> model = new HashMap<>();
            model.put("id", newId);
            model.put("name", modelName);
            model.put("message", message);
            response.put("model", model);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * 随机切换皮肤
     * GET /api/rand_textures?id=1-23
     */
    @GetMapping(value = "/rand_textures", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> randomTextures(@RequestParam("id") String id) {
        try {
            String[] parts = id.split("-");
            int modelId = Integer.parseInt(parts[0]);
            int currentTextureId = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
            
            Object modelName = modelListService.getIdToName(modelId);
            if (modelName == null) {
                return ResponseEntity.badRequest().body("Model not found");
            }
            
            List<Object> textures = modelTexturesService.getTextureList(modelName.toString());
            if (textures == null || textures.isEmpty()) {
                return ResponseEntity.badRequest().body("No textures available");
            }
            
            int newTextureId;
            do {
                newTextureId = new Random().nextInt(textures.size()) + 1;
            } while (newTextureId == currentTextureId && textures.size() > 1);
            
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> model = new HashMap<>();
            model.put("id", modelId + "-" + newTextureId);
            model.put("name", modelName);
            response.put("model", model);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * 顺序切换皮肤
     * GET /api/switch_textures?id=1-23
     */
    @GetMapping(value = "/switch_textures", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> switchTextures(@RequestParam("id") String id) {
        try {
            String[] parts = id.split("-");
            int modelId = Integer.parseInt(parts[0]);
            int currentTextureId = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
            
            Object modelName = modelListService.getIdToName(modelId);
            if (modelName == null) {
                return ResponseEntity.badRequest().body("Model not found");
            }
            
            List<Object> textures = modelTexturesService.getTextureList(modelName.toString());
            if (textures == null || textures.isEmpty()) {
                return ResponseEntity.badRequest().body("No textures available");
            }
            
            int newTextureId = currentTextureId + 1;
            if (newTextureId > textures.size()) {
                newTextureId = 1;
            }
            
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> model = new HashMap<>();
            model.put("id", modelId + "-" + newTextureId);
            model.put("name", modelName);
            response.put("model", model);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * 更新皮肤缓存
     * GET /api/add
     */
    @GetMapping(value = "/add", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> updateTextures() {
        try {
            StringBuilder result = new StringBuilder();
            int modelCount = modelListService.getModelCount();
            
            for (int i = 1; i <= modelCount; i++) {
                Object modelName = modelListService.getIdToName(i);
                if (modelName instanceof String) {
                    String message = modelTexturesService.updateTextureCache(modelName.toString());
                    result.append("<p>").append(message).append("</p>");
                }
            }
            
            return ResponseEntity.ok(result.toString());
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("<p>Error: " + e.getMessage() + "</p>");
        }
    }
    
    /**
     * 加载模型配置
     */
    private ModelConfig loadModelConfig(Object modelName, int textureId) {
        try {
            String modelPathStr;
            if (modelName instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> modelGroup = (List<String>) modelName;
                modelPathStr = textureId > 0 && textureId <= modelGroup.size() 
                    ? modelGroup.get(textureId - 1) 
                    : modelGroup.get(0);
            } else {
                modelPathStr = modelName.toString();
            }
            
            File configFile = new File(modelPath, modelPathStr + "/index.json");
            if (!configFile.exists()) {
                return null;
            }
            
            String json = FileUtils.readFileToString(configFile, "UTF-8");
            ModelConfig config = objectMapper.readValue(json, ModelConfig.class);
            
            // 如果指定了皮肤ID，更新皮肤配置
            if (textureId > 0 && modelName instanceof String) {
                Object textureName = modelTexturesService.getTextureName(modelName.toString(), textureId);
                if (textureName != null) {
                    if (textureName instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<String> textureList = (List<String>) textureName;
                        config.setTextures(textureList);
                    } else {
                        config.setTextures(Arrays.asList(textureName.toString()));
                    }
                }
            }
            
            return config;
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to load model config", e);
        }
    }
    
    /**
     * 处理模型路径
     */
    private void processModelPaths(ModelConfig config, String modelName) {
        // 处理模型文件路径
        if (config.getModel() != null) {
            config.setModel("../" + modelPath + "/" + modelName + "/" + config.getModel());
        }
        
        // 处理皮肤路径
        if (config.getTextures() != null) {
            List<String> processedTextures = new ArrayList<>();
            for (String texture : config.getTextures()) {
                processedTextures.add("../" + modelPath + "/" + modelName + "/" + texture);
            }
            config.setTextures(processedTextures);
        }
        
        // 处理其他文件路径
        if (config.getPose() != null) {
            config.setPose("../" + modelPath + "/" + modelName + "/" + config.getPose());
        }
        
        if (config.getPhysics() != null) {
            config.setPhysics("../" + modelPath + "/" + modelName + "/" + config.getPhysics());
        }
        
        // 处理动作文件路径
        if (config.getMotions() != null) {
            config.getMotions().forEach((motionType, motionList) -> {
                motionList.forEach(motion -> {
                    if (motion.containsKey("file")) {
                        motion.put("file", "../" + modelPath + "/" + modelName + "/" + motion.get("file"));
                    }
                    if (motion.containsKey("sound")) {
                        motion.put("sound", "../" + modelPath + "/" + modelName + "/" + motion.get("sound"));
                    }
                });
            });
        }
        
        // 处理表情文件路径
        if (config.getExpressions() != null) {
            config.getExpressions().forEach((expressionType, expression) -> {
                if (expression.containsKey("file")) {
                    expression.put("file", "../" + modelPath + "/" + modelName + "/" + expression.get("file"));
                }
            });
        }
    }
}