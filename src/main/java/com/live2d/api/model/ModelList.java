package com.live2d.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * 模型列表配置类
 * 对应PHP版本的model_list.json结构
 */
public class ModelList {
    
    @JsonProperty("models")
    private List<Object> models;
    
    @JsonProperty("messages")
    private List<String> messages;
    
    public ModelList() {}
    
    public ModelList(List<Object> models, List<String> messages) {
        this.models = models;
        this.messages = messages;
    }
    
    public List<Object> getModels() {
        return models;
    }
    
    public void setModels(List<Object> models) {
        this.models = models;
    }
    
    public List<String> getMessages() {
        return messages;
    }
    
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
    
    /**
     * 根据ID获取模型名称
     * @param id 模型ID（从1开始）
     * @return 模型名称或模型名称数组
     */
    public Object getIdToName(int id) {
        if (id < 1 || id > models.size()) {
            return null;
        }
        return models.get(id - 1);
    }
    
    /**
     * 根据模型名称获取ID
     * @param name 模型名称
     * @return 模型ID（从1开始），未找到返回-1
     */
    public int getNameToId(String name) {
        for (int i = 0; i < models.size(); i++) {
            Object model = models.get(i);
            if (model instanceof String && model.equals(name)) {
                return i + 1;
            } else if (model instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> modelGroup = (List<String>) model;
                if (modelGroup.contains(name)) {
                    return i + 1;
                }
            }
        }
        return -1;
    }
}