/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.utilities.transformations;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Data Transformer from which all concrete DataTransformers are derived,
 * this class essentially provides dynamic, reflection-based class-to-fields map operations 
 * the result of which are stored to a static context and thus available to all 
 * DataTransformer instances created in the current Java Process.<br>
 * 
 * Filed map data is used by various concrete transformers like DataFilterer and DataSorter
 * to sort and filter data by specific fields as approptiate.<br>
 * 
 * Obtaining the fields of object instances via reflection is relatively expensive operation 
 * and thus it makes sense to only undertake this process once for each class and then cache
 * the results. 
 * @author gilesthompson
 */
public class AbstractDataTransformer {
    
    
    /** 
        Central point to store the results of all class-to-fields mapping operations undertaken
        in the current JVM process.All sub classes will be able to access the data contained
        in this map via the "protected" getFieldsMap() method which will actually implictly
        parse and store field data to this map for a class where the data does not already exist.
    */
    private static final Map<String,Map<String,Field>> CLASS_TO_FIELDS_MAP = new HashMap<>();
    
    
    
    
    /** 
        protected method, provides access to class field map data, will implicitly introspect 
        the runtime type of the provided object to extract and store field data where such
        data does not already exist in the internal map. 
     */
    protected Map<String,Field> getClassFieldMap(Class aClass){
        
         Map<String,Field> classToFieldsMap;
         String className = aClass.getName();
         synchronized(CLASS_TO_FIELDS_MAP){
             classToFieldsMap = CLASS_TO_FIELDS_MAP.get(className);
         }
         if(classToFieldsMap == null){
             
             classToFieldsMap = this.buildFieldMap(aClass);
             synchronized(CLASS_TO_FIELDS_MAP){
                 
                 CLASS_TO_FIELDS_MAP.put(className,classToFieldsMap);
             }
             
         }

         return classToFieldsMap;
    }
    
    
     /** 
        Introspect the class of the provided object types to extract reference to 
        all fields contained therein; the resulting fields are stored to a
        map under there corresponding names.
        NOTE: This operation is relatively expensive from a time-complexity stand point
        *     and thus the returned output should, where possible, be cached.
     */
    private Map<String,Field> buildFieldMap(Class aClass){
        
        Map<String,Field> classFields = new HashMap<>();
        
        Field[] classFieldsArr = aClass.getDeclaredFields();
        
        
        for(Field curField : classFieldsArr){
            
            curField.setAccessible(true);
            
            classFields.put(curField.getName(),curField);
        }
       
        
        return classFields;
    }
    
}
