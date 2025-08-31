package com.live2d.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.live2d.api.model.ModelList;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 模型列表服务类
 * 对应PHP版本的modelList.php功能
 */
@Service
public class ModelListService {
    
    @Value("${live2d.model.path:model}")
    private String modelPath;
    
    private ModelList modelList;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @PostConstruct
    public void init() {
        loadModelList();
    }
    
    /**
     * 加载模型列表配置
     */
    public void loadModelList() {
        try {
            File modelListFile = new File(modelPath, "model_list.json");
            if (modelListFile.exists()) {
                String json = FileUtils.readFileToString(modelListFile, "UTF-8");
                modelList = objectMapper.readValue(json, ModelList.class);
            } else {
                throw new RuntimeException("model_list.json not found in " + modelPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load model list", e);
        }
    }
    
    /**
     * 获取模型列表
     */
    public ModelList getModelList() {
        return modelList;
    }
    
    /**
     * 根据ID获取模型名称
     */
    public Object getIdToName(int id) {
        if (modelList == null) {
            loadModelList();
        }
        return modelList.getIdToName(id);
    }
    
    /**
     * 根据模型名称获取ID
     */
    public int getNameToId(String name) {
        if (modelList == null) {
            loadModelList();
        }
        return modelList.getNameToId(name);
    }
    
    /**
     * 获取模型总数
     */
    public int getModelCount() {
        if (modelList == null) {
            loadModelList();
        }
        return modelList.getModels().size();
    }
    
    /**
     * 获取指定索引的模型
     */
    public Object getModelByIndex(int index) {
        if (modelList == null) {
            loadModelList();
        }
        List<Object> models = modelList.getModels();
        if (index >= 0 && index < models.size()) {
            return models.get(index);
        }
        return null;
    }
    
    /**
     * 获取指定索引的消息
     */
    public String getMessageByIndex(int index) {
        if (modelList == null) {
            loadModelList();
        }
        List<String> messages = modelList.getMessages();
        if (index >= 0 && index < messages.size()) {
            return messages.get(index);
        }
        return "";
    }
}