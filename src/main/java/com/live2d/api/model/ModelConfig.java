package com.live2d.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * Live2D模型配置文件类
 * 对应每个模型目录下的index.json结构
 */
public class ModelConfig {
    
    @JsonProperty("version")
    private String version;
    
    @JsonProperty("model")
    private String model;
    
    @JsonProperty("textures")
    private List<String> textures;
    
    @JsonProperty("layout")
    private Map<String, Double> layout;
    
    @JsonProperty("hit_areas_custom")
    private Map<String, List<Double>> hitAreasCustom;
    
    @JsonProperty("motions")
    private Map<String, List<Map<String, String>>> motions;
    
    @JsonProperty("expressions")
    private Map<String, Map<String, String>> expressions;
    
    @JsonProperty("pose")
    private String pose;
    
    @JsonProperty("physics")
    private String physics;
    
    public ModelConfig() {}
    
    // Getters and Setters
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public List<String> getTextures() {
        return textures;
    }
    
    public void setTextures(List<String> textures) {
        this.textures = textures;
    }
    
    public Map<String, Double> getLayout() {
        return layout;
    }
    
    public void setLayout(Map<String, Double> layout) {
        this.layout = layout;
    }
    
    public Map<String, List<Double>> getHitAreasCustom() {
        return hitAreasCustom;
    }
    
    public void setHitAreasCustom(Map<String, List<Double>> hitAreasCustom) {
        this.hitAreasCustom = hitAreasCustom;
    }
    
    public Map<String, List<Map<String, String>>> getMotions() {
        return motions;
    }
    
    public void setMotions(Map<String, List<Map<String, String>>> motions) {
        this.motions = motions;
    }
    
    public Map<String, Map<String, String>> getExpressions() {
        return expressions;
    }
    
    public void setExpressions(Map<String, Map<String, String>> expressions) {
        this.expressions = expressions;
    }
    
    public String getPose() {
        return pose;
    }
    
    public void setPose(String pose) {
        this.pose = pose;
    }
    
    public String getPhysics() {
        return physics;
    }
    
    public void setPhysics(String physics) {
        this.physics = physics;
    }
}