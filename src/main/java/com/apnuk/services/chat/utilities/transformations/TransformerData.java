/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.utilities.transformations;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Base TransformerData class, the concrete values returned from all Transformer classes will
 * be derived from this class.
 * @author gilesthompson
 */
public class TransformerData<T> {
    
    private List<T> data;
    
    private final Class<T> dataClass;
    
    /**
       Transformation meta-data that subclasses may optionally use to store
       data pertaining to the transformation operation.Each class will store
       there data to the map using their respective key; a list of currently
       stored keys may be obtained by calling getKeySet().
    */
    private Map<String,Map<String,String>> transformationMetaData;
    

    public TransformerData(List<T> dataToTransform,Class<T> dataClass) {
        
        this.data = dataToTransform;
        
        this.dataClass = dataClass;
        
        this.transformationMetaData = new HashMap<>();
        
        
    }
    
        

    public void setData(List<T> data) {
        this.data = data;
    }

    public Class<T> getDataClass() {
        return dataClass;
    }
    
    public List<T> getData() {
        return data;
    }
    
    
    public Map<String,String> getTransformationMetaData(String key){
        
        return this.transformationMetaData.get(key);
    }
    
    public Set<String> getKeySet(){
        
        return this.transformationMetaData.keySet();
    }
    
   
    void addTransformationMetaData(String metaDataKey,Map<String,String> metaData){
        
        this.transformationMetaData.put(metaDataKey,metaData);
    }
    
    
    public static String getPaginationMetaDataKey(){
        
        return "pagination";
    }
    
    
    
}
