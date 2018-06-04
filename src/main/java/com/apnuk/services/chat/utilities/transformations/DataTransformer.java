/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.utilities.transformations;



/**
 * Base Abstract Data Transformation interface that all DataTransformers 
 * will implement.
 * @author gilesthompson
 * @param <DT> DataType The type of data to transform.
 * 
 */
public interface DataTransformer<DT> {
    
    /**
     * Applies Transformation to the provided data and returns the result, this method
     * should generally be ONLY used by sub-classes which should as well as providing
     * an appropriate implementation should ALSO provide a higher-level utility-specific
     * method.For example the DataFilteere method provides a Filter() method which will
     * in-turn issue a call to this method internally.<BR>
     * 
     * Crucially, the fact that all Transformers internally have a transform() method
     * allows the caller to chain one or more transformers together (via the decorator pattern)
     * to realise multiple data transforms on a dataset
     * @param metaData The data to apply to the transformation.
     * @return TD The transformed data.
     */
    public TransformerData<DT> transform();
    
    
    
    
    
  
    
}
