package com.live2d.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * Live2D模型配置文件类
 * 对应每个模型目录下的index.json结构
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelConfig {
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("label")
    private String label;
    
    @JsonProperty("version")
    private String version;
    
    @JsonProperty("model")
    private String model;
    
    @JsonProperty("textures")
    private List<String> textures;
    
    @JsonProperty("layout")
    private Map<String, Object> layout;
    
    @JsonProperty("hit_areas_custom")
    private Map<String, List<Double>> hitAreasCustom;
    
    @JsonProperty("motions")
    private Map<String, List<Map<String, Object>>> motions;
    
    @JsonProperty("expressions")
    private Map<String, Map<String, String>> expressions;
    
    @JsonProperty("pose")
    private String pose;
    
    @JsonProperty("physics")
    private String physics;
    
    public ModelConfig() {}
    
    // Getters and Setters
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
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
    
    public Map<String, Object> getLayout() {
        return layout;
    }
    
    public void setLayout(Map<String, Object> layout) {
        this.layout = layout;
    }
    
    public Map<String, List<Double>> getHitAreasCustom() {
        return hitAreasCustom;
    }
    
    public void setHitAreasCustom(Map<String, List<Double>> hitAreasCustom) {
        this.hitAreasCustom = hitAreasCustom;
    }
    
    public Map<String, List<Map<String, Object>>> getMotions() {
        return motions;
    }
    
    public void setMotions(Map<String, List<Map<String, Object>>> motions) {
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