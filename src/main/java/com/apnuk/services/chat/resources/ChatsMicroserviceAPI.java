/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.resources;




import com.apnuk.services.chat.dtos.Affiliate;
import com.apnuk.services.chat.dtos.Category;
import com.apnuk.services.chat.dtos.Chat;
import com.apnuk.services.chat.dtos.Settings;
import com.apnuk.services.chat.dtos.Keyword;
import com.apnuk.services.chat.dtos.Network;
import com.apnuk.services.chat.dtos.NoteName;
import com.apnuk.services.chat.dtos.Promo;
import com.apnuk.services.chat.dtos.Route;
import com.apnuk.services.chat.dtos.Scheme;
import com.apnuk.services.chat.dtos.Template;
import com.apnuk.services.chat.dtos.TemplateEntry;
import com.apnuk.services.chat.exceptions.ChatsServiceException;
import com.apnuk.services.chat.resources.ServiceResponse.ListingOptions;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.apnuk.services.chat.services.ChatsService;

import com.apnuk.services.chat.utilities.transformations.TransformerData;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 Client-facing RESTful Chat service interface (API), internal (i.e other services)
 and external clients may use this API to obtain access to and manage Chat 
 specific data.
 @author gilesthompson
 */
@RestController
//@RequestMapping("/api/v1/chatproviders")
@RequestMapping("/chats")
public class ChatsMicroserviceAPI {
    
    @Autowired
    private ChatsService chatsService;
    
    private final boolean debugMessagingEnabled = true;
    
    public ChatsMicroserviceAPI() {
        
    }
    
    @RequestMapping(method = GET)
    @ApiOperation(value = "Gets all Chats currently registered in the system.", notes = "Gets all Chat currently registered in the system. Results will be paginated by default; users may interactively select the number of records they would like to view per gae via the \"per_page\" query parameter.  See the Chat schema for more information.  ",tags={""})
    public ResponseEntity<ServiceResponse<Chat>> listChats(@ApiParam(name="per_page", value="The number of entires to display per page, default to 20 if not specified")
                                                           @RequestParam(value ="per_page", required = false) Integer perPage,
                                                           @ApiParam(name="page", value="The number of the page to fetch, defaults to 1 if not specified")
                                                           @RequestParam(value = "page", required = false) Integer page,
                                                           @ApiParam(name="paginate", value="Toggles pagination off/on defaults to TRUE if not specified.")
                                                           @RequestParam(value = "paginate", required = false) String paginate,
                                                           @ApiParam(name="include_deleted", value="indicates whether or not deleted entries should be included in in the listings, defaults to FALSE if not specified.")
                                                           @RequestParam(value = "include_deleted", required = false) boolean includeDeleted,
                                                           @ApiParam(name="results_limit", value="the maximum number of results to return, defaults to 1000 if not specified, may be set to an arbitrarily large value to return all records.")
                                                           @RequestParam(value = "results_limit", required = false) Integer resultsLimit,
                                                           @ApiParam(name="filter_by", value="an optional set of name value pairs that a request may be filtered by.")
                                                           @RequestParam(value = "filter_by", required = false) String filterByParams,
                                                           HttpServletRequest request){
        
        
        
        
         try {
             
             
             //TODO This should be done with pagination enabled in the final build providing
             //ofcourse the "paginate" query parameter has been set to TRUE.
             
             //build list options from supplied query parameters..
             ListOptions listOptions = ListOptions.newInst()
                                                  .setIncludeDeleted(includeDeleted)
                                                  .setResultsLimit((resultsLimit == null) ? 0 : resultsLimit);
                                                 
             
             //set filters
             if(filterByParams != null)
                listOptions.setFilterByNameValuePairs(ResourceUtils.filterQueryStringToNameValuePairs("filters", filterByParams));
             
             //set pagination
             boolean paginateEnabled = (paginate == null || paginate.equalsIgnoreCase("true"));
             
             if(paginateEnabled)
                 listOptions.setPaginationNameValuePairs(ResourceUtils.paginationParametersToNameValuePairs("pagination",(page == null || page < 1) ? 1: page,(perPage == null || perPage < 1) ? 10 : perPage));
             
                 
             
             
             TransformerData<Chat> allChatsData = this.chatsService.listChats(listOptions);
             
             //build response....
             
             //if pagination is enabled prepare pagination meta data, this will be ultimately appended to the 
             //ListingOptions instance which will be in turn appended to the ServiceResponse.
             ListingOptions.Pagination paginationMetaData = null;
             if(paginateEnabled){
              
                 Map<String,String> pageMetaMap = allChatsData.getTransformationMetaData(TransformerData.getPaginationMetaDataKey());
                 
                 paginationMetaData = ListingOptions.Pagination
                                                    .newInst(Integer.parseInt(pageMetaMap.get("maxPerPage")),
                                                             Integer.parseInt(pageMetaMap.get("totalEntries")), 
                                                             Integer.parseInt(pageMetaMap.get("totalPages")), 
                                                             Integer.parseInt(pageMetaMap.get("previousPage")), 
                                                             Integer.parseInt(pageMetaMap.get("currentPage")), 
                                                             Integer.parseInt(pageMetaMap.get("nextPage")));
                 
             }
             
             
             
             //construct ServiceResponse...
             ServiceResponse<Chat> serviceResponse = new ServiceResponse<>();
             serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat data.")
                            .setData(allChatsData.getData())
                            .setListingOptions(ListingOptions.newInst(listOptions.getResultsLimit(), 
                                                                      listOptions.isIncludeDeleted(),
                                                                      null,
                                                                      filterByParams,
                                                                      paginateEnabled,
                                                                      paginationMetaData));
                            

             //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
             
         }
        
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chats.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to list chats",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chats.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    
        
        
    }
    
    
    
   
    @ApiOperation(value = "Gets a Chat by its assigned id.", notes = "Gets a Chat by its assigned id. See the Chat schema for more information.  ",tags={""})
    @RequestMapping(value = "/{id}", method = GET)
    public ResponseEntity<ServiceResponse<Chat>> getChatById(@ApiParam(name="id", value="The id of the Chat to fetch.")
                                                             @PathVariable("id") int id,
                                                             @ApiParam(name="include_deleted", value="indicates whether or not deleted entries should be included in in the listings, defaults to FALSE if not specified.")
                                                             @RequestParam(value = "include_deleted", required = false) boolean includeDeleted){
        
        
        try {
            
            Chat targetChat = this.chatsService.getChatById(id,includeDeleted);
            
            ServiceResponse<Chat> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat data.")
                            .setSingularData(targetChat)
                            .setListingOptions(ListingOptions.newInst(0, 
                                                                      includeDeleted,
                                                                      null,
                                                                      null,
                                                                      false,
                                                                      null));
            
            //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
        }
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error response to client
             //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to get chat "
                    + "with the specified id. The chat could not be found",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
        
        
    }
    
    
    
    
    @ApiOperation(value = "Creates new Chat.", notes = "Creates new Chat, the id of the newly created Chat is then duly returned to the caller.  ",tags={""})
    @RequestMapping(method = POST)
    public ResponseEntity<ServiceResponse<String>> createChat(@ApiParam(name="chatProvider", value="The Chat to add to the system.")  
                                                              @RequestBody(required = false) Chat chat){
        
        
        try {
            
            int assignedChatId = this.chatsService.createChat(chat);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully created new Chat.")
                           .setSingularData(Integer.toString(assignedChatId));
                           
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
                           
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to create chat.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to create chat.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
        
      
    }
    
    
    
    @ApiOperation(value = "Updates the Chat with the specified id.", notes = "Updates the Chat with the specified id, the id of the updated Chat is then returned.  ",tags={""})
    @RequestMapping(value = "/{id}",method = PUT)
    public ResponseEntity<ServiceResponse<String>> updateChat(@ApiParam(name="chatProvider", value="The Chat to update to the system.")
                                                                      @RequestBody(required = false) Chat chat,
                                                                      @ApiParam(name="id", value="The id of the Chat to update.")
                                                                      @PathVariable("id") int id){
        
        
        try {

            if(id != chat.getId())
                return (ResponseEntity)this.buildErrorServiceResponse("Id mismatch.",null,HttpStatus.BAD_REQUEST);
                
            
            int updatedChatId = this.chatsService.updateChat(chat);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully updated chat.")
                           .setSingularData(Integer.toString(updatedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to update chat.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to update chat.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
         
         
         
    }
    
    
    
    @ApiOperation(value = "Deletes the Chat with the specified id.", notes = "Deletes the Chat with the specified id. Its is important to note that entries are only ever \"soft deleted\" that is marked as deleted rather than actually removed from the system. The id of the deleted entry is returned.",tags={""})
    @RequestMapping(value = "/{id}",method = DELETE)
    public ResponseEntity<ServiceResponse<String>> deleteChat(@ApiParam(name="id", value="The id of the Chat to delete.")  
                                                              @PathVariable("id") int id){
        
        
        
        try {
            
            int idOfDeletedChat = this.chatsService.deleteChat(id);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully deleted chat.")
                           .setSingularData(Integer.toString(idOfDeletedChat));
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to delete chat.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to delete chat.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
        
        
         
         
    }
    
    
    
    
    
    
                                    //Chat >> General Settings
    
    
   @ApiOperation(value = "Adds anew Settings to the Chat with the specified id.", notes = "Adds a new Settings to the Chat with the specified id; any existing settings will be overriden in the process.",tags={""})
   @RequestMapping(value = "/{id}/settings",method = POST)
   public ResponseEntity<ServiceResponse<String>> createChatTemplate(@ApiParam(name="settings", value="The settings to associate with the chat.")
                                                                     @RequestBody(required = false) Settings settings,
                                                                     @ApiParam(name="id", value="The id of the Chat to associate the settings with.")
                                                                     @PathVariable("id") int id){
        
        
         try {
            
            int assignedChatId = this.chatsService.createChatSettings(id,settings);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully created chat Settings")
                           .setSingularData(Integer.toString(assignedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
                           
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to create chat settings.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat settings.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to create chat settings.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat settings.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
   
    
    @ApiOperation(value = "Gets the GeneralSettings for the Chat with the specified id.", notes = "Gets the GeneralSettings for the Chat with the specified id. See the GeneralSettings schema for more information.  ",tags={""})
    @RequestMapping(value = "/{id}/settings", method = GET)
    public ResponseEntity<ServiceResponse<Settings>> getChatSettings(@ApiParam(name="id", value="The id of the Chat to fetch general settings for.")
                                                                     @PathVariable("id") int id){
        
        
        try {
            
            Settings settings = this.chatsService.getChatSettings(id);
            ServiceResponse<Settings> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully fetched chat settings data.")
                           .setSingularData(settings);
                           
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
         catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to get chat settingds.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to get chat settings.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to get chat settings.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to get chat settings.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
        
        
        
    }
    
    
    
    @ApiOperation(value = "Updates GeneralSettings for Chat with the specified id.", notes = "Updates GeneralSettings for the Chat with the specified id, the id of the updated Chat is then returned.  ",tags={""})
    @RequestMapping(value = "/{id}/settings",method = PUT)
    public ResponseEntity<ServiceResponse<String>> updateChatSettings(@ApiParam(name="generalSettings", value="The updated GeneralSettings to associate with the CharProvider ")
                                                                                     @RequestBody(required = false) Settings generalSettings,
                                                                                     @ApiParam(name="id", value="The id of the Chat that the updated GeneralSettings applies to.")
                                                                                     @PathVariable("id") int id){
        
        
        
        try {

            
  
            int updatedChatId = this.chatsService.updateChatSettings(id,generalSettings);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully updated chat settings.")
                           .setSingularData(Integer.toString(updatedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to update chat settings.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to update chat settings.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
         
    }
    
    
   
    
    
    
                                        //Chat >> Templates
    
    @ApiOperation(value = "Lists all Templates associated with the Chat with the specified id.", notes = "Lists all Templates associated with the Chat with the specified id.. See the Template schema for more information.  ",tags={""})
    @RequestMapping(value = "/{id}/templates", method = GET)
    public ResponseEntity<ServiceResponse<Template>> listChatTemplates(@ApiParam(name="id", value="The id of the Chat to fetch Templates for.")
                                                                       @PathVariable("id") int id,
                                                                       @RequestParam(value ="per_page", required = false) Integer perPage,
                                                                       @ApiParam(name="page", value="The number of the page to fetch, defaults to 1 if not specified")
                                                                       @RequestParam(value = "page", required = false) Integer page,
                                                                       @ApiParam(name="paginate", value="Toggles pagination off/on defaults to TRUE if not specified.")
                                                                       @RequestParam(value = "paginate", required = false) String paginate,
                                                                       @ApiParam(name="include_deleted", value="indicates whether or not deleted entries should be included in in the listings, defaults to FALSE if not specified.")
                                                                       @RequestParam(value = "include_deleted", required = false) boolean includeDeleted,
                                                                       @ApiParam(name="results_limit", value="the maximum number of results to return, defaults to 1000 if not specified, may be set to an arbitrarily large value to return all records.")
                                                                       @RequestParam(value = "results_limit", required = false) Integer resultsLimit,
                                                                       @ApiParam(name="filter_by", value="an optional set of name value pairs that a request may be filtered by.")
                                                                       @RequestParam(value = "filter_by", required = false) String filterByParams){ 
        
        
        
        
        
        try {
             
             
             //TODO This should be done with pagination enabled in the final build providing
             //ofcourse the "paginate" query parameter has been set to TRUE.
             
             //build list options from supplied query parameters..
             ListOptions listOptions = ListOptions.newInst()
                                                  .setIncludeDeleted(includeDeleted)
                                                  .setResultsLimit((resultsLimit == null) ? 0 : resultsLimit);
                                                 
             
             //set filters
             if(filterByParams != null)
                listOptions.setFilterByNameValuePairs(ResourceUtils.filterQueryStringToNameValuePairs("filters", filterByParams));
             
             //set pagination
             boolean paginateEnabled = (paginate == null || paginate.equalsIgnoreCase("true"));
             
             if(paginateEnabled)
                 listOptions.setPaginationNameValuePairs(ResourceUtils.paginationParametersToNameValuePairs("pagination",(page == null || page < 1) ? 1: page,(perPage == null || perPage < 1) ? 10 : perPage));
             
                 
             
             
             TransformerData<Template> allTemplatesData = this.chatsService.listChatTemplates(id,listOptions);
             
             //build response....
             
             //if pagination is enabled prepare pagination meta data, this will be ultimately appended to the 
             //ListingOptions instance which will be in turn appended to the ServiceResponse.
             ListingOptions.Pagination paginationMetaData = null;
             if(paginateEnabled){
              
                 Map<String,String> pageMetaMap = allTemplatesData.getTransformationMetaData(TransformerData.getPaginationMetaDataKey());
                 
                 paginationMetaData = ListingOptions.Pagination
                                                    .newInst(Integer.parseInt(pageMetaMap.get("maxPerPage")),
                                                             Integer.parseInt(pageMetaMap.get("totalEntries")), 
                                                             Integer.parseInt(pageMetaMap.get("totalPages")), 
                                                             Integer.parseInt(pageMetaMap.get("previousPage")), 
                                                             Integer.parseInt(pageMetaMap.get("currentPage")), 
                                                             Integer.parseInt(pageMetaMap.get("nextPage")));
                 
             }
             
             
             
             //construct ServiceResponse...
             ServiceResponse<Template> serviceResponse = new ServiceResponse<>();
             serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Template data.")
                            .setData(allTemplatesData.getData())
                            .setListingOptions(ListingOptions.newInst(listOptions.getResultsLimit(), 
                                                                      listOptions.isIncludeDeleted(),
                                                                      null,
                                                                      filterByParams,
                                                                      paginateEnabled,
                                                                      paginationMetaData));
                            

             //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
             
         }
        
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat templates.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to list chat templates",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat templates.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
   }
    
    
   @ApiOperation(value = "Gets Template with the provided id.", notes = "Gets the Template with the provided id associated with the Chat with the specified id. See the Template schema for more information.  ",tags={""})
   @RequestMapping(value = "/{id}/templates/{templateId}", method = GET)
   public ResponseEntity<ServiceResponse<Template>> getChatTemplateById(@ApiParam(name="id", value="The id of the Chat to fetch Templates for.")
                                                                        @PathVariable("id") int id,
                                                                        @ApiParam(name="templateId", value="The id of the Template to fetch.")
                                                                        @PathVariable("templateId") int templateId){
       
       
      
       try {
            
            Template targetChatTemplate = this.chatsService.getChatTemplate(id,templateId);
            
            ServiceResponse<Template> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Template data.")
                            .setSingularData(targetChatTemplate);
                            
            
            //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
        }
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error response to client
             //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat template.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to get chat template "
                    + "with the specified id.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat template.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
   }
    
    
    
   @ApiOperation(value = "Adds a new Chat Template to the system.", notes = "Adds a new template to the system and associates it with the Chat with the specified id. The id of the newly created Template is then returned to the caller.  ",tags={""})
   @RequestMapping(value = "/{id}/templates",method = POST)
   public ResponseEntity<ServiceResponse<String>> createChatTemplate(@ApiParam(name="template", value="The new Chat Template to add to the system.")
                                                                             @RequestBody(required = false) Template template,
                                                                             @ApiParam(name="id", value="The id of the Chat to associate the template with.")
                                                                             @PathVariable("id") int id){
        
        
         try {
            
            int assignedChatId = this.chatsService.createChatTemplate(id,template);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully created chat template")
                           .setSingularData(Integer.toString(assignedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
                           
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to create chat template.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat template.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to create chat template.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat template.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
   
   
   
    @ApiOperation(value = "Updates Template with the specified id.", notes = "Updates Template with the provided id associated with the Chat with the specified id, the id of the updated Template is then returned to the caller.  ",tags={""})
    @RequestMapping(value = "/{id}/templates/{templateId}",method = PUT)
    public ResponseEntity<ServiceResponse<String>> updateChatTemplate(@ApiParam(name="template", value="The updated template to associate with the Chat.")
                                                                              @RequestBody(required = false) Template template,
                                                                              @ApiParam(name="id", value="The id of the Chat that the updated Template applies to.")
                                                                              @PathVariable("id") int id,
                                                                              @ApiParam(name="templateId", value="The id of the Template to be updated.")
                                                                              @PathVariable("templateId") int templateId){
        
        
         try {

            
  
            int updatedChatId = this.chatsService.updateChatTemplate(id,template);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully updated Chat Template")
                           .setSingularData(Integer.toString(updatedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to update chat template.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat template.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to update chat template.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat template.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    @ApiOperation(value = "Deletes Template with the specified id.", notes = "Deletes Template with the provided id associated with the Chat with the specified id, the id of the deleted Template is then returned to the caller. It is important to note that the template is only \"soft deleted\" and thus not actually removed from the system. ",tags={""})
    @RequestMapping(value = "/{id}/templates/{templateId}",method = DELETE)
    public ResponseEntity<ServiceResponse<String>> deleteChatTemplate(@ApiParam(name="id", value="The id of the Chat that the Template to be deleted applies to.")
                                                                      @PathVariable("id") int id,
                                                                      @ApiParam(name="templateId", value="The id of the Template to be deleted.")
                                                                      @PathVariable("templateId") int templateId){
        
        
         try {
            
            int idOfDeletedChat = this.chatsService.deleteChatTemplate(id,templateId);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully deleted Chat Template with the specified id.")
                           .setSingularData(Integer.toString(idOfDeletedChat));
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to delete chat template.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat template.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to delete chat template.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat template.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    @ApiOperation(value = "List all entries associated with the Template with the specified id.", notes = "Lists all entries associated with the Template with the provided id associated with the Chat with the specified id.",tags={""})
    @RequestMapping(value = "/{id}/templates/{templateId}/entries",method = GET)
    public ResponseEntity<ServiceResponse<TemplateEntry>> listChatTemplateEntries(@ApiParam(name="id", value="The id of the Chat associated with the Template that entries should be listed for.")
                                                                                  @PathVariable("id") int id,
                                                                                  @ApiParam(name="templateId", value="The id of the Template that entries should be listed for.")
                                                                                  @PathVariable("templateId") int templateId,
                                                                                  @RequestParam(value ="per_page", required = false) Integer perPage,
                                                                                  @ApiParam(name="page", value="The number of the page to fetch, defaults to 1 if not specified")
                                                                                  @RequestParam(value = "page", required = false) Integer page,
                                                                                  @ApiParam(name="paginate", value="Toggles pagination off/on defaults to TRUE if not specified.")
                                                                                  @RequestParam(value = "paginate", required = false) String paginate,
                                                                                  @ApiParam(name="include_deleted", value="indicates whether or not deleted entries should be included in in the listings, defaults to FALSE if not specified.")
                                                                                  @RequestParam(value = "include_deleted", required = false) boolean includeDeleted,
                                                                                  @ApiParam(name="results_limit", value="the maximum number of results to return, defaults to 1000 if not specified, may be set to an arbitrarily large value to return all records.")
                                                                                  @RequestParam(value = "results_limit", required = false) Integer resultsLimit,
                                                                                  @ApiParam(name="filter_by", value="an optional set of name value pairs that a request may be filtered by.")
                                                                                  @RequestParam(value = "filter_by", required = false) String filterByParams){
        
        
        
        
        
        
        try {
             
             
             //TODO This should be done with pagination enabled in the final build providing
             //ofcourse the "paginate" query parameter has been set to TRUE.
             
             //build list options from supplied query parameters..
             ListOptions listOptions = ListOptions.newInst()
                                                  .setIncludeDeleted(includeDeleted)
                                                  .setResultsLimit((resultsLimit == null) ? 0 : resultsLimit);
                                                 
             
             //set filters
             if(filterByParams != null)
                listOptions.setFilterByNameValuePairs(ResourceUtils.filterQueryStringToNameValuePairs("filters", filterByParams));
             
             //set pagination
             boolean paginateEnabled = (paginate == null || paginate.equalsIgnoreCase("true"));
             
             if(paginateEnabled)
                 listOptions.setPaginationNameValuePairs(ResourceUtils.paginationParametersToNameValuePairs("pagination",(page == null || page < 1) ? 1: page,(perPage == null || perPage < 1) ? 10 : perPage));
             
                 
             
             
             TransformerData<TemplateEntry> allTemplateEntryData = this.chatsService.listChatTemplateEntries(id,templateId,listOptions);
             
             //build response....
             
             //if pagination is enabled prepare pagination meta data, this will be ultimately appended to the 
             //ListingOptions instance which will be in turn appended to the ServiceResponse.
             ListingOptions.Pagination paginationMetaData = null;
             if(paginateEnabled){
              
                 Map<String,String> pageMetaMap = allTemplateEntryData.getTransformationMetaData(TransformerData.getPaginationMetaDataKey());
                 
                 paginationMetaData = ListingOptions.Pagination
                                                    .newInst(Integer.parseInt(pageMetaMap.get("maxPerPage")),
                                                             Integer.parseInt(pageMetaMap.get("totalEntries")), 
                                                             Integer.parseInt(pageMetaMap.get("totalPages")), 
                                                             Integer.parseInt(pageMetaMap.get("previousPage")), 
                                                             Integer.parseInt(pageMetaMap.get("currentPage")), 
                                                             Integer.parseInt(pageMetaMap.get("nextPage")));
                 
             }
             
             
             
             //construct ServiceResponse...
             ServiceResponse<TemplateEntry> serviceResponse = new ServiceResponse<>();
             serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Template Entry data.")
                            .setData(allTemplateEntryData.getData())
                            .setListingOptions(ListingOptions.newInst(listOptions.getResultsLimit(), 
                                                                      listOptions.isIncludeDeleted(),
                                                                      null,
                                                                      filterByParams,
                                                                      paginateEnabled,
                                                                      paginationMetaData));
                            

             //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
             
         }
        
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat template entry.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to list chat template entry",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat template entry.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
         
    }
    
    
    
    @ApiOperation(value = "Appends new entry to the Template with the specified id.", notes = "Appends new entry to the Template with the specified id associated with the Chat with the specified id. The id of the newly create Entry is returned to the caller",tags={""})
    @RequestMapping(value = "/{id}/templates/{templateId}/entries",method = POST)
    public ResponseEntity<ServiceResponse<String>> addChatTemplateEntry(@ApiParam(name="templateEntry", value="The entry to append to the Template.")
                                                                        @RequestBody(required = false) TemplateEntry templateEntry,
                                                                        @ApiParam(name="id", value="The id of the Chat associated with the Template that entry should be append to.")
                                                                        @PathVariable("id") int id,
                                                                        @ApiParam(name="templateId", value="The id of the Template that entry should be append to.")
                                                                        @PathVariable("templateId") int templateId){
        
        
          try {
            
            int assignedChatId = this.chatsService.createChatTemplateEntry(id,templateId,templateEntry);
            
            ServiceResponse<TemplateEntry> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully created Chat Template Entry")
                           .setSingularData(Integer.toString(assignedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
                           
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to create chat template entry.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat template.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to create chat template entry.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat template entry.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    @ApiOperation(value = "Updates the TemplateEntry with the specified id.", notes = "Updates entry with the specified id associated with the Template with the specified id. The id of the updated Entry is returned to the caller",tags={""})
    @RequestMapping(value = "/{id}/templates/{templateId}/entries/{templateEntryId}",method = PUT)
    public ResponseEntity<ServiceResponse<String>> updateChatTemplateEntry(@ApiParam(name="templateEntry", value="The updated template entry.")
                                                                                   @RequestBody(required = false) TemplateEntry templateEntry,
                                                                                   @ApiParam(name="id", value="The id of the Chat associated with the Template that THE entry should be updated to.")
                                                                                   @PathVariable("id") int id,
                                                                                   @ApiParam(name="templateId", value="The id of the Template that the entry to be updated relates to.")
                                                                                   @PathVariable("templateId") int templateId,
                                                                                   @ApiParam(name="templateEntryId", value="The id of the TemplateEntry that should be updated.")
                                                                                   @PathVariable("templateEntryId") int templateEntryId){
        
        
        
        try {

            
  
            int updatedChatId = this.chatsService.updateChatTemplateEntry(id,templateId,templateEntry);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully updated Chat Template Entry.")
                           .setSingularData(Integer.toString(updatedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to update chat template.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat template.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to update chat template.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat template.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
         
    }
    
    
    
    @ApiOperation(value = "Deletes the TemplateEntry with the specified id.", notes = "Deletes the TemplateEntry with the specified id associated with the Template with the specified id. The id of the deleted Entry is returned to the caller",tags={""})
    @RequestMapping(value = "/{id}/templates/{templateId}/entries/{templateEntryId}",method = DELETE)
    public ResponseEntity<ServiceResponse<String>> deleteChatTemplateEntry(@ApiParam(name="id", value="The id of the Chat associated with the Template that entry should be deleted from.")
                                                                           @PathVariable("id") int id,
                                                                           @ApiParam(name="templateId", value="The id of the Template that the entry should be deleted from")
                                                                           @PathVariable("templateId") int templateId,
                                                                           @ApiParam(name="templateEntryId", value="The id of the TemplateEntry that should be deleted")
                                                                           @PathVariable("templateEntryId") int templateEntryId){
        
        
        try {
            
            int idOfDeletedChat = this.chatsService.deleteChatTemplateEntry(id,templateId,templateEntryId);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully deleted Chat Template Entry.")
                           .setSingularData(Integer.toString(idOfDeletedChat));
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to delete chat template.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat template.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to delete chat template.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat template.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    
    
                                                                //Chat >> Networks
    
    
    @ApiOperation(value = "Lists all Networks associated with the Chat with the specified id.", notes = "Lists all Networks associated with the Chat with the specified id.. See the Network schema for more information.  ",tags={""})
    @RequestMapping(value = "/{id}/networks", method = GET)
    public ResponseEntity<ServiceResponse<Network>> getChatNetworks(@ApiParam(name="id", value="The id of the Chat to fetch Networks for.")
                                                                    @PathVariable("id") int id,
                                                                    @RequestParam(value ="per_page", required = false) Integer perPage,
                                                                    @ApiParam(name="page", value="The number of the page to fetch, defaults to 1 if not specified")
                                                                    @RequestParam(value = "page", required = false) Integer page,
                                                                    @ApiParam(name="paginate", value="Toggles pagination off/on defaults to TRUE if not specified.")
                                                                    @RequestParam(value = "paginate", required = false) String paginate,
                                                                    @ApiParam(name="include_deleted", value="indicates whether or not deleted entries should be included in in the listings, defaults to FALSE if not specified.")
                                                                    @RequestParam(value = "include_deleted", required = false) boolean includeDeleted,
                                                                    @ApiParam(name="results_limit", value="the maximum number of results to return, defaults to 1000 if not specified, may be set to an arbitrarily large value to return all records.")
                                                                    @RequestParam(value = "results_limit", required = false) Integer resultsLimit,
                                                                    @ApiParam(name="filter_by", value="an optional set of name value pairs that a request may be filtered by.")
                                                                    @RequestParam(value = "filter_by", required = false) String filterByParams){
        
        
        
        try {
             
             
             //TODO This should be done with pagination enabled in the final build providing
             //ofcourse the "paginate" query parameter has been set to TRUE.
             
             //build list options from supplied query parameters..
             ListOptions listOptions = ListOptions.newInst()
                                                  .setIncludeDeleted(includeDeleted)
                                                  .setResultsLimit((resultsLimit == null) ? 0 : resultsLimit);
                                                 
             
             //set filters
             if(filterByParams != null)
                listOptions.setFilterByNameValuePairs(ResourceUtils.filterQueryStringToNameValuePairs("filters", filterByParams));
             
             //set pagination
             boolean paginateEnabled = (paginate == null || paginate.equalsIgnoreCase("true"));
             
             if(paginateEnabled)
                 listOptions.setPaginationNameValuePairs(ResourceUtils.paginationParametersToNameValuePairs("pagination",(page == null || page < 1) ? 1: page,(perPage == null || perPage < 1) ? 10 : perPage));
             
                 
             
             
             TransformerData<Network> allNetworksData = this.chatsService.listChatNetworks(id,listOptions);
             
             //build response....
             
             //if pagination is enabled prepare pagination meta data, this will be ultimately appended to the 
             //ListingOptions instance which will be in turn appended to the ServiceResponse.
             ListingOptions.Pagination paginationMetaData = null;
             if(paginateEnabled){
              
                 Map<String,String> pageMetaMap = allNetworksData.getTransformationMetaData(TransformerData.getPaginationMetaDataKey());
                 
                 paginationMetaData = ListingOptions.Pagination
                                                    .newInst(Integer.parseInt(pageMetaMap.get("maxPerPage")),
                                                             Integer.parseInt(pageMetaMap.get("totalEntries")), 
                                                             Integer.parseInt(pageMetaMap.get("totalPages")), 
                                                             Integer.parseInt(pageMetaMap.get("previousPage")), 
                                                             Integer.parseInt(pageMetaMap.get("currentPage")), 
                                                             Integer.parseInt(pageMetaMap.get("nextPage")));
                 
             }
             
             
             
             //construct ServiceResponse...
             ServiceResponse<Network> serviceResponse = new ServiceResponse<>();
             serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Network data.")
                            .setData(allNetworksData.getData())
                            .setListingOptions(ListingOptions.newInst(listOptions.getResultsLimit(), 
                                                                      listOptions.isIncludeDeleted(),
                                                                      null,
                                                                      filterByParams,
                                                                      paginateEnabled,
                                                                      paginationMetaData));
                            

             //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
             
         }
        
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat templates.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to list chat templates",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat templates.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
   }
    
    
   @ApiOperation(value = "Adds a new Chat Network to the system.", notes = "Adds a new Network to the system and associates it with the Chat with the specified id. The id of the newly created Network is then returned to the caller.  ",tags={""})
   @RequestMapping(value = "/{id}/networks",method = POST)
   public ResponseEntity<ServiceResponse<String>> createChatNetwork(@ApiParam(name="network", value="The new Chat Network to add to the system.")
                                                                            @RequestBody(required = false) Network network,
                                                                            @ApiParam(name="id", value="The id of the Chat to associate the Network with.")
                                                                            @PathVariable("id") int id){
        
        
         try {
            
            int assignedChatId = this.chatsService.createChatNetwork(id,network);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully created Chat Network")
                           .setSingularData(Integer.toString(assignedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
                           
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to create chat network.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat network.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to create chat network.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat network.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
   
   
   
    @ApiOperation(value = "Updates the Network with the specified id.", notes = "Updates the Network with the provided id associated with the Chat with the specified id, the id of the updated Network is then returned to the caller.  ",tags={""})
    @RequestMapping(value = "/{id}/networks/{networkId}",method = PUT)
    public ResponseEntity<ServiceResponse<String>> updateChatNetwork(@ApiParam(name="network", value="The updated network to associate with the Chat.")
                                                                             @RequestBody(required = false) Network network,
                                                                             @ApiParam(name="id", value="The id of the Chat that the updated Network applies to.")
                                                                             @PathVariable("id") int id,
                                                                             @ApiParam(name="networkId", value="The id of the Network to be updated.")
                                                                             @PathVariable("networkId") int networkId){
        
        
         try {

            
  
            int updatedChatId = this.chatsService.updateChatNetwork(id,network);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully updated Chat Network.")
                           .setSingularData(Integer.toString(updatedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to update chat network.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat network.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to update chat network.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat network.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    @ApiOperation(value = "Deletes the Network with the specified id.", notes = "Deletes the Network with the provided id associated with the Chat with the specified id, the id of the deleted Network is then returned to the caller. It is important to note that the Network is only \"soft deleted\" and thus not actually removed from the system. ",tags={""})
    @RequestMapping(value = "/{id}/networks/{networkId}",method = DELETE)
    public ResponseEntity<ServiceResponse<String>> deleteChatNetwork(@ApiParam(name="id", value="The id of the Chat that the Network to be deleted applies to.")
                                                                     @PathVariable("id") int id,
                                                                     @ApiParam(name="networkId", value="The id of the Network to be deleted.")
                                                                     @PathVariable("networkId") int networkId){
        
        
        try {
            
            int idOfDeletedChat = this.chatsService.deleteChatNetwork(id,networkId);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully deleted Chat Network.")
                           .setSingularData(Integer.toString(idOfDeletedChat));
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to delete chat network.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat template.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to delete chat network.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat network.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    
    
   
                                                                //Chat >> Routes
    
    
    @ApiOperation(value = "Lists all Routes associated with the Chat with the specified id.", notes = "Lists all Routes associated with the Chat with the specified id.. See the Route schema for more information.  ",tags={""})
    @RequestMapping(value = "/{id}/routes", method = GET)
    public ResponseEntity<ServiceResponse<Route>> getChatRoutes(@ApiParam(name="id", value="The id of the Chat to fetch Routes for.")
                                                                @PathVariable("id") int id,
                                                                @RequestParam(value ="per_page", required = false) Integer perPage,
                                                                @ApiParam(name="page", value="The number of the page to fetch, defaults to 1 if not specified")
                                                                @RequestParam(value = "page", required = false) Integer page,
                                                                @ApiParam(name="paginate", value="Toggles pagination off/on defaults to TRUE if not specified.")
                                                                @RequestParam(value = "paginate", required = false) String paginate,
                                                                @ApiParam(name="include_deleted", value="indicates whether or not deleted entries should be included in in the listings, defaults to FALSE if not specified.")
                                                                @RequestParam(value = "include_deleted", required = false) boolean includeDeleted,
                                                                @ApiParam(name="results_limit", value="the maximum number of results to return, defaults to 1000 if not specified, may be set to an arbitrarily large value to return all records.")
                                                                @RequestParam(value = "results_limit", required = false) Integer resultsLimit,
                                                                @ApiParam(name="filter_by", value="an optional set of name value pairs that a request may be filtered by.")
                                                                @RequestParam(value = "filter_by", required = false) String filterByParams){
        
        
        try {
             
             
             //TODO This should be done with pagination enabled in the final build providing
             //ofcourse the "paginate" query parameter has been set to TRUE.
             
             //build list options from supplied query parameters..
             ListOptions listOptions = ListOptions.newInst()
                                                  .setIncludeDeleted(includeDeleted)
                                                  .setResultsLimit((resultsLimit == null) ? 0 : resultsLimit);
                                                 
             
             //set filters
             if(filterByParams != null)
                listOptions.setFilterByNameValuePairs(ResourceUtils.filterQueryStringToNameValuePairs("filters", filterByParams));
             
             //set pagination
             boolean paginateEnabled = (paginate == null || paginate.equalsIgnoreCase("true"));
             
             if(paginateEnabled)
                 listOptions.setPaginationNameValuePairs(ResourceUtils.paginationParametersToNameValuePairs("pagination",(page == null || page < 1) ? 1: page,(perPage == null || perPage < 1) ? 10 : perPage));
             
                 
             
             
             TransformerData<Route> allRoutesData = this.chatsService.listChatRoutes(id,listOptions);
             
             //build response....
             
             //if pagination is enabled prepare pagination meta data, this will be ultimately appended to the 
             //ListingOptions instance which will be in turn appended to the ServiceResponse.
             ListingOptions.Pagination paginationMetaData = null;
             if(paginateEnabled){
              
                 Map<String,String> pageMetaMap = allRoutesData.getTransformationMetaData(TransformerData.getPaginationMetaDataKey());
                 
                 paginationMetaData = ListingOptions.Pagination
                                                    .newInst(Integer.parseInt(pageMetaMap.get("maxPerPage")),
                                                             Integer.parseInt(pageMetaMap.get("totalEntries")), 
                                                             Integer.parseInt(pageMetaMap.get("totalPages")), 
                                                             Integer.parseInt(pageMetaMap.get("previousPage")), 
                                                             Integer.parseInt(pageMetaMap.get("currentPage")), 
                                                             Integer.parseInt(pageMetaMap.get("nextPage")));
                 
             }
             
             
             
             //construct ServiceResponse...
             ServiceResponse<Route> serviceResponse = new ServiceResponse<>();
             serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Route data.")
                            .setData(allRoutesData.getData())
                            .setListingOptions(ListingOptions.newInst(listOptions.getResultsLimit(), 
                                                                      listOptions.isIncludeDeleted(),
                                                                      null,
                                                                      filterByParams,
                                                                      paginateEnabled,
                                                                      paginationMetaData));
                            

             //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
             
         }
        
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat routes.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to list chat routes",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat routes.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
   }
    
    
   @ApiOperation(value = "Adds a new Chat Route to the system.", notes = "Adds a new Route to the system and associates it with the Chat with the specified id. The id of the newly created Route is then returned to the caller.  ",tags={""})
   @RequestMapping(value = "/{id}/routes",method = POST)
   public ResponseEntity<ServiceResponse<String>> createChatRoutes(@ApiParam(name="route", value="The new Chat Route to add to the system.")
                                                                   @RequestBody(required = false) Route route,
                                                                   @ApiParam(name="id", value="The id of the Chat to associate the Route with.")
                                                                   @PathVariable("id") int id){
        
        
         try {
            
            int assignedChatId = this.chatsService.createChatRoute(id,route);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully created Chat Route")
                           .setSingularData(Integer.toString(assignedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
                           
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to create chat route.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat route.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to create chat route.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat network.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
   
   
   
    @ApiOperation(value = "Updates the Route with the specified id.", notes = "Updates the Route with the provided id associated with the Chat with the specified id, the id of the updated Route is then returned to the caller.  ",tags={""})
    @RequestMapping(value = "/{id}/routes/{routeId}",method = PUT)
    public ResponseEntity<ServiceResponse<String>> updateChatRoutes(@ApiParam(name="route", value="The updated route to associate with the Chat.")
                                                                    @RequestBody(required = false) Route route,
                                                                    @ApiParam(name="id", value="The id of the Chat that the updated Route applies to.")
                                                                    @PathVariable("id") int id,
                                                                    @ApiParam(name="routeId", value="The id of the Route to be updated.")
                                                                    @PathVariable("routeId") int routeId){
        
        
         try {

            
  
            int updatedChatId = this.chatsService.updateChatRoute(id,route);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully updated Chat Route.")
                           .setSingularData(Integer.toString(updatedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to update chat route.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat route.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to update chat route.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat route.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    @ApiOperation(value = "Deletes the Route with the specified id.", notes = "Deletes the Route with the provided id associated with the Chat with the specified id, the id of the deleted Route is then returned to the caller. It is important to note that the Route is only \"soft deleted\" and thus not actually removed from the system. ",tags={""})
    @RequestMapping(value = "/{id}/routes/{routeId}",method = DELETE)
    public ResponseEntity<ServiceResponse<String>> deleteChatRoute(@ApiParam(name="id", value="The id of the Chat that the Route to be deleted applies to.")
                                                                   @PathVariable("id") int id,
                                                                   @ApiParam(name="routeId", value="The id of the Route to be deleted.")
                                                                   @PathVariable("routeId") int routeId){
        
        
       
        try {
            
            int idOfDeletedChat = this.chatsService.deleteChatRoute(id,routeId);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully deleted Chat Route.")
                           .setSingularData(Integer.toString(idOfDeletedChat));
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to delete chat route.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat route.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to delete chat route.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat route.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    
    @ApiOperation(value = "Gets the Route with the specified id.", notes = "Gets the Route with the provided id associated with the Chat with the specified id.  ",tags={""})
    @RequestMapping(value = "/{id}/routes/{routeId}",method = GET)
    public ResponseEntity<ServiceResponse<Route>> getChatRouteById(@ApiParam(name="id", value="The id of the Chat that the Route applies to.")
                                                                   @PathVariable("id") int id,
                                                                   @ApiParam(name="routeId", value="The id of the Route to fetch.")
                                                                   @PathVariable("routeId") int routeId){
        
        
         try {
            
            Route targetChatRoute = this.chatsService.getChatRoute(id,routeId);
            
            ServiceResponse<Route> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Route data.")
                            .setSingularData(targetChatRoute);
                            
            
            //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
        }
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error response to client
             //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat template.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to get chat template "
                    + "with the specified id.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat template.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    
    
   
    
                                                                //Chat >> Keywords
    
    
    @ApiOperation(value = "Lists all Keywords associated with the Chat with the specified id.", notes = "Lists all Keywords associated with the Chat with the specified id.. See the Keyword schema for more information.  ",tags={""})
    @RequestMapping(value = "/{id}/keywords", method = GET)
    public ResponseEntity<ServiceResponse<Keyword>> getChatKeywords(@ApiParam(name="id", value="The id of the Chat to fetch Keywords for.")
                                                                    @PathVariable("id") int id,
                                                                    @RequestParam(value ="per_page", required = false) Integer perPage,
                                                                    @ApiParam(name="page", value="The number of the page to fetch, defaults to 1 if not specified")
                                                                    @RequestParam(value = "page", required = false) Integer page,
                                                                    @ApiParam(name="paginate", value="Toggles pagination off/on defaults to TRUE if not specified.")
                                                                    @RequestParam(value = "paginate", required = false) String paginate,
                                                                    @ApiParam(name="include_deleted", value="indicates whether or not deleted entries should be included in in the listings, defaults to FALSE if not specified.")
                                                                    @RequestParam(value = "include_deleted", required = false) boolean includeDeleted,
                                                                    @ApiParam(name="results_limit", value="the maximum number of results to return, defaults to 1000 if not specified, may be set to an arbitrarily large value to return all records.")
                                                                    @RequestParam(value = "results_limit", required = false) Integer resultsLimit,
                                                                    @ApiParam(name="filter_by", value="an optional set of name value pairs that a request may be filtered by.")
                                                                    @RequestParam(value = "filter_by", required = false) String filterByParams){
        
        
        
        
        try {
             
             
             //TODO This should be done with pagination enabled in the final build providing
             //ofcourse the "paginate" query parameter has been set to TRUE.
             
             //build list options from supplied query parameters..
             ListOptions listOptions = ListOptions.newInst()
                                                  .setIncludeDeleted(includeDeleted)
                                                  .setResultsLimit((resultsLimit == null) ? 0 : resultsLimit);
                                                 
             
             //set filters
             if(filterByParams != null)
                listOptions.setFilterByNameValuePairs(ResourceUtils.filterQueryStringToNameValuePairs("filters", filterByParams));
             
             //set pagination
             boolean paginateEnabled = (paginate == null || paginate.equalsIgnoreCase("true"));
             
             if(paginateEnabled)
                 listOptions.setPaginationNameValuePairs(ResourceUtils.paginationParametersToNameValuePairs("pagination",(page == null || page < 1) ? 1: page,(perPage == null || perPage < 1) ? 10 : perPage));
             
                 
             
             
             TransformerData<Keyword> allKeywordsData = this.chatsService.listChatKeywords(id,listOptions);
             
             //build response....
             
             //if pagination is enabled prepare pagination meta data, this will be ultimately appended to the 
             //ListingOptions instance which will be in turn appended to the ServiceResponse.
             ListingOptions.Pagination paginationMetaData = null;
             if(paginateEnabled){
              
                 Map<String,String> pageMetaMap = allKeywordsData.getTransformationMetaData(TransformerData.getPaginationMetaDataKey());
                 
                 paginationMetaData = ListingOptions.Pagination
                                                    .newInst(Integer.parseInt(pageMetaMap.get("maxPerPage")),
                                                             Integer.parseInt(pageMetaMap.get("totalEntries")), 
                                                             Integer.parseInt(pageMetaMap.get("totalPages")), 
                                                             Integer.parseInt(pageMetaMap.get("previousPage")), 
                                                             Integer.parseInt(pageMetaMap.get("currentPage")), 
                                                             Integer.parseInt(pageMetaMap.get("nextPage")));
                 
             }
             
             
             
             //construct ServiceResponse...
             ServiceResponse<Keyword> serviceResponse = new ServiceResponse<>();
             serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Keyword data.")
                            .setData(allKeywordsData.getData())
                            .setListingOptions(ListingOptions.newInst(listOptions.getResultsLimit(), 
                                                                      listOptions.isIncludeDeleted(),
                                                                      null,
                                                                      filterByParams,
                                                                      paginateEnabled,
                                                                      paginationMetaData));
                            

             //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
             
         }
        
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat keywords.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to list chat keywords",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat keywords.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
   }
    
    
   @ApiOperation(value = "Adds a new Chat Keyword to the system.", notes = "Adds a new Keyword to the system and associates it with the Chat with the specified id. The id of the newly created Keyword is then returned to the caller.  ",tags={""})
   @RequestMapping(value = "/{id}/keywords",method = POST)
   public ResponseEntity<ServiceResponse<String>> createChatKeyword(@ApiParam(name="keyword", value="The new Chat keyword to add to the system.")
                                                                    @RequestBody(required = false) Keyword keyword,
                                                                    @ApiParam(name="id", value="The id of the Chat to associate the Keyword with.")
                                                                    @PathVariable("id") int id){
        
        
         try {
            
            int assignedChatId = this.chatsService.createChatKeyword(id,keyword);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully created Chat Keyword")
                           .setSingularData(Integer.toString(assignedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
                           
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to create chat keyword", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat keyword.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to create chat keyword.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat keyword.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
   
   
   
    @ApiOperation(value = "Updates the Keyword with the specified id.", notes = "Updates the Keyword with the provided id associated with the Chat with the specified id, the id of the updated Keyword is then returned to the caller.  ",tags={""})
    @RequestMapping(value = "/{id}/keywords/{keywordId}",method = PUT)
    public ResponseEntity<ServiceResponse<String>> updateChatKeyword(@ApiParam(name="keyword", value="The updated keyword to associate with the Chat.")
                                                                     @RequestBody(required = false) Keyword keyword,
                                                                     @ApiParam(name="id", value="The id of the Chat that the updated keyword applies to.")
                                                                     @PathVariable("id") int id,
                                                                     @ApiParam(name="keywordId", value="The id of the Keyword to be updated.")
                                                                     @PathVariable("keywordId") int keywordId){
        
        
          try {

            
  
            int updatedChatId = this.chatsService.updateChatKeyword(id,keyword);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully updated Chat Keyword.")
                           .setSingularData(Integer.toString(updatedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to update chat keyword.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat keyword.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to update chat keyword.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat keyword.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
     @ApiOperation(value = "Deletes the Keyword with the specified id.", notes = "Deletes the Keyword with the provided id associated with the Chat with the specified id, the id of the deleted Keyword is then returned to the caller. It is important to note that the Keyword is only \"soft deleted\" and thus not actually removed from the system. ",tags={""})
    @RequestMapping(value = "/{id}/keywords/{keywordId}",method = DELETE)
    public ResponseEntity<ServiceResponse<String>> deleteChatKeyword(@ApiParam(name="id", value="The id of the Chat that the Keyword to be deleted applies to.")
                                                                     @PathVariable("id") int id,
                                                                     @ApiParam(name="keywordId", value="The id of the Keyword to be deleted.")
                                                                     @PathVariable("keywordId") int keywordId){
        
        
          
        try {
            
            int idOfDeletedChat = this.chatsService.deleteChatKeyword(id,keywordId);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully deleted Chat Keyword.")
                           .setSingularData(Integer.toString(idOfDeletedChat));
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to delete chat keyword.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat template.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to delete chat keyword.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to delete chat keyword.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    
    @ApiOperation(value = "Gets the Keyword with the specified id.", notes = "Gets the Keyword with the provided id associated with the Chat with the specified id.  ",tags={""})
    @RequestMapping(value = "/{id}/keywords/{keywordId}",method = GET)
    public ResponseEntity<ServiceResponse<Keyword>> getChatKeywordById(@ApiParam(name="id", value="The id of the Chat that the Keyword applies to.")
                                                                       @PathVariable("id") int id,
                                                                       @ApiParam(name="keywordId", value="The id of the Keyword to fetch.")
                                                                       @PathVariable("keywordId") int keywordId){
        
        
         try {
            
            Keyword targetChatKeyword = this.chatsService.getChatKeyword(id,keywordId);
            
            ServiceResponse<Keyword> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Keyword data.")
                            .setSingularData(targetChatKeyword);
                            
            
            //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
        }
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error response to client
             //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat keyword.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to get chat keyword "
                    + "with the specified id.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat keyword.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    
    
    
                                
                                                          
    
    
   
                                                                //Chat >> NoteNames
    
    
    @ApiOperation(value = "Lists all NoteNames associated with the Chat with the specified id.", notes = "Lists all NoteNames associated with the Chat with the specified id.. See the NoteName schema for more information.  ",tags={""})
    @RequestMapping(value = "/{id}/notenames", method = GET)
    public ResponseEntity<ServiceResponse<NoteName>> getChatNoteNames(@ApiParam(name="id", value="The id of the Chat to fetch NoteNames for.")
                                                                      @PathVariable("id") int id,
                                                                      @RequestParam(value ="per_page", required = false) Integer perPage,
                                                                      @ApiParam(name="page", value="The number of the page to fetch, defaults to 1 if not specified")
                                                                      @RequestParam(value = "page", required = false) Integer page,
                                                                      @ApiParam(name="paginate", value="Toggles pagination off/on defaults to TRUE if not specified.")
                                                                      @RequestParam(value = "paginate", required = false) String paginate,
                                                                      @ApiParam(name="include_deleted", value="indicates whether or not deleted entries should be included in in the listings, defaults to FALSE if not specified.")
                                                                      @RequestParam(value = "include_deleted", required = false) boolean includeDeleted,
                                                                      @ApiParam(name="results_limit", value="the maximum number of results to return, defaults to 1000 if not specified, may be set to an arbitrarily large value to return all records.")
                                                                      @RequestParam(value = "results_limit", required = false) Integer resultsLimit,
                                                                      @ApiParam(name="filter_by", value="an optional set of name value pairs that a request may be filtered by.")
                                                                      @RequestParam(value = "filter_by", required = false) String filterByParams){
        
        
         try {
             
             
             //TODO This should be done with pagination enabled in the final build providing
             //ofcourse the "paginate" query parameter has been set to TRUE.
             
             //build list options from supplied query parameters..
             ListOptions listOptions = ListOptions.newInst()
                                                  .setIncludeDeleted(includeDeleted)
                                                  .setResultsLimit((resultsLimit == null) ? 0 : resultsLimit);
                                                 
             
             //set filters
             if(filterByParams != null)
                listOptions.setFilterByNameValuePairs(ResourceUtils.filterQueryStringToNameValuePairs("filters", filterByParams));
             
             //set pagination
             boolean paginateEnabled = (paginate == null || paginate.equalsIgnoreCase("true"));
             
             if(paginateEnabled)
                 listOptions.setPaginationNameValuePairs(ResourceUtils.paginationParametersToNameValuePairs("pagination",(page == null || page < 1) ? 1: page,(perPage == null || perPage < 1) ? 10 : perPage));
             
                 
             
             
             TransformerData<NoteName> allKeywordsData = this.chatsService.listChatNoteNames(id,listOptions);
             
             //build response....
             
             //if pagination is enabled prepare pagination meta data, this will be ultimately appended to the 
             //ListingOptions instance which will be in turn appended to the ServiceResponse.
             ListingOptions.Pagination paginationMetaData = null;
             if(paginateEnabled){
              
                 Map<String,String> pageMetaMap = allKeywordsData.getTransformationMetaData(TransformerData.getPaginationMetaDataKey());
                 
                 paginationMetaData = ListingOptions.Pagination
                                                    .newInst(Integer.parseInt(pageMetaMap.get("maxPerPage")),
                                                             Integer.parseInt(pageMetaMap.get("totalEntries")), 
                                                             Integer.parseInt(pageMetaMap.get("totalPages")), 
                                                             Integer.parseInt(pageMetaMap.get("previousPage")), 
                                                             Integer.parseInt(pageMetaMap.get("currentPage")), 
                                                             Integer.parseInt(pageMetaMap.get("nextPage")));
                 
             }
             
             
             
             //construct ServiceResponse...
             ServiceResponse<NoteName> serviceResponse = new ServiceResponse<>();
             serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Keyword data.")
                            .setData(allKeywordsData.getData())
                            .setListingOptions(ListingOptions.newInst(listOptions.getResultsLimit(), 
                                                                      listOptions.isIncludeDeleted(),
                                                                      null,
                                                                      filterByParams,
                                                                      paginateEnabled,
                                                                      paginationMetaData));
                            

             //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
             
         }
        
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat keywords.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to list chat keywords",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat keywords.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
   }
    
    
   @ApiOperation(value = "Adds a new Chat NoteName to the system.", notes = "Adds a new NoteName to the system and associates it with the Chat with the specified id. The id of the newly created NoteName is then returned to the caller.  ",tags={""})
   @RequestMapping(value = "/{id}/notenames",method = POST)
   public ResponseEntity<ServiceResponse<String>> createChatNoteName(@ApiParam(name="notename", value="The new Chat notename to add to the system.")
                                                                     @RequestBody(required = false) NoteName notename,
                                                                     @ApiParam(name="id", value="The id of the Chat to associate the NoteName with.")
                                                                     @PathVariable("id") int id){
        
        
         try {
            
            int assignedChatId = this.chatsService.createChatNoteName(id,notename);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully created Chat NoteName")
                           .setSingularData(Integer.toString(assignedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
                           
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to create chat notename", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat keyword.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to create chat notename.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat notename.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
   
   
   
    @ApiOperation(value = "Updates the NoteName with the specified id.", notes = "Updates the NoteName with the provided id associated with the Chat with the specified id, the id of the updated NoteName is then returned to the caller.  ",tags={""})
    @RequestMapping(value = "/{id}/notenames/{notenameId}",method = PUT)
    public ResponseEntity<ServiceResponse<String>> updateChatNoteName(@ApiParam(name="notename", value="The updated notename to associate with the Chat.")
                                                                      @RequestBody(required = false) NoteName notename,
                                                                      @ApiParam(name="id", value="The id of the Chat that the updated notename applies to.")
                                                                      @PathVariable("id") int id,
                                                                      @ApiParam(name="notenameId", value="The id of the NoteName to be updated.")
                                                                      @PathVariable("notenameId") int notenameId){
        
        
         try {

            
  
            int updatedChatId = this.chatsService.updateChatNoteName(id,notename);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully updated Chat NoteName.")
                           .setSingularData(Integer.toString(updatedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to update chat NoteName.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat NoteName.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to update chat NoteName.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat NoteName.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    @ApiOperation(value = "Deletes the NoteName with the specified id.", notes = "Deletes the NoteName with the provided id associated with the Chat with the specified id, the id of the deleted NoteName is then returned to the caller. It is important to note that the NoteName is only \"soft deleted\" and thus not actually removed from the system. ",tags={""})
    @RequestMapping(value = "/{id}/notenames/{notenameId}",method = DELETE)
    public ResponseEntity<ServiceResponse<String>> deleteChatNoteName(@ApiParam(name="id", value="The id of the Chat that the NoteName to be deleted applies to.")
                                                                      @PathVariable("id") int id,
                                                                      @ApiParam(name="notenameId", value="The id of the NoteName to be deleted.")
                                                                      @PathVariable("notenameId") int notenameId){
        
        
         try {
            
            int idOfDeletedChat = this.chatsService.deleteChatNoteName(id,notenameId);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully deleted Chat NoteName.")
                           .setSingularData(Integer.toString(idOfDeletedChat));
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to delete chat note name.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat template.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to delete chat note name.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to delete chat note name.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    
    
    
    
    @ApiOperation(value = "Gets the NoteName with the specified id.", notes = "Gets the NoteName with the provided id associated with the Chat with the specified id.  ",tags={""})
    @RequestMapping(value = "/{id}/notenames/{notenameId}",method = GET)
    public ResponseEntity<ServiceResponse<NoteName>> getChatNoteNameById(@ApiParam(name="id", value="The id of the Chat that the NoteName applies to.")
                                                                         @PathVariable("id") int id,
                                                                         @ApiParam(name="notenameId", value="The id of the NoteName to fetch.")
                                                                         @PathVariable("notenameId") int notenameId){
        
        
        try {
            
            NoteName chatNoteName = this.chatsService.getChatNoteName(id,notenameId);
            
            ServiceResponse<Keyword> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat NoteName data.")
                            .setSingularData(chatNoteName);
                            
            
            //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
        }
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error response to client
             //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat note name.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to get chat note name."
                    + "with the specified id.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat note name.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    
    
    
    
    
    
    
   
                                                            //Chat >> Promos
    
    
    @ApiOperation(value = "Lists all Promos associated with the Chat with the specified id.", notes = "Lists all Promos associated with the Chat with the specified id.. See the Promo schema for more information.  ",tags={""})
    @RequestMapping(value = "/{id}/promos", method = GET)
    public ResponseEntity<ServiceResponse<Promo>> getChatPromos(@ApiParam(name="id", value="The id of the Chat to fetch Promos for.")
                                                                @PathVariable("id") int id,
                                                                @RequestParam(value ="per_page", required = false) Integer perPage,
                                                                @ApiParam(name="page", value="The number of the page to fetch, defaults to 1 if not specified")
                                                                @RequestParam(value = "page", required = false) Integer page,
                                                                @ApiParam(name="paginate", value="Toggles pagination off/on defaults to TRUE if not specified.")
                                                                @RequestParam(value = "paginate", required = false) String paginate,
                                                                @ApiParam(name="include_deleted", value="indicates whether or not deleted entries should be included in in the listings, defaults to FALSE if not specified.")
                                                                @RequestParam(value = "include_deleted", required = false) boolean includeDeleted,
                                                                @ApiParam(name="results_limit", value="the maximum number of results to return, defaults to 1000 if not specified, may be set to an arbitrarily large value to return all records.")
                                                                @RequestParam(value = "results_limit", required = false) Integer resultsLimit,
                                                                @ApiParam(name="filter_by", value="an optional set of name value pairs that a request may be filtered by.")
                                                                @RequestParam(value = "filter_by", required = false) String filterByParams){
        
        
        
        
        try {
             
             
             //TODO This should be done with pagination enabled in the final build providing
             //ofcourse the "paginate" query parameter has been set to TRUE.
             
             //build list options from supplied query parameters..
             ListOptions listOptions = ListOptions.newInst()
                                                  .setIncludeDeleted(includeDeleted)
                                                  .setResultsLimit((resultsLimit == null) ? 0 : resultsLimit);
                                                 
             
             //set filters
             if(filterByParams != null)
                listOptions.setFilterByNameValuePairs(ResourceUtils.filterQueryStringToNameValuePairs("filters", filterByParams));
             
             //set pagination
             boolean paginateEnabled = (paginate == null || paginate.equalsIgnoreCase("true"));
             
             if(paginateEnabled)
                 listOptions.setPaginationNameValuePairs(ResourceUtils.paginationParametersToNameValuePairs("pagination",(page == null || page < 1) ? 1: page,(perPage == null || perPage < 1) ? 10 : perPage));
             
                 
             
             
             TransformerData<Promo> allPromoData = this.chatsService.listChatPromos(id,listOptions);
             
             //build response....
             
             //if pagination is enabled prepare pagination meta data, this will be ultimately appended to the 
             //ListingOptions instance which will be in turn appended to the ServiceResponse.
             ListingOptions.Pagination paginationMetaData = null;
             if(paginateEnabled){
              
                 Map<String,String> pageMetaMap = allPromoData.getTransformationMetaData(TransformerData.getPaginationMetaDataKey());
                 
                 paginationMetaData = ListingOptions.Pagination
                                                    .newInst(Integer.parseInt(pageMetaMap.get("maxPerPage")),
                                                             Integer.parseInt(pageMetaMap.get("totalEntries")), 
                                                             Integer.parseInt(pageMetaMap.get("totalPages")), 
                                                             Integer.parseInt(pageMetaMap.get("previousPage")), 
                                                             Integer.parseInt(pageMetaMap.get("currentPage")), 
                                                             Integer.parseInt(pageMetaMap.get("nextPage")));
                 
             }
             
             
             
             //construct ServiceResponse...
             ServiceResponse<Promo> serviceResponse = new ServiceResponse<>();
             serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Promo data.")
                            .setData(allPromoData.getData())
                            .setListingOptions(ListingOptions.newInst(listOptions.getResultsLimit(), 
                                                                      listOptions.isIncludeDeleted(),
                                                                      null,
                                                                      filterByParams,
                                                                      paginateEnabled,
                                                                      paginationMetaData));
                            

             //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
             
         }
        
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat promo.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to list chat promo",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat promo.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
        
        
        
   }
    
    
   @ApiOperation(value = "Adds a new Chat Promo to the system.", notes = "Adds a new Promo to the system and associates it with the Chat with the specified id. The id of the newly created Promo is then returned to the caller.  ",tags={""})
   @RequestMapping(value = "/{id}/promos",method = POST)
   public ResponseEntity<ServiceResponse<String>> createChatPromo(@ApiParam(name="promo", value="The new Chat promo to add to the system.")
                                                                  @RequestBody(required = false) Promo promo,
                                                                  @ApiParam(name="id", value="The id of the Chat to associate the Promo with.")
                                                                  @PathVariable("id") int id){
        
        
         try {
            
            int assignedChatId = this.chatsService.createChatPromo(id,promo);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully created Chat Promo")
                           .setSingularData(Integer.toString(assignedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
                           
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to create chat promo", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat keyword.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to create chat promo.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat promo.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
   
   
   
    @ApiOperation(value = "Updates the Promo with the specified id.", notes = "Updates the Promo with the provided id associated with the Chat with the specified id, the id of the updated Promo is then returned to the caller.  ",tags={""})
    @RequestMapping(value = "/{id}/promos/{promoId}",method = PUT)
    public ResponseEntity<ServiceResponse<String>> updateChatPromo(@ApiParam(name="promo", value="The updated promo to associate with the Chat.")
                                                                   @RequestBody(required = false) Promo promo,
                                                                   @ApiParam(name="id", value="The id of the Chat that the updated promo applies to.")
                                                                   @PathVariable("id") int id,
                                                                   @ApiParam(name="promoId", value="The id of the Promo to be updated.")
                                                                   @PathVariable("promoId") int promoId){
        
        
         try {

            
  
            int updatedChatId = this.chatsService.updateChatPromo(id,promo);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully updated Chat Promo.")
                           .setSingularData(Integer.toString(updatedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to update chat Promo.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat NoteName.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to update chat Promo.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat Promo.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    @ApiOperation(value = "Deletes the Promo with the specified id.", notes = "Deletes the Promo with the provided id associated with the Chat with the specified id, the id of the deleted Promo is then returned to the caller. It is important to note that the Promo is only \"soft deleted\" and thus not actually removed from the system. ",tags={""})
    @RequestMapping(value = "/{id}/promos/{promoId}",method = DELETE)
    public ResponseEntity<ServiceResponse<String>> deleteChatPromo(@ApiParam(name="id", value="The id of the Chat that the Promo to be deleted applies to.")
                                                                   @PathVariable("id") int id,
                                                                   @ApiParam(name="promoId", value="The id of the Promo to be deleted.")
                                                                   @PathVariable("promoId") int promoId){
        
        
        try {
            
            int idOfDeletedChat = this.chatsService.deleteChatPromo(id,promoId);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully deleted Chat Promo.")
                           .setSingularData(Integer.toString(idOfDeletedChat));
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to delete chat promo.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to delete chat promo.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to delete chat promo.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to delete chat promo.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    
    @ApiOperation(value = "Gets the Promo with the specified id.", notes = "Gets the Promo with the provided id associated with the Chat with the specified id.  ",tags={""})
    @RequestMapping(value = "/{id}/promos/{promoId}",method = GET)
    public ResponseEntity<ServiceResponse<Promo>> getChatPromoById(@ApiParam(name="id", value="The id of the Chat that the Promo applies to.")
                                                                   @PathVariable("id") int id,
                                                                   @ApiParam(name="promoId", value="The id of the Promo to fetch.")
                                                                   @PathVariable("promoId") int promoId){
        
        
         try {
            
            Promo targetChatPromo = this.chatsService.getChatPromo(id,promoId);
            
            ServiceResponse<Promo> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Promo data.")
                            .setSingularData(targetChatPromo);
                            
            
            //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
        }
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error response to client
             //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat promo.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to get chat promo."
                    + "with the specified id.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat promo.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    
    
    
    
                                                                //Chat >> Category
    
    
    @ApiOperation(value = "Lists all Categories associated with the Chat with the specified id.", notes = "Lists all Categories associated with the Chat with the specified id.. See the Category schema for more information.  ",tags={""})
    @RequestMapping(value = "/{id}/categories", method = GET)
    public ResponseEntity<ServiceResponse<Category>> getChatCategories(@ApiParam(name="id", value="The id of the Chat to fetch Categories for.")
                                                                       @PathVariable("id") int id,
                                                                       @RequestParam(value ="per_page", required = false) Integer perPage,
                                                                       @ApiParam(name="page", value="The number of the page to fetch, defaults to 1 if not specified")
                                                                       @RequestParam(value = "page", required = false) Integer page,
                                                                       @ApiParam(name="paginate", value="Toggles pagination off/on defaults to TRUE if not specified.")
                                                                       @RequestParam(value = "paginate", required = false) String paginate,
                                                                       @ApiParam(name="include_deleted", value="indicates whether or not deleted entries should be included in in the listings, defaults to FALSE if not specified.")
                                                                       @RequestParam(value = "include_deleted", required = false) boolean includeDeleted,
                                                                       @ApiParam(name="results_limit", value="the maximum number of results to return, defaults to 1000 if not specified, may be set to an arbitrarily large value to return all records.")
                                                                       @RequestParam(value = "results_limit", required = false) Integer resultsLimit,
                                                                       @ApiParam(name="filter_by", value="an optional set of name value pairs that a request may be filtered by.")
                                                                       @RequestParam(value = "filter_by", required = false) String filterByParams){
        
        
         try {
             
             
             //TODO This should be done with pagination enabled in the final build providing
             //ofcourse the "paginate" query parameter has been set to TRUE.
             
             //build list options from supplied query parameters..
             ListOptions listOptions = ListOptions.newInst()
                                                  .setIncludeDeleted(includeDeleted)
                                                  .setResultsLimit((resultsLimit == null) ? 0 : resultsLimit);
                                                 
             
             //set filters
             if(filterByParams != null)
                listOptions.setFilterByNameValuePairs(ResourceUtils.filterQueryStringToNameValuePairs("filters", filterByParams));
             
             //set pagination
             boolean paginateEnabled = (paginate == null || paginate.equalsIgnoreCase("true"));
             
             if(paginateEnabled)
                 listOptions.setPaginationNameValuePairs(ResourceUtils.paginationParametersToNameValuePairs("pagination",(page == null || page < 1) ? 1: page,(perPage == null || perPage < 1) ? 10 : perPage));
             
                 
             
             
             TransformerData<Category> allCategoryData = this.chatsService.listChatCategories(id,listOptions);
             
             //build response....
             
             //if pagination is enabled prepare pagination meta data, this will be ultimately appended to the 
             //ListingOptions instance which will be in turn appended to the ServiceResponse.
             ListingOptions.Pagination paginationMetaData = null;
             if(paginateEnabled){
              
                 Map<String,String> pageMetaMap = allCategoryData.getTransformationMetaData(TransformerData.getPaginationMetaDataKey());
                 
                 paginationMetaData = ListingOptions.Pagination
                                                    .newInst(Integer.parseInt(pageMetaMap.get("maxPerPage")),
                                                             Integer.parseInt(pageMetaMap.get("totalEntries")), 
                                                             Integer.parseInt(pageMetaMap.get("totalPages")), 
                                                             Integer.parseInt(pageMetaMap.get("previousPage")), 
                                                             Integer.parseInt(pageMetaMap.get("currentPage")), 
                                                             Integer.parseInt(pageMetaMap.get("nextPage")));
                 
             }
             
             
             
             //construct ServiceResponse...
             ServiceResponse<Category> serviceResponse = new ServiceResponse<>();
             serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Category data.")
                            .setData(allCategoryData.getData())
                            .setListingOptions(ListingOptions.newInst(listOptions.getResultsLimit(), 
                                                                      listOptions.isIncludeDeleted(),
                                                                      null,
                                                                      filterByParams,
                                                                      paginateEnabled,
                                                                      paginationMetaData));
                            

             //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
             
         }
        
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat categories.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to list chat categories",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat categories.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
   }
    
    
   @ApiOperation(value = "Adds a new Chat Category to the system.", notes = "Adds a new Category to the system and associates it with the Chat with the specified id. The id of the newly created Category is then returned to the caller.  ",tags={""})
   @RequestMapping(value = "/{id}/categories",method = POST)
   public ResponseEntity<ServiceResponse<String>> createChatCategory(@ApiParam(name="category", value="The new Chat category to add to the system.")
                                                                     @RequestBody(required = false) Category category,
                                                                     @ApiParam(name="id", value="The id of the Chat to associate the Category with.")
                                                                     @PathVariable("id") int id){
        
        
         try {
            
            int assignedChatId = this.chatsService.createChatCategory(id,category);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully created Chat Category")
                           .setSingularData(Integer.toString(assignedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
                           
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to create chat category", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat category.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to create chat category.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat category.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
   
   
   
    @ApiOperation(value = "Updates the Category with the specified id.", notes = "Updates the Category with the provided id associated with the Chat with the specified id, the id of the updated Category is then returned to the caller.  ",tags={""})
    @RequestMapping(value = "/{id}/categories/{categoryId}",method = PUT)
    public ResponseEntity<ServiceResponse<String>> updateChatCategory(@ApiParam(name="category", value="The updated category to associate with the Chat.")
                                                                      @RequestBody(required = false) Category category,
                                                                      @ApiParam(name="id", value="The id of the Chat that the updated category applies to.")
                                                                      @PathVariable("id") int id,
                                                                      @ApiParam(name="categoryId", value="The id of the Category to be updated.")
                                                                      @PathVariable("categoryId") int categoryId){
        
        
          try {

            
  
            int updatedChatId = this.chatsService.updateChatCategory(id,category);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully updated Chat Categorey.")
                           .setSingularData(Integer.toString(updatedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to update chat Category.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat Chat Category.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to update chat Category.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat Category.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    @ApiOperation(value = "Deletes the Category with the specified id.", notes = "Deletes the Category with the provided id associated with the Chat with the specified id, the id of the deleted Category is then returned to the caller. It is important to note that the Category is only \"soft deleted\" and thus not actually removed from the system. ",tags={""})
    @RequestMapping(value = "/{id}/categories/{categoryId}",method = DELETE)
    public ResponseEntity<ServiceResponse<String>> deleteChatCategory(@ApiParam(name="id", value="The id of the Chat that the Category to be deleted applies to.")
                                                                      @PathVariable("id") int id,
                                                                      @ApiParam(name="categoryId", value="The id of the Category to be deleted.")
                                                                      @PathVariable("categoryId") int categoryId){
        
        
        try {
            
            int idOfDeletedChat = this.chatsService.deleteChatCategory(id,categoryId);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully deleted Chat Cateogry.")
                           .setSingularData(Integer.toString(idOfDeletedChat));
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to delete chat category.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to delete chat promo.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to delete chat category.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to delete chat category.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    
    @ApiOperation(value = "Gets the Category with the specified id.", notes = "Gets the Category with the provided id associated with the Chat with the specified id.  ",tags={""})
    @RequestMapping(value = "/{id}/categories/{categoryId}",method = GET)
    public ResponseEntity<ServiceResponse<Category>> getChatCategoryById(@ApiParam(name="id", value="The id of the Chat that the Category applies to.")
                                                                         @PathVariable("id") int id,
                                                                         @ApiParam(name="categoryId", value="The id of the Category to fetch.")
                                                                         @PathVariable("categoryId") int categoryId){
        
        
          try {
            
            Category targetChatCategory = this.chatsService.getChatCategory(id,categoryId);
            
            ServiceResponse<Category> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Category data.")
                            .setSingularData(targetChatCategory);
                            
            
            //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
        }
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error response to client
             //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat category.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to get chat category."
                    + "with the specified id.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat category.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    
    
    
    
    
    
                                                       //chat >> Affiliates
    
    @ApiOperation(value = "Lists all Affiliates associated with the Chat with the specified id.", notes = "Lists all Affiliates associated with the Chat with the specified id.. See the Affiliate schema for more information.  ",tags={""})
    @RequestMapping(value = "/{id}/affiliates", method = GET)
    public ResponseEntity<ServiceResponse<Affiliate>> getChatAffiliates(@ApiParam(name="id", value="The id of the Chat to fetch Affiliates for.")
                                                                        @PathVariable("id") int id,
                                                                        @RequestParam(value ="per_page", required = false) Integer perPage,
                                                                        @ApiParam(name="page", value="The number of the page to fetch, defaults to 1 if not specified")
                                                                        @RequestParam(value = "page", required = false) Integer page,
                                                                        @ApiParam(name="paginate", value="Toggles pagination off/on defaults to TRUE if not specified.")
                                                                        @RequestParam(value = "paginate", required = false) String paginate,
                                                                        @ApiParam(name="include_deleted", value="indicates whether or not deleted entries should be included in in the listings, defaults to FALSE if not specified.")
                                                                        @RequestParam(value = "include_deleted", required = false) boolean includeDeleted,
                                                                        @ApiParam(name="results_limit", value="the maximum number of results to return, defaults to 1000 if not specified, may be set to an arbitrarily large value to return all records.")
                                                                        @RequestParam(value = "results_limit", required = false) Integer resultsLimit,
                                                                        @ApiParam(name="filter_by", value="an optional set of name value pairs that a request may be filtered by.")
                                                                        @RequestParam(value = "filter_by", required = false) String filterByParams){
        
        
         try {
             
             
             //TODO This should be done with pagination enabled in the final build providing
             //ofcourse the "paginate" query parameter has been set to TRUE.
             
             //build list options from supplied query parameters..
             ListOptions listOptions = ListOptions.newInst()
                                                  .setIncludeDeleted(includeDeleted)
                                                  .setResultsLimit((resultsLimit == null) ? 0 : resultsLimit);
                                                 
             
             //set filters
             if(filterByParams != null)
                listOptions.setFilterByNameValuePairs(ResourceUtils.filterQueryStringToNameValuePairs("filters", filterByParams));
             
             //set pagination
             boolean paginateEnabled = (paginate == null || paginate.equalsIgnoreCase("true"));
             
             if(paginateEnabled)
                 listOptions.setPaginationNameValuePairs(ResourceUtils.paginationParametersToNameValuePairs("pagination",(page == null || page < 1) ? 1: page,(perPage == null || perPage < 1) ? 10 : perPage));
             
                 
             
             
             TransformerData<Affiliate> allAffiliateData = this.chatsService.listChatAffiliates(id,listOptions);
             
             //build response....
             
             //if pagination is enabled prepare pagination meta data, this will be ultimately appended to the 
             //ListingOptions instance which will be in turn appended to the ServiceResponse.
             ListingOptions.Pagination paginationMetaData = null;
             if(paginateEnabled){
              
                 Map<String,String> pageMetaMap = allAffiliateData.getTransformationMetaData(TransformerData.getPaginationMetaDataKey());
                 
                 paginationMetaData = ListingOptions.Pagination
                                                    .newInst(Integer.parseInt(pageMetaMap.get("maxPerPage")),
                                                             Integer.parseInt(pageMetaMap.get("totalEntries")), 
                                                             Integer.parseInt(pageMetaMap.get("totalPages")), 
                                                             Integer.parseInt(pageMetaMap.get("previousPage")), 
                                                             Integer.parseInt(pageMetaMap.get("currentPage")), 
                                                             Integer.parseInt(pageMetaMap.get("nextPage")));
                 
             }
             
             
             
             //construct ServiceResponse...
             ServiceResponse<Affiliate> serviceResponse = new ServiceResponse<>();
             serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Affiliate data.")
                            .setData(allAffiliateData.getData())
                            .setListingOptions(ListingOptions.newInst(listOptions.getResultsLimit(), 
                                                                      listOptions.isIncludeDeleted(),
                                                                      null,
                                                                      filterByParams,
                                                                      paginateEnabled,
                                                                      paginationMetaData));
                            

             //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
             
         }
        
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat affiliates.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to list chat affiliates",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat affiliates.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
   }
    
    
   @ApiOperation(value = "Adds a new Chat Affiliate to the system.", notes = "Adds a new Affiliate to the system and associates it with the Chat with the specified id. The id of the newly created Affiliate is then returned to the caller.  ",tags={""})
   @RequestMapping(value = "/{id}/affiliates",method = POST)
   public ResponseEntity<ServiceResponse<String>> createChatAffiliate(@ApiParam(name="affiliate", value="The new Chat affiliate to add to the system.")
                                                                      @RequestBody(required = false) Affiliate affiliate,
                                                                      @ApiParam(name="id", value="The id of the Chat to associate the Affiliate with.")
                                                                      @PathVariable("id") int id){
        
        
         try {
            
            int assignedChatId = this.chatsService.createChatAffiliate(id,affiliate);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully created Chat Affiliate")
                           .setSingularData(Integer.toString(assignedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
                           
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to create chat affiliate", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat affiliate.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to create chat affiliate.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat affiliate.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
   
   
   
    @ApiOperation(value = "Updates the Affiliate with the specified id.", notes = "Updates the Affiliate with the provided id associated with the Chat with the specified id, the id of the updated Affiliate is then returned to the caller.  ",tags={""})
    @RequestMapping(value = "/{id}/affiliates/{affiliateId}",method = PUT)
    public ResponseEntity<ServiceResponse<String>> updateChatAffiliate(@ApiParam(name="affiliate", value="The updated affiliate to associate with the Chat.")
                                                                       @RequestBody(required = false) Affiliate affiliate,
                                                                       @ApiParam(name="id", value="The id of the Chat that the updated affiliate applies to.")
                                                                       @PathVariable("id") int id,
                                                                       @ApiParam(name="affiliateId", value="The id of the Affiliate to be updated.")
                                                                       @PathVariable("affiliateId") int affiliateId){
        
        
        try {

            
  
            int updatedChatId = this.chatsService.updateChatAffiliate(id,affiliate);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully updated Chat Affiliate.")
                           .setSingularData(Integer.toString(updatedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to update chat Affiliate.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat Chat Affiliate.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to update chat Affiliate.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat Affiliate.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    @ApiOperation(value = "Deletes the Affiliate with the specified id.", notes = "Deletes the Affiliate with the provided id associated with the Chat with the specified id, the id of the deleted Affiliate is then returned to the caller. It is important to note that the Affiliate is only \"soft deleted\" and thus not actually removed from the system. ",tags={""})
    @RequestMapping(value = "/{id}/affiliates/{affiliateId}",method = DELETE)
    public ResponseEntity<ServiceResponse<String>> deleteChatAffiliate(@ApiParam(name="id", value="The id of the Chat that the Affiliate to be deleted applies to.")
                                                                       @PathVariable("id") int id,
                                                                       @ApiParam(name="affiliateId", value="The id of the Affiliate to be deleted.")
                                                                       @PathVariable("affiliateId") int affiliateId){
        
        
       try {
            
            int idOfDeletedChat = this.chatsService.deleteChatAffiliate(id,affiliateId);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully deleted Chat Affiliate.")
                           .setSingularData(Integer.toString(idOfDeletedChat));
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to delete chat affiliate.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to delete chat affiliate.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to delete chat affiliate.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to delete chat affiliate.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    
    @ApiOperation(value = "Gets the Affiliate with the specified id.", notes = "Gets the Affiliate with the provided id associated with the Chat with the specified id.  ",tags={""})
    @RequestMapping(value = "/{id}/affiliates/{affiliateId}",method = GET)
    public ResponseEntity<ServiceResponse<Affiliate>> getChatAffiliateById(@ApiParam(name="id", value="The id of the Chat that the Affiliate applies to.")
                                                                           @PathVariable("id") int id,
                                                                           @ApiParam(name="affiliateId", value="The id of the Affiliate to fetch.")
                                                                           @PathVariable("affiliateId") int affiliateId){
        
        
       try {
            
            Affiliate targetChatAffiliate = this.chatsService.getChatAffiliate(id, affiliateId);
            
            ServiceResponse<Affiliate> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Affiliate data.")
                            .setSingularData(targetChatAffiliate);
                            
            
            //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
        }
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error response to client
             //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat affiliate.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to get chat affiliate."
                    + "with the specified id.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat affiliate.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    
    
    
    
                                                            //chat >> Scheme
    
    
                
    
    @ApiOperation(value = "Lists all Schemes associated with the Chat with the specified id.", notes = "Lists all Schemes associated with the Chat with the specified id.. See the Scheme schema for more information.  ",tags={""})
    @RequestMapping(value = "/{id}/schemes", method = GET)
    public ResponseEntity<ServiceResponse<Scheme>> getChatSchemes(@ApiParam(name="id", value="The id of the Chat to fetch Schemes for.")
                                                                  @PathVariable("id") int id,
                                                                  @RequestParam(value ="per_page", required = false) Integer perPage,
                                                                  @ApiParam(name="page", value="The number of the page to fetch, defaults to 1 if not specified")
                                                                  @RequestParam(value = "page", required = false) Integer page,
                                                                  @ApiParam(name="paginate", value="Toggles pagination off/on defaults to TRUE if not specified.")
                                                                  @RequestParam(value = "paginate", required = false) String paginate,
                                                                  @ApiParam(name="include_deleted", value="indicates whether or not deleted entries should be included in in the listings, defaults to FALSE if not specified.")
                                                                  @RequestParam(value = "include_deleted", required = false) boolean includeDeleted,
                                                                  @ApiParam(name="results_limit", value="the maximum number of results to return, defaults to 1000 if not specified, may be set to an arbitrarily large value to return all records.")
                                                                  @RequestParam(value = "results_limit", required = false) Integer resultsLimit,
                                                                  @ApiParam(name="filter_by", value="an optional set of name value pairs that a request may be filtered by.")
                                                                  @RequestParam(value = "filter_by", required = false) String filterByParams){
        
        
        try {
             
             
             //TODO This should be done with pagination enabled in the final build providing
             //ofcourse the "paginate" query parameter has been set to TRUE.
             
             //build list options from supplied query parameters..
             ListOptions listOptions = ListOptions.newInst()
                                                  .setIncludeDeleted(includeDeleted)
                                                  .setResultsLimit((resultsLimit == null) ? 0 : resultsLimit);
                                                 
             
             //set filters
             if(filterByParams != null)
                listOptions.setFilterByNameValuePairs(ResourceUtils.filterQueryStringToNameValuePairs("filters", filterByParams));
             
             //set pagination
             boolean paginateEnabled = (paginate == null || paginate.equalsIgnoreCase("true"));
             
             if(paginateEnabled)
                 listOptions.setPaginationNameValuePairs(ResourceUtils.paginationParametersToNameValuePairs("pagination",(page == null || page < 1) ? 1: page,(perPage == null || perPage < 1) ? 10 : perPage));
             
                 
             
             
             TransformerData<Scheme> allSchemeData = this.chatsService.listChatSchemes(id,listOptions);
             
             //build response....
             
             //if pagination is enabled prepare pagination meta data, this will be ultimately appended to the 
             //ListingOptions instance which will be in turn appended to the ServiceResponse.
             ListingOptions.Pagination paginationMetaData = null;
             if(paginateEnabled){
              
                 Map<String,String> pageMetaMap = allSchemeData.getTransformationMetaData(TransformerData.getPaginationMetaDataKey());
                 
                 paginationMetaData = ListingOptions.Pagination
                                                    .newInst(Integer.parseInt(pageMetaMap.get("maxPerPage")),
                                                             Integer.parseInt(pageMetaMap.get("totalEntries")), 
                                                             Integer.parseInt(pageMetaMap.get("totalPages")), 
                                                             Integer.parseInt(pageMetaMap.get("previousPage")), 
                                                             Integer.parseInt(pageMetaMap.get("currentPage")), 
                                                             Integer.parseInt(pageMetaMap.get("nextPage")));
                 
             }
             
             
             
             //construct ServiceResponse...
             ServiceResponse<Scheme> serviceResponse = new ServiceResponse<>();
             serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Scheme data.")
                            .setData(allSchemeData.getData())
                            .setListingOptions(ListingOptions.newInst(listOptions.getResultsLimit(), 
                                                                      listOptions.isIncludeDeleted(),
                                                                      null,
                                                                      filterByParams,
                                                                      paginateEnabled,
                                                                      paginationMetaData));
                            

             //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
             
         }
        
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat schemes.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to list chat schemes",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to list chat schemes.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
   }
    
    
   @ApiOperation(value = "Adds a new Chat Scheme to the system.", notes = "Adds a new Scheme to the system and associates it with the Chat with the specified id. The id of the newly created Scheme is then returned to the caller.  ",tags={""})
   @RequestMapping(value = "/{id}/schemes",method = POST)
   public ResponseEntity<ServiceResponse<String>> createChatScheme(@ApiParam(name="scheme", value="The new Chat scheme to add to the system.")
                                                                   @RequestBody(required = false) Scheme scheme,
                                                                   @ApiParam(name="id", value="The id of the Chat to associate the Scheme with.")
                                                                   @PathVariable("id") int id){
        
        
          try {
            
            int assignedChatId = this.chatsService.createChatScheme(id,scheme);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully created Chat Scheme")
                           .setSingularData(Integer.toString(assignedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
                           
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to create chat scheme", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat scheme.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to create chat scheme.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to create chat scheme.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
   
   
   
    @ApiOperation(value = "Updates the Scheme with the specified id.", notes = "Updates the Scheme with the provided id associated with the Chat with the specified id, the id of the updated Scheme is then returned to the caller.  ",tags={""})
    @RequestMapping(value = "/{id}/schemes/{schemeId}",method = PUT)
    public ResponseEntity<ServiceResponse<String>> updateChatScheme(@ApiParam(name="scheme", value="The updated scheme to associate with the Chat.")
                                                                    @RequestBody(required = false) Scheme scheme,
                                                                    @ApiParam(name="id", value="The id of the Chat that the updated scheme applies to.")
                                                                    @PathVariable("id") int id,
                                                                    @ApiParam(name="schemeId", value="The id of the Scheme to be updated.")
                                                                    @PathVariable("schemeId") int schemeId){
        
        
          try {

  
            int updatedChatId = this.chatsService.updateChatScheme(id,scheme);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully updated Chat Scheme.")
                           .setSingularData(Integer.toString(updatedChatId));
            
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to update chat Scheme.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat Chat Scheme.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to update chat Scheme.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to update chat Scheme.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    @ApiOperation(value = "Deletes the Scheme with the specified id.", notes = "Deletes the Scheme with the provided id associated with the Chat with the specified id, the id of the deleted Scheme is then returned to the caller. It is important to note that the Scheme is only \"soft deleted\" and thus not actually removed from the system. ",tags={""})
    @RequestMapping(value = "/{id}/schemes/{schemeId}",method = DELETE)
    public ResponseEntity<ServiceResponse<String>> deleteChatScheme(@ApiParam(name="id", value="The id of the Chat that the Scheme to be deleted applies to.")
                                                                    @PathVariable("id") int id,
                                                                    @ApiParam(name="schemeId", value="The id of the Scheme to be deleted.")
                                                                    @PathVariable("schemeId") int schemeId){
        
        
       try {
            
            int idOfDeletedChat = this.chatsService.deleteChatScheme(id,schemeId);
            
            ServiceResponse<String> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                           .setMessage("Successfully deleted Chat Scheme.")
                           .setSingularData(Integer.toString(idOfDeletedChat));
            
            return new ResponseEntity(serviceResponse,HttpStatus.OK);
        } 
        catch (ChatsServiceException ex) {
           
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occurred whilst attempting to delete chat scheme.", ex);
            
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to delete chat scheme.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(NullPointerException ex){
            
             Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to delete chat scheme.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to delete chat scheme.",ex.getMessage(),HttpStatus.NOT_FOUND);
            
        }
    }
    
    
    
    
    @ApiOperation(value = "Gets the Scheme with the specified id.", notes = "Gets the Scheme with the provided id associated with the Chat with the specified id.  ",tags={""})
    @RequestMapping(value = "/{id}/schemes/{schemeId}",method = GET)
    public ResponseEntity<ServiceResponse<Scheme>> getChatSchemeById(@ApiParam(name="id", value="The id of the Chat that the Scheme applies to.")
                                                                     @PathVariable("id") int id,
                                                                     @ApiParam(name="schemeId", value="The id of the Scheme to fetch.")
                                                                     @PathVariable("schemeId") int schemeId){
        
        
         try {
            
            Scheme targetChatScheme = this.chatsService.getChatScheme(id,schemeId);
            
            ServiceResponse<Scheme> serviceResponse = new ServiceResponse<>();
            serviceResponse.setStatus(HttpStatus.OK.name())
                            .setMessage("Successfully fetched Chat Scheme data.")
                            .setSingularData(targetChatScheme);
                            
            
            //wrap the result of the call to our service is a ServiceResponse instance and return it to the caller.
             return new ResponseEntity(serviceResponse,HttpStatus.OK);
        }
        catch (ChatsServiceException ex) {
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            
            //return 500 error response to client
             //return 500 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat scheme.",ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
        catch(NullPointerException ex){
            
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE,"An error occured whilst attempting to get chat scheme."
                    + "with the specified id.",ex);
            
            //return 404 error to client.
            return (ResponseEntity)this.buildErrorServiceResponse("An error occured whilst attempting to fetch chat scheme.",ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    
    
    
    
    
   /** 
        Simply redirects to the API documentation for this micro service, this method is NOT part
        of the Chat public API itself and thus may be removed or disabled in production
        builds. 
     * @param request
     * @param response
     * @return 
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException
     */
    @ApiOperation(value = "Display this Chats API Documentation, this method is NOT included in the docs itself.", notes = "",tags={""}, hidden = true)
    @RequestMapping("/docs")
    public ResponseEntity<?> redirectToAPIDocumentation(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, URISyntaxException {
         
        /**
        response.setHeader("Location", string1);
        response.sendRedirect(request.getContextPath() + "/swagger-ui.html#/chat45provider45api");  
        response.flushBuffer();
        */
        
        
        URI uri = new URI(request.getContextPath() + "/swagger-ui.html#/chat45provider45api");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        
        
        /**
        request.getServletContext()
               .getRequestDispatcher("/swagger-ui.html#/chat45provider45api")
               .forward(request,response);
        
        */
        
    }
    
    
      
    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<String> handleException() {
        
        return buildJSONErrorResponseMsg("A malformed request was received.", HttpStatus.BAD_REQUEST);
    }
    
    
    
    private ResponseEntity<ServiceResponse>  buildErrorServiceResponse(String message,String debugMessage, HttpStatus status) {

      
            ServiceResponse esResponse = new ServiceResponse();
            esResponse.setMessage(message);
            esResponse.setStatus(status.name());
            esResponse.setDebugMsgEnabled(debugMessagingEnabled);
            esResponse.setDebugMessage(debugMessage);
           
            return new ResponseEntity(esResponse,status);
     }
    
    private static ResponseEntity<String> buildJSONErrorResponseMsg(String message, HttpStatus status) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (null == status) 
            throw new IllegalArgumentException("A status must be provided.");
        
      
         return new ResponseEntity<>("{\"Error\": \"".concat(message).concat("\"}"), headers, status);
            
          
           
        }

    


    
    
    private int testSave() throws ChatsServiceException{
        
      
            System.out.println("Attempting test save standby....");
            
            //top level ChatEntity object
            /**
            ChatEntity chatEntity = new ChatEntity();
            chatEntity.setSlice("test_slice");
            chatEntity.setName("A test chat entity name.");
            chatEntity.setDescription("A test chat entity");
            chatEntity.setDeleted(false);
            
            
            //append 1 or more categories.
            CategoryEntity ce = new CategoryEntity();
            ce.setName("Test Category 1");
            ce.setDescription("A test Cat desc");
            
            CategoryEntity ce2 = new CategoryEntity();
            ce2.setName("Test Category 1");
            ce2.setDescription("A test Cat desc");
            
            List<CategoryEntity> catEntList = new ArrayList<>();
            catEntList.add(ce);
            catEntList.add(ce2);
            
            chatEntity.setCategories(catEntList);
            
            
            //append 1 or more schemes
            SchemeEntity scheme = new SchemeEntity();
            scheme.setName("scheme1");
            scheme.setDescription("A test description");
            
            SchemeEntity scheme2 = new SchemeEntity();
            scheme2.setName("scheme1");
            scheme2.setDescription("A test description");
            
            List<SchemeEntity> schemeEntList = new ArrayList<>();
            schemeEntList.add(scheme);
            schemeEntList.add(scheme2);
            
            
            chatEntity.setSchemes(schemeEntList);
            
            
            
            
            
            
            
            //append 1 or affiliates.
            AffiliateEntity ae = new AffiliateEntity();
            ae.setName("Test Affiliate 1");
            ae.setBrandName("A brand name");
            ae.setCategory(ce2);  //use one of the categories we defined above.
            ae.setScheme(scheme); //use one of the scheme we defined above.
            
            
            AffiliateEntity a2 = new AffiliateEntity();
            a2.setName("Test Affiliate 1");
            a2.setBrandName("A brand name");
            a2.setCategory(ce);  //use one of the categories we defined above.
            a2.setScheme(scheme2); //use one of the scheme we defined above.
            
            
            List<AffiliateEntity> affiliateEntList = new ArrayList<>();
            affiliateEntList.add(ae);
            affiliateEntList.add(a2);
            
            chatEntity.setAffiliates(affiliateEntList);
            
            
            //append one or more keywords to the ChatEntity.
            KeywordEntity keywordEnt = new KeywordEntity();
            keywordEnt.setName("test chat entity");
            keywordEnt.setChatBlock(true);
            keywordEnt.setChatInfo(true);
            keywordEnt.setCommand("This is a test command");
            keywordEnt.setGlobal(true);
            keywordEnt.setNoCreditCheck(false);
            
            List<KeywordEntity> keywordsList = new ArrayList<>();
            keywordsList.add(keywordEnt);
            
            
            chatEntity.setKeywords(keywordsList);
            
            
            //append one or more networks to the ChatEntity
            NetworkEntity ne = new NetworkEntity();
            ne.setNetwork("Test network name");
            ne.setAllowPrePay(true);
            ne.setAllowReverseBill(true);
            
            List<NetworkEntity> networksList = new ArrayList<>();
            networksList.add(ne);
            
            chatEntity.setNetworks(networksList);
            
            
            //append one or more promos to the chat entity
            PromoEntity pe = new PromoEntity();
            pe.setName("Test promo 1");
            pe.setAlreadyClaimedText("test already claimed text");
            pe.setAmount(new BigDecimal("4.00"));
            pe.setDeleted(false);
            pe.setDescription("Test promo1 description");
            pe.setEndTime(new Date());
            pe.setJoinChat(true);
            pe.setJoinDating(true);
            pe.setPromoEndedText("Test promo ended text");
            pe.setPromoNotStartedText("Test promo not started text");
            pe.setStartTime(new Date());
            pe.setSuccessText("Test promo success text");
            
            List<PromoEntity> promosList = new ArrayList<>();
            promosList.add(pe);
            
            chatEntity.setPromos(promosList);
            
            
            //append one or more routes to the chat entity.
            RouteEntity re = new RouteEntity();
            re.setRouteName("My Test Route Name");
            re.setInRev(BigDecimal.ONE);
            re.setMmsCost(BigDecimal.ZERO);
            re.setOutRev(BigDecimal.ONE);
            re.setSmsCost(BigDecimal.TEN);
            
            
            RouteEntity re2 = new RouteEntity();
            re2.setRouteName("My Test Route Name");
            re2.setInRev(BigDecimal.ONE);
            re2.setMmsCost(BigDecimal.ZERO);
            re2.setOutRev(BigDecimal.ONE);
            re2.setSmsCost(BigDecimal.TEN);
            
            List<RouteEntity> routesList = new ArrayList<>();
            routesList.add(re);
            routesList.add(re2);
            
            chatEntity.setRoutes(routesList);
            
            //append a settings instance to the ChatEntity, unlike
            //the other sub entities Settings has a uni-directional One-to-One relationship
            //with ChatEntity rather than a One-To-Many. Settings has a relatively large number
            //of promperties and thus we only set a small subset here
            SettingsEntity settings = new SettingsEntity();
            settings.setAutoJoinChat(true);
            settings.setName("Default Settings");
            settings.setCode("Test code");
            settings.setAutomaticAdRegularTime(1012923);
            settings.setAutoJoinChat(true);
            settings.setBillDuringJoin(true);
            
            
            chatEntity.setSettings(settings);
            
            
            
            //next we define somne templates to store to the ChatEntity
            //Fistly we define some Entries for each template
            TemplateEntryEntity tee = new TemplateEntryEntity();
            tee.setCode("3647");
            tee.setDescription("Desciption");
            tee.setText("Template text1 ");
            
            TemplateEntryEntity tee2 = new TemplateEntryEntity();
            tee2.setCode("4647");
            tee2.setDescription("Desciption");
            tee2.setText("Template text2 ");
            
            
            TemplateEntryEntity tee3 = new TemplateEntryEntity();
            tee3.setCode("4647");
            tee3.setDescription("Desciption");
            tee3.setText("Template text2 ");
            
            List<TemplateEntryEntity> templateEntries = new ArrayList<>();
            templateEntries.add(tee);
            templateEntries.add(tee2);
            templateEntries.add(tee3);
            
            
            
            
            TemplateEntity te = new TemplateEntity();
            te.setCode("Template code 1");
            te.setDescription("A test template description");
            te.setEntries(templateEntries);
            
            
            TemplateEntity te2 = new TemplateEntity();
            te2.setCode("Template code 2 ");
            te2.setDescription("A test template description");
            
            List<TemplateEntity> templatesList = new ArrayList<>();
            templatesList.add(te);
            templatesList.add(te2);
            
            chatEntity.setTemplates(templatesList);
            
            */
            
            //Finally we attemnot to save the ENTIRE object graph saving the
            //top level ob ject should result in a persistence cascade that
            //inturn saves all sub entities.
            Chat chat = new Chat();
            chat.setName("Test chat for service");
            chat.setSlice("test_slice");
            chat.setName("A test chat entity name.");
            chat.setDescription("A test chat entity");
            //chat.setDeleted(false);
            int assignedChatId = this.chatsService.createChat(chat);
            
            
            System.out.println("Test save completed successfully!!");
            
            return assignedChatId;
        
        
       
        
    }
    
    private void testRead(int idOfEntityToRead){
        
        
        try {
            
            Chat targetChat = this.chatsService.getChatById(idOfEntityToRead,false);
            System.out.println(targetChat.getId());
        } 
        
        catch (ChatsServiceException ex) {
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
    }
    
    private int testSoftDelete(int idOfChatToDelete){
        
        int idOfDeletedChat = 0;
        try {
            idOfDeletedChat = this.chatsService.deleteChat(idOfChatToDelete);
            return idOfDeletedChat;
        } 
        catch (ChatsServiceException ex) {
            Logger.getLogger(ChatsMicroserviceAPI.class.getName()).log(Level.SEVERE, null, ex);
            return idOfDeletedChat;
        }
    }
    
    
}
