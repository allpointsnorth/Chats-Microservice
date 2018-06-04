/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.utilities.transformations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to hold Transformer name/value pair data a single
 * name may be associated with multiple values.
 * @author gilesthompson
 */
public class NameValuePairs {
    
    /** The name of this name/value pair set, this may be used to
        denote the operation to which this data applies for
        example "SortAttributes" or "FilterArrtibutes".*/
    private final String title;
    
    private final Map<String,List<String>> nameValsMap;
    
  

    public NameValuePairs(String title) {
        
        this(title,null);
    }

    public NameValuePairs(String title,Map<String, List<String>> keyValuesMap) {
        
        this.title = title;
        this.nameValsMap = (keyValuesMap == null) ? new HashMap<>() : keyValuesMap;
       
    }
    
    public NameValuePairs addPair(String name,List<String> values){
        
        this.nameValsMap.put(name, values);
        return this;
    }
    
    public NameValuePairs addPair(String name, String... values){
        
        this.nameValsMap.put(name,Arrays.asList(values));
        return this;
    }
    
    public NameValuePairs addPair(String name, String commaDelimitedValues){
        
        String[] values = commaDelimitedValues.split(",");
        this.addPair(name,values);
        return this;
    }
    
    /** Expects multiple values for each key to be specified as comma-delimited strings. */
    public NameValuePairs addAllPairsAsStrings(Map<String,String> nameValuePairs){
        
        nameValuePairs.forEach((k,v)->{this.addPair(k,v);});
        return this;
    }
    
    public NameValuePairs addAllPairs(Map<String,List<String>> nameValuePairs){
        
        this.nameValsMap.putAll(nameValuePairs);
        return this;
    }

    public String getTitle() {
        
        return title;
    }

    public Map<String, List<String>> getNameValsMap() {
        
        return nameValsMap;
    }
    
    
    
    public static NameValuePairs newInst(String title){
        
        return new NameValuePairs(title);
    }
    
}
