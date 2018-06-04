/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.utilities.transformations;

import java.util.List;
import java.util.Map;

/**
 * A sorting specific DataTransformer.
 * @author gilesthompson
 * @param <DT> The Data Type to apply the transformation to.
 */
public class SortingDataTransformer<DT> extends AbstractDataTransformer implements DataTransformer<DT> {
    
    private final TransformerData<DT> dataToTransform;
    
    private final NameValuePairs filterNameValuePairs;
    
    public SortingDataTransformer(TransformerData<DT> dataToTransform, NameValuePairs filterNameValuePairs) {
        
        this.dataToTransform = dataToTransform;
        this.filterNameValuePairs = filterNameValuePairs;
    }
    
     public SortingDataTransformer(DataTransformer delegate,NameValuePairs nameValuePairs) {
        
        //call method on our delegate FIRST to obtain data to trnasform in this class.
        this.dataToTransform = delegate.transform();
        this.filterNameValuePairs = nameValuePairs;
        
    }

    @Override
    public TransformerData<DT> transform() {
        
        //if no sort data has been provided we simply return the data unchanged
        if(this.filterNameValuePairs == null)
            return this.dataToTransform;
        
        return this.sortData(this.dataToTransform.getData(), 
                             this.filterNameValuePairs.getNameValsMap(), 
                             this.dataToTransform.getDataClass());
    }
    
    

   private TransformerData<DT> sortData(List<DT> data,Map<String,List<String>> sortAttributes, Class classReference){
       
       /** 
           We simply wrap the sort facilities provided by the Collections framework 
           but additionally ad the ability to be able to chain sorts together. 
        */
      
       
       return null;
   }
    
    
}
