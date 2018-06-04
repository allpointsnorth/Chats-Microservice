/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The ServiceResponse that will be returned to the caller of the API
 * @author gilesthompson
 * @param <T> The response type wrapped by this service response, only applies
 *            where the call to the service method that returned this ServiceResponse
 *            instance actually returns some data and did not result in an error.
 */
public class ServiceResponse<T> implements Serializable {
    
     /** 
        The service response data payload, this will ONLY
        be used where the underlying service method returns some data.
     */
    private List<T> data;

    private String status;
    
    private String message;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String debugMessage;
    
    private transient boolean debugMsgEnabled;
    
    @JsonProperty("listing_options")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ListingOptions listingOptions;
    
    
        
    
     public ServiceResponse setMessage(String message) {
        this.message = message;
        return this;
    }
    

    public ServiceResponse setStatus(String status) {
        this.status = status;
        return this;
    }
    
    

    public String getStatus() {
        return status;
        
    }
    

    public String getMessage() {
        return message;
    }

    
    public List<T> getData() {
        return data;
    }

    public ServiceResponse setData(List<T> data) {
        this.data = data;
        return this;
        
    }
    
    /** Used where there is only a single data item. */
    public ServiceResponse setSingularData(T data){
        this.data = new ArrayList<>();
        this.data.add(data);
        return this;
    }
    

    public ServiceResponse setDebugMessage(String debugMessage) {
        
        if(this.debugMsgEnabled)
            this.debugMessage = debugMessage;
        
        return this;
        
    }
   
   
   
    public String getDebugMessage() {
        
        return debugMessage;
    
    }

    public ServiceResponse setListingOptions(ListingOptions listingOptions) {
        this.listingOptions = listingOptions;
        return this;
    }

    public ServiceResponse setDebugMsgEnabled(boolean debugMsgEnabled) {
        this.debugMsgEnabled = debugMsgEnabled;
        return this;
    }
   

    public ListingOptions getListingOptions() {
        return listingOptions;
    }
    
    

   

  

    
    
   /** 
       Models one or more optional request attributes that may be 
       specified with all requests received into the service; this
       will enable front-end client to optionally render visuals
       that pertain to the request that resulted in this service 
       response.For example where the response data has been filtered
       or ordered by one or parameters these may be displayed to the
       user.
   */
   public static class ListingOptions implements Serializable{
       
        /** specifies whether the user elected to limit results to a specified amount
            where not specified the value defaults to 0; note the Service resource
            may enforce a limit on results as necessary. 
         */
        @JsonProperty("results_limit")
        private int resultsLimit;
        
        /** 
            Specifies whether or not deleted entries was requested to be included in the 
            results, this defaults to FALSE where it is not specified. 
        */
        @JsonProperty("include_deleted")
        private boolean includeDeleted;
        
        
        /** 
            Denotes whether the caller requested for results to be ordered by 
            one or more attributes; where specified,sorting is applied in
            the order in which the parameters where specified. 
        */
        @JsonProperty("order_by")
        private String orderby;

        @JsonProperty("filter_by")
        private String filterBy;
        
        @JsonProperty("pagination_enabled")
        private boolean paginationEnabled;

        @JsonProperty("pagination_data")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Pagination pagination;
        
        public ListingOptions() {
        }
        

        public ListingOptions(int resultsLimit, boolean includeDeleted, String orderby, String filterBy, boolean paginateData, Pagination pagination) {
            
            this.resultsLimit = resultsLimit;
            this.includeDeleted = includeDeleted;
            this.orderby = orderby;
            this.filterBy = filterBy;
            this.paginationEnabled = paginateData;
            this.pagination = pagination;
            
        }

        public int getResultsLimit() {
            return resultsLimit;
        }

        public void setResultsLimit(int resultsLimit) {
            this.resultsLimit = resultsLimit;
        }

        public boolean isIncludeDeleted() {
            return includeDeleted;
        }

        public void setIncludeDeleted(boolean includeDeleted) {
            this.includeDeleted = includeDeleted;
        }

        public String getOrderby() {
            return orderby;
        }

        public void setOrderby(String orderby) {
            this.orderby = orderby;
        }

        public String getFilterBy() {
            return filterBy;
        }

        public void setFilterBy(String filterBy) {
            this.filterBy = filterBy;
        }
        
        

        public void setPaginationEnabled(boolean paginationEnabled) {
            this.paginationEnabled = paginationEnabled;
        }
        
        
        public boolean isPaginationEnabled() {
            return paginationEnabled;
        }
        
 
        public void setPagination(Pagination pagination) {
            this.pagination = pagination;
        }
        

        public Pagination getPagination() {
            return pagination;
        }
        
       
        
        public static ListingOptions newInst(int resultsLimit, boolean includeDeleted,String orderby,String filterBy, boolean paginateData, Pagination pagination){
            
            return new ListingOptions(resultsLimit,includeDeleted,orderby,filterBy,paginateData,pagination);
        }

        
        
        
        
       public static class Pagination implements Serializable{
        
        @JsonProperty("per_page")
        private int totalEntriesInPage;
        @JsonProperty("total")
        private int totalEntriesCount;
        @JsonProperty("total_pages")
        private int totalPagesCount;
        @JsonProperty("previous_page")
        private int previousPage;
        @JsonProperty("page")
        private int currentPage;
        @JsonProperty("next_page")
        private int nextPage;

        public Pagination(int totalEntriesInPage, int totalEntriesCount, int totalPagesCount, int previousPage, int currentPage, int nextPage) {
            this.totalEntriesInPage = totalEntriesInPage;
            this.totalEntriesCount = totalEntriesCount;
            this.totalPagesCount = totalPagesCount;
            this.previousPage = previousPage;
            this.currentPage = currentPage;
            this.nextPage = nextPage;
            
           
        }

        public int getTotalEntriesInPage() {
            return totalEntriesInPage;
        }

        public void setTotalEntriesInPage(int totalEntriesInPage) {
            this.totalEntriesInPage = totalEntriesInPage;
        }

        public int getTotalEntriesCount() {
            return totalEntriesCount;
        }

        public void setTotalEntriesCount(int totalEntriesCount) {
            this.totalEntriesCount = totalEntriesCount;
        }

        public int getTotalPagesCount() {
            return totalPagesCount;
        }

        public void setTotalPagesCount(int totalPagesCount) {
            this.totalPagesCount = totalPagesCount;
        }

        public void setPreviousPage(int previousPage) {
            this.previousPage = previousPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public int getNextPage() {
            return nextPage;
        }

        public int getPreviousPage() {
            return previousPage;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

 
        public static Pagination newInst(int totalEntriesInPage, int totalEntriesCount, int totalPagesCount, int previousPage, int currentPage, int nextPage){
            
            return new Pagination(totalEntriesInPage,totalEntriesCount,totalPagesCount,previousPage,currentPage,nextPage);
        }
    }        
     

   }
    
    
    
    
    
    


    
}
