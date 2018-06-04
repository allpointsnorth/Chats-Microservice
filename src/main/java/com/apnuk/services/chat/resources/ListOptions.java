/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.resources;

import com.apnuk.services.chat.utilities.transformations.NameValuePairs;

/**
 * Models a set of options that may be optionally applied to any of the
 * listing operations provided by the service.
 * @author gilesthompson
 */
public class ListOptions {
    
    private NameValuePairs filterByNameValuePairs;
    private NameValuePairs orderByNameValuePairs;
    private NameValuePairs paginationNameValuePairs;
    private boolean includeDeleted;
    private int resultsLimit;
    
    //pagination
    private boolean paginate; //defaults to true
    private int page;
    private int perPage;

    public ListOptions setFilterByNameValuePairs(NameValuePairs filterByNameValuePairs) {
        
        this.filterByNameValuePairs = filterByNameValuePairs;
        return this;
    }

    public ListOptions setOrderByNameValuePairs(NameValuePairs orderByNameValuePairs) {
        
        this.orderByNameValuePairs = orderByNameValuePairs;
        return this;
    }

    public ListOptions setPaginationNameValuePairs(NameValuePairs paginationNameValuePairs) {
        
        this.paginationNameValuePairs = paginationNameValuePairs;
        return this;
    }

    
    public ListOptions setIncludeDeleted(boolean includeDeleted) {
        
        this.includeDeleted = includeDeleted;
        return this;
    }
    
   
    public ListOptions setResultsLimit(int resultsLimit) {
        
        this.resultsLimit = resultsLimit;
        return this;
    }
    
        
    public ListOptions setPaginate(boolean paginate) {
        
        this.paginate = paginate;
        return this;
    }
    
    public ListOptions setPerPage(int perPage) {
        
        this.perPage = perPage;
        return this;
    }
    
    public ListOptions setPage(int page) {
        
        this.page = page;
        return this;
    }
   
    
    public NameValuePairs getFilterByNameValuePairs() {
        return filterByNameValuePairs;
    }

    public NameValuePairs getOrderByNameValuePairs() {
        return orderByNameValuePairs;
    }

    public NameValuePairs getPaginationNameValuePairs() {
        
        return paginationNameValuePairs;
    }

    public boolean isIncludeDeleted() {
        return includeDeleted;
    }

    public int getResultsLimit() {
        return resultsLimit;
    }

    public boolean isPaginate() {
        return paginate;
    }

    public int getPage() {
        return page;
    }

    public int getPerPage() {
        return perPage;
    }
    
    
    public static ListOptions newInst(){
        
        return new ListOptions();
    }
}
