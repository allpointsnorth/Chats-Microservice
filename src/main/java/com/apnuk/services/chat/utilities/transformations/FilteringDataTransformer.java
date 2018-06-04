/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.utilities.transformations;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * Filtering specific DataTransformer, allows provided data to be arbitrarily 
 * filtered according to one or more Key-Value pairs.
 * @author gilesthompson
 * @param <DT> The data to be filtered.
 */
public class FilteringDataTransformer<DT> extends AbstractDataTransformer implements DataTransformer<DT>{

    
    private final TransformerData<DT> dataToTransform;
    
    private final NameValuePairs filterNameValuePairs;
   

    public FilteringDataTransformer(DataTransformer delegate,NameValuePairs nameValuePairs) {
        
        //call method on our delegate FIRST to obtain data to trnasform in this class.
        this.dataToTransform = delegate.transform();
        this.filterNameValuePairs = nameValuePairs;
        
    }
    

    public FilteringDataTransformer(TransformerData<DT> dataToFilter,NameValuePairs nameValuePairs) {
        
        this.dataToTransform = dataToFilter;
        this.filterNameValuePairs = nameValuePairs;
    }
    
    

  
    
    @Override
    public TransformerData<DT> transform() {
        
        //if no filter data has been provided we simply return the
        //the dataset as-is
        if(this.filterNameValuePairs == null)
            return this.dataToTransform;
        
        return this.filterData(this.dataToTransform.getData(),
                               filterNameValuePairs.getNameValsMap(),
                               this.dataToTransform.getDataClass());
        

    }
    
 
    
    private TransformerData<DT> filterData(List<DT> data,Map<String,List<String>> classFieldNameValuesPairs,Class classReference){
        
        
        //get field map data from our super class for the specified class
        Map<String,Field> classFieldMap = super.getClassFieldMap(classReference);
        
        
        //check that each entry has fields values matching our specified filters.
        List<DT> filteredData = data.stream()
                                    .filter(ce-> {return this.objectHasFieldValues(ce,classFieldMap,classFieldNameValuesPairs);})
                                    .collect(Collectors.toList());
        
        
        this.dataToTransform.setData(filteredData);
        
        return this.dataToTransform;
    }
    
    
    private boolean objectHasFieldValues(Object anObjectInstance,Map<String,Field> classFieldMap,Map<String,List<String>> targetClassFieldValues){
        
          /** iterate over target fields, look up the field by name and check its
              value on the provide instance, the first time we encounter a field
              that doesn't match we return false. Thus where we get to the end of
              the interactive process we can assume that the fields in question,
              for the provided instance match the target values specified.
              NOTE: where a field has multiple target value we do an EXCLUSIVE OR operation on each  value
                    and only return false where at least one value doesn't match */
          
          for(Entry<String,List<String>> entry :targetClassFieldValues.entrySet()){
          
                
                Field targetField = classFieldMap.get(entry.getKey());
                boolean found = false;
                if(targetField != null){
                    
                    for(String curTargetFieldValue : entry.getValue())
                        try {
                            if(targetField.get(anObjectInstance).toString().equals(curTargetFieldValue)){
                                found = true;
                                System.out.println("Current object field "+entry.getKey()+" value is: "+targetField.get(anObjectInstance).toString()+" and match equals: "+targetField.get(anObjectInstance).toString().equals(curTargetFieldValue) );
                                break;
                            }
                        } 
                        catch (IllegalArgumentException | IllegalAccessException ex) {
                            Logger.getLogger(FilteringDataTransformer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    
                    if(!found)
                        return false;
                }
                
                
          
          }

        return true;
        
    }
    
 
    
    
}
