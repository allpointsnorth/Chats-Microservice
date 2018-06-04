/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.utilities.transformations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A pagination-specific data transformer; provided with PaginationData this
 * transformer will return a subset of the provided data that equates to
 * the specified page providing, of course the specified page falls within
 * the bounds of the provided data set. (where this is not the case an
 * appropriate exception will be thrown)
 * @author gilesthompson
 * @param <DT> The DataType of the data to be paginated
 */
public class PaginationDataTransformer<DT> extends AbstractDataTransformer implements DataTransformer<DT>{

    
    private final TransformerData<DT> dataToTransform;
    
    private final NameValuePairs paginationNameValuePairs;
   

    public PaginationDataTransformer(TransformerData<DT> dataToTransform, NameValuePairs paginationNameValuePairs) {
        
        this.dataToTransform = dataToTransform;
        this.paginationNameValuePairs = paginationNameValuePairs;
    }
    
    

    public PaginationDataTransformer(DataTransformer delegate,NameValuePairs paginationNameValuePairs) {
        
        this.dataToTransform = delegate.transform(); //always call our delegate first and then operate on the returned data.
        this.paginationNameValuePairs = paginationNameValuePairs;
    }
    
    
   
    
    @Override
    public TransformerData<DT> transform() {
        
        //if no pagination data has been specified then we simply
        //return the data as-is
        if(paginationNameValuePairs == null)
            return this.dataToTransform;
        
        //delegate to our private helper method to actually paginate the data
        return this.paginateData(this.dataToTransform.getData(),
                                 this.paginationNameValuePairs.getNameValsMap(),
                                 this.dataToTransform.getDataClass());
    }
    
    /** 
        Actually computes and returns the specified page as denoted in the provided paginationAttributes map, the 
        map will contain the following keys: 
        page - the page the user would like to fetch; defaults to 1 if not specified<br>
        perPage - the number of results the user would like to display per page; defaults to 10 if not specified.
     */
    private TransformerData<DT> paginateData(List<DT> data,Map<String,List<String>> paginationAttributes,Class classReference){
        
        int targetPageNumber = Integer.parseInt(paginationAttributes.get("page").get(0));
        int maxPerPage = Integer.parseInt(paginationAttributes.get("perPage").get(0));
        
      
        int pageStartOffset = (targetPageNumber > 1) 
                            ? ((targetPageNumber * maxPerPage) - maxPerPage)
                            : 0;
        
        int pageEndOffset = ((pageStartOffset + maxPerPage) < data.size()) 
                          ? (pageStartOffset + maxPerPage)
                          : data.size();
        
        
        if(pageStartOffset > (data.size() - 1))
            throw new NullPointerException("The specified page does not exist.");
        
        
        List<DT> pageDataSubset  = data.subList(pageStartOffset,pageEndOffset);
        
        
        //compute total pages
        int totalPagesCount = data.size()/maxPerPage;
        
        //add 1 more additional page where there are one or more entries remaining as the result of 
        //a modulous operation
        totalPagesCount = ((data.size()%maxPerPage) > 0) ? totalPagesCount+1 : totalPagesCount;
        
        int totalEntriesCount = data.size();
        
        int previousPage = (targetPageNumber > 1) ? targetPageNumber-1 : -1;
        
        int nextPage = ((pageEndOffset) < data.size()) ? (targetPageNumber+1) : -1;
        
        //build PaginationData response.
        
        //set data to our paginated subset
        this.dataToTransform.setData(pageDataSubset);
        
        //set metadata.
        Map<String,String> metaData = new HashMap<>();
        metaData.put("totalEntries",Integer.toString(totalEntriesCount));
        metaData.put("maxPerPage",Integer.toString(maxPerPage));
        metaData.put("totalPages",Integer.toString(totalPagesCount));
        metaData.put("previousPage",Integer.toString(previousPage));
        metaData.put("currentPage",Integer.toString(targetPageNumber));
        metaData.put("nextPage",Integer.toString(nextPage));
       
      
        this.dataToTransform.addTransformationMetaData(TransformerData.getPaginationMetaDataKey(), metaData);
        
        
        return this.dataToTransform;
    }
    
}
