package com.live2d.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 模型皮肤服务类
 * 对应PHP版本的modelTextures.php功能
 */
@Service
public class ModelTexturesService {
    
    @Value("${live2d.model.path:model}")
    private String modelPath;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 获取皮肤名称
     * @param modelName 模型名称
     * @param id 皮肤ID（从1开始）
     * @return 皮肤名称或皮肤名称数组
     */
    public Object getTextureName(String modelName, int id) {
        List<Object> textureList = getTextureList(modelName);
        if (textureList != null && id > 0 && id <= textureList.size()) {
            return textureList.get(id - 1);
        }
        return null;
    }
    
    /**
     * 获取皮肤列表缓存
     * @param modelName 模型名称
     * @return 皮肤列表
     */
    public List<Object> getTextureList(String modelName) {
        File modelDir = new File(modelPath, modelName);
        File cacheFile = new File(modelDir, "textures.cache");
        
        if (cacheFile.exists()) {
            try {
                String json = FileUtils.readFileToString(cacheFile, "UTF-8");
                return objectMapper.readValue(json, new TypeReference<List<Object>>() {});
            } catch (IOException e) {
                // 如果缓存文件损坏，重新生成
                return generateTextureCache(modelName);
            }
        } else {
            return generateTextureCache(modelName);
        }
    }
    
    /**
     * 生成皮肤缓存
     * @param modelName 模型名称
     * @return 皮肤列表
     */
    private List<Object> generateTextureCache(String modelName) {
        List<Object> textures = getTextures(modelName);
        if (textures != null && !textures.isEmpty()) {
            try {
                File modelDir = new File(modelPath, modelName);
                File cacheFile = new File(modelDir, "textures.cache");
                String json = objectMapper.writeValueAsString(textures);
                FileUtils.writeStringToFile(cacheFile, json, "UTF-8");
            } catch (IOException e) {
                throw new RuntimeException("Failed to write texture cache", e);
            }
        }
        return textures;
    }
    
    /**
     * 获取皮肤列表
     * @param modelName 模型名称
     * @return 皮肤列表
     */
    public List<Object> getTextures(String modelName) {
        File modelDir = new File(modelPath, modelName);
        File texturesOrderFile = new File(modelDir, "textures_order.json");
        
        if (texturesOrderFile.exists()) {
            // 多组皮肤组合模式
            return getTexturesWithOrder(modelName, texturesOrderFile);
        } else {
            // 单模型单皮肤模式
            return getSimpleTextures(modelName, modelDir);
        }
    }
    
    /**
     * 获取简单皮肤列表（单模型单皮肤模式）
     */
    private List<Object> getSimpleTextures(String modelName, File modelDir) {
        File texturesDir = new File(modelDir, "textures");
        if (!texturesDir.exists() || !texturesDir.isDirectory()) {
            return null;
        }
        
        File[] textureFiles = texturesDir.listFiles((dir, name) -> 
            name.toLowerCase().endsWith(".png") || 
            name.toLowerCase().endsWith(".jpg") || 
            name.toLowerCase().endsWith(".jpeg"));
        
        if (textureFiles == null) {
            return null;
        }
        
        return Arrays.stream(textureFiles)
                .map(file -> "textures/" + file.getName())
                .collect(Collectors.toList());
    }
    
    /**
     * 获取组合皮肤列表（多组皮肤组合模式）
     */
    private List<Object> getTexturesWithOrder(String modelName, File texturesOrderFile) {
        try {
            String json = FileUtils.readFileToString(texturesOrderFile, "UTF-8");
            List<List<String>> orderList = objectMapper.readValue(json, new TypeReference<List<List<String>>>() {});
            
            List<List<String>> allCombinations = new ArrayList<>();
            
            for (List<String> group : orderList) {
                List<List<String>> groupCombinations = new ArrayList<>();
                
                for (String textureDir : group) {
                    File dir = new File(modelPath, modelName + "/" + textureDir);
                    if (dir.exists() && dir.isDirectory()) {
                        File[] files = dir.listFiles((d, name) -> 
                            name.toLowerCase().endsWith(".png") || 
                            name.toLowerCase().endsWith(".jpg") || 
                            name.toLowerCase().endsWith(".jpeg"));
                        
                        if (files != null) {
                            for (File file : files) {
                                groupCombinations.add(Arrays.asList(textureDir + "/" + file.getName()));
                            }
                        }
                    }
                }
                
                allCombinations = arrayExhaustive(allCombinations, groupCombinations);
            }
            
            return allCombinations.stream()
                    .map(combination -> combination.stream()
                            .map(texture -> modelName + "/" + texture)
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to read textures_order.json", e);
        }
    }
    
    /**
     * 数组穷举合并（对应PHP版本的array_exhaustive方法）
     */
    private List<List<String>> arrayExhaustive(List<List<String>> arr1, List<List<String>> arr2) {
        List<List<String>> result = new ArrayList<>();
        
        if (arr1.isEmpty()) {
            result.addAll(arr2);
        } else {
            for (List<String> item1 : arr1) {
                for (List<String> item2 : arr2) {
                    List<String> combined = new ArrayList<>(item1);
                    combined.addAll(item2);
                    result.add(combined);
                }
            }
        }
        
        return result;
    }
    
    /**
     * 更新皮肤缓存
     * @param modelName 模型名称
     * @return 更新结果消息
     */
    public String updateTextureCache(String modelName) {
        File modelDir = new File(modelPath, modelName);
        File cacheFile = new File(modelDir, "textures.cache");
        
        if (!cacheFile.exists()) {
            generateTextureCache(modelName);
            return modelName + " / textures.cache / Created.";
        }
        
        try {
            // 获取当前缓存
            List<Object> currentTextures = getTextureList(modelName);
            
            // 获取新的皮肤列表
            List<Object> newTextures = getTextures(modelName);
            
            if (currentTextures == null || newTextures == null) {
                return modelName + " / textures.cache / No Update.";
            }
            
            // 比较差异
            Set<String> currentSet = currentTextures.stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet());
            
            Set<String> newSet = newTextures.stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet());
            
            if (currentSet.equals(newSet)) {
                return modelName + " / textures.cache / No Update.";
            } else {
                // 更新缓存
                String json = objectMapper.writeValueAsString(newTextures);
                FileUtils.writeStringToFile(cacheFile, json, "UTF-8");
                return modelName + " / textures.cache / Updated.";
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to update texture cache", e);
        }
    }
}