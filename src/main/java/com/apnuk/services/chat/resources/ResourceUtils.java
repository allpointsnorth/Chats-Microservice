/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.resources;

import com.apnuk.services.chat.utilities.transformations.NameValuePairs;
import java.util.HashMap;
import java.util.Map;

/**
 * Various utility methods that a RESTFul resource may use to help
 * facilitate the request/response cycle.
 * @author gilesthompson
 */
public class ResourceUtils {
    
    
    public static NameValuePairs  paginationParametersToNameValuePairs(String title,int page,int resultsPerPage){
        
        Map<String,String> pairs = new HashMap<>();
        pairs.put("page",Integer.toString(page));
        pairs.put("perPage",Integer.toString(resultsPerPage));
        
        NameValuePairs nvps = new NameValuePairs(title);
        nvps.addAllPairsAsStrings(pairs);
        return nvps;
        
    }
    
    public static NameValuePairs filterQueryStringToNameValuePairs(String title,String filterQueryString){
        
        //parameter attribute values are delimited by semi colons so we split at that literal 
        //to obtain a list of query attributes and there respective values.
        String[] queryParamValuePairs = filterQueryString.split(";");
        
        //next we split each pair into key/value components
        Map<String,String> paramKeyValuePairs = new HashMap<>();
        for(String currentKeyValuePair : queryParamValuePairs){
            
            String[] curPairKeyValueComp = currentKeyValuePair.split("=");
            paramKeyValuePairs.put(curPairKeyValueComp[0],curPairKeyValueComp[1]);
            
        }
        
        
        NameValuePairs nvps = new NameValuePairs(title);
        nvps.addAllPairsAsStrings(paramKeyValuePairs);
        return nvps;
    }
    
}
