/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.services;

import com.apnuk.services.chat.dtos.Affiliate;
import com.apnuk.services.chat.dtos.Category;
import com.apnuk.services.chat.dtos.Chat;
import com.apnuk.services.chat.dtos.Keyword;
import com.apnuk.services.chat.dtos.Network;
import com.apnuk.services.chat.dtos.NoteName;
import com.apnuk.services.chat.dtos.Promo;
import com.apnuk.services.chat.dtos.Route;
import com.apnuk.services.chat.dtos.Scheme;
import com.apnuk.services.chat.dtos.Settings;
import com.apnuk.services.chat.dtos.Template;
import com.apnuk.services.chat.dtos.TemplateEntry;
import com.apnuk.services.chat.entities.AffiliateEntity;
import com.apnuk.services.chat.entities.CategoryEntity;
import com.apnuk.services.chat.entities.ChatEntity;
import com.apnuk.services.chat.entities.KeywordEntity;
import com.apnuk.services.chat.entities.NetworkEntity;
import com.apnuk.services.chat.entities.NoteNameEntity;
import com.apnuk.services.chat.entities.PromoEntity;
import com.apnuk.services.chat.entities.RouteEntity;
import com.apnuk.services.chat.entities.SchemeEntity;
import com.apnuk.services.chat.entities.SettingsEntity;
import com.apnuk.services.chat.entities.TemplateEntity;
import com.apnuk.services.chat.entities.TemplateEntryEntity;
import com.apnuk.services.chat.exceptions.ChatsServiceException;
import com.apnuk.services.chat.repositories.AffiliateRepository;
import com.apnuk.services.chat.repositories.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.apnuk.services.chat.repositories.ChatsRepository;
import com.apnuk.services.chat.repositories.KeywordRepository;
import com.apnuk.services.chat.repositories.NetworksRepository;
import com.apnuk.services.chat.repositories.NoteNamesRepository;
import com.apnuk.services.chat.repositories.PromosRepository;
import com.apnuk.services.chat.repositories.RoutesRepository;
import com.apnuk.services.chat.repositories.SchemesRepository;
import com.apnuk.services.chat.repositories.SettingsRepository;
import com.apnuk.services.chat.repositories.TemplateEntriesRepository;
import com.apnuk.services.chat.repositories.TemplatesRepository;
import com.apnuk.services.chat.resources.ListOptions;
import com.apnuk.services.chat.utilities.transformations.FilteringDataTransformer;
import com.apnuk.services.chat.utilities.transformations.PaginationDataTransformer;
import com.apnuk.services.chat.utilities.transformations.SortingDataTransformer;
import com.apnuk.services.chat.utilities.transformations.TransformerData;
import java.util.Map;
import java.util.stream.Collectors;



import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;



/**
 * Reference implementation of a ChatsService.
 * @author gilesthompson
 */
@Component
@Transactional(readOnly = false)
public class DefaultChatsService implements ChatsService{
    
   @Autowired
   private ChatsRepository chatsRepository;
 
   @Autowired
   private KeywordRepository keywordsRepository;
   
   @Autowired
   private NetworksRepository networksRepository;
   
   
   @Autowired
   private NoteNamesRepository notenamesRepository;
   
   
   @Autowired
   private PromosRepository promosRepository;
   
   
   @Autowired
   private RoutesRepository routesRepository;
   
   
   @Autowired
   private SchemesRepository schemesRepository;
   
   @Autowired
   private TemplateEntriesRepository templateEntriesRepository;
   
   @Autowired
   private TemplatesRepository templatesRepository;
   
   
   @Autowired
   private CategoryRepository categoryRepository;
   
   
   @Autowired
   private AffiliateRepository affiliateRepository;
   
   
   
   
   
   
  
   
   /**Map to serve as a session-scoped cache for loaded top-level Chats entities. */
   private Map<Integer,ChatEntity> localEntityCache;

    @Override
    public TransformerData<Chat> listChats(ListOptions listOptions) throws ChatsServiceException {
        
        
        List<ChatEntity> allChatEntities = loadAllEntries(listOptions.isIncludeDeleted(),listOptions.getResultsLimit());
        
        List<Chat> allChatModels = new ArrayList<>();
         
        //iterate over the return entity insstance and convert each one to a model.       
        allChatEntities.forEach(chatEntity -> { allChatModels.add(chatEntity.toDTO());});
        
        //crucially here we DECOATRE the raw list data with one or more transformers which will 
        //each be applied to the data in the order in which they are specified as required, inside to out.
        //The data tranformers will be applied in the following order:
        //1)Filter
        //2)Sort
        //3)Paginate
        
        //Filter...
        TransformerData<Chat> transformerData = new TransformerData(allChatModels,Chat.class);
        FilteringDataTransformer filteringDataTransformer = new FilteringDataTransformer<>(transformerData,listOptions.getFilterByNameValuePairs());
        
        //DECORATE Filter with Sort.
        SortingDataTransformer sortingDataTransformer = new SortingDataTransformer(filteringDataTransformer,listOptions.getOrderByNameValuePairs());
        
        //DECORATE Sort with Pagination. 
        PaginationDataTransformer<Chat> paginationDataTransformer = new PaginationDataTransformer(sortingDataTransformer,listOptions.getPaginationNameValuePairs());
       
        //call paginate which will inturn call all nested transformation routines.
        return paginationDataTransformer.transform();
    }

    @Override
    public Chat getChatById(int chatId,boolean includeDeleted) throws ChatsServiceException {
        
        if(chatId <= 0)
            throw new ChatsServiceException("An invalid Chat id was specified.");
       
      
        return this.loadChatEntry(chatId,includeDeleted)
                   .toDTO();
                   
    
    }
    
    
    @Override
    
    public int createChat(Chat chat) throws ChatsServiceException {
        
        //extended input validation
        if(chat == null)
            throw new ChatsServiceException("A null Chat value was specified.");
        
        if(chat.getName() == null || chat.getName().length() < 1)
            throw new ChatsServiceException("The specified chat was not assigned a valid name");
        
        if(chat.getId() != 0)
            chat.setId(0);
        
        
         //convert the Chat model to an ChatEntity
         ChatEntity chatEntity = chat.toEntity();
         
         //store the resulting ChatEntity to the underlying data store
         this.chatsRepository.save(chatEntity);
         
         //TODO: cache the newly created entity here
         
         //return the id of the newly create ChatEntity to the caller as a string.
         return chatEntity.getId();
        
    }

    @Override
    
    public int updateChat(Chat chat) throws ChatsServiceException {
       
        //preliminary input validation
        if(chat == null)
            throw new ChatsServiceException("Update failed, a null Chat instance was specified.");
        
        if(chat.getId() <= 0)
            throw new ChatsServiceException("Update failed, an invalid chat id was specified");
        
        
        //attempt to fetch entry with the specified id, if no entry isn available
        //under the specified id then we prompty abort the operatiobn and return an
        //exception to that effect.
        ChatEntity targetChatEntity = this.loadChatEntry(chat.getId(),false);
        
        
        
        //otherwise we proceed to update the entry, essentially we store the new targetChat
        //instance in place of the existing one. Will return the id of the entry that
        //was updated where at least one field was updated or -1 otherwise.
        int updatedVal = updateChatEntry(targetChatEntity,chat);
        
        
        return updatedVal;
        
    }

    @Override
    
    public int deleteChat(int chatId) throws ChatsServiceException {
        
        if(chatId <= 0)
            throw new ChatsServiceException("A null or invalid chat id was specified.");
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        
        //our Data Access Object implementation will ONLY soft delete the entry
        return this.chatsRepository.softDeleteChat(targetChatEntity);
        
                
    }

    @Override
    public Settings getChatSettings(int chatId) throws ChatsServiceException {
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
       
        return targetChatEntity.getSettings()
                               .toDTO();
                               
    }
    
    
     @Override
     
    public int createChatSettings(int chatId, Settings settings) throws ChatsServiceException {
       
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        
        SettingsEntity settingsEntity = settings.toEntity();
        
        targetChatEntity.setSettings(settingsEntity);
        
        this.chatsRepository.save(targetChatEntity);
        
        return targetChatEntity.getId();
    }
    
    

    @Override
    public int updateChatSettings(int chatId, Settings settings) throws ChatsServiceException {
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
            
        SettingsEntity settingsEntity = settings.toEntity();
        
        targetChatEntity.setSettings(settingsEntity);
        
        this.chatsRepository.save(targetChatEntity);
        
        //#update cache here to reflect new setting
        
        return targetChatEntity.getId();
        
    }

    @Override
    public TransformerData<Template> listChatTemplates(int chatId,ListOptions listOptions) throws ChatsServiceException {
      
        
        if(chatId <= 0)
            throw new ChatsServiceException("An invalid Chat id was specified.");
        
        
        //check cache first and return from cache if available.
      
        //Chat MUST exist in order to list templates for it.
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        
        List<Template> templates = new ArrayList<>();
     
        for(TemplateEntity curTemplateEntity : targetChatEntity.getTemplates()){
            
            //if the user has elected NOT to view deleted items
            //AND the current item HAS been deleted then we 
            //omit it from the list
            if(!listOptions.isIncludeDeleted() && curTemplateEntity.isDeleted())
                continue;
            
            //if we have already reached the max number of records 
           
            templates.add(curTemplateEntity.toDTO());
        }
        
        //store collection of Templates to the appropriate cached Chats instance where it exists.
        
        
        //if the user has elected to limit the results they wish to receive create a sublist
        templates = (listOptions.getResultsLimit() > 0) ? templates.subList(0,listOptions.getResultsLimit()) : templates;
        
        
        //crucially here we DECOATRE the raw list data with one or more transformers which will 
        //each be applied to the data in the order in which they are specified as required, inside to out.
        //The data tranformers will be applied in the following order:
        //1)Filter
        //2)Sort
        //3)Paginate
        
        //Filter...
        TransformerData<Template> transformerData = new TransformerData(templates,Template.class);
        FilteringDataTransformer filteringDataTransformer = new FilteringDataTransformer<>(transformerData,listOptions.getFilterByNameValuePairs());
        
        //DECORATE Filter with Sort.
        SortingDataTransformer sortingDataTransformer = new SortingDataTransformer(filteringDataTransformer,listOptions.getOrderByNameValuePairs());
        
        //DECORATE Sort with Pagination. 
        PaginationDataTransformer<Template> paginationDataTransformer = new PaginationDataTransformer(sortingDataTransformer,listOptions.getPaginationNameValuePairs());
       
        //call paginate which will inturn call all nested transformation routines.
        return paginationDataTransformer.transform();
       
    }

    @Override
    public Template getChatTemplate(int chatId, int templateId) throws ChatsServiceException {
       
         if(chatId <= 0)
            throw new ChatsServiceException("An invalid Chat id was specified.");
         
         if(templateId <= 0)
            throw new ChatsServiceException("An invalid Chat template id was specified.");
          
         
          //check cache first and return from cache if available
         
          Template targetTemplate = this.listChatTemplates(chatId,ListOptions.newInst()
                                                                             .setPaginate(false)
                                                                             .setResultsLimit(0)
                                                                             .setIncludeDeleted(false))
                                        .getData()                                                             
                                        .stream()
                                        .filter(c -> c.getId() == templateId)
                                        .findFirst()
                                        .orElseThrow(()-> new NullPointerException("Unable to locate chat template with the specified id."));
         
         
        
         return targetTemplate;
    }

    @Override
    
    public int createChatTemplate(int chatId, Template template) throws ChatsServiceException {
        
        if(chatId <= 0)
           throw new ChatsServiceException("An invalid chat id was specified.");
            
        
        if(template == null)
            throw new ChatsServiceException("A null or invalid template was specified.");
        
        if(template.getId() != 0)
           template.setId(0);
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        
        TemplateEntity templateEntity = template.toEntity();
        
        this.templatesRepository.save(templateEntity);
        
        targetChatEntity.getTemplates()
                        .add(templateEntity);
        
        
        this.chatsRepository.save(targetChatEntity);
        
        return templateEntity.getId();
    }

    @Override
    
    public int updateChatTemplate(int chatId, Template template) throws ChatsServiceException {
        
        if(chatId <= 0)
           throw new ChatsServiceException("An invalid chat id was specified.");
            
        
        if(template == null)
            throw new ChatsServiceException("A null or invalid template was specified.");
        
        if(template.getId() <= 0)
            throw new ChatsServiceException("A invalid template id was specified.");
        
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        TemplateEntity targetTemplate = targetChatEntity.getTemplates()
                                                        .stream()
                                                        .filter(curChatTemplate -> !curChatTemplate.isDeleted())
                                                        .filter(curChatTemplate -> curChatTemplate.getId() == template.getId())
                                                        .findFirst()
                                                        .orElseThrow(()->new NullPointerException("Update failed, unable to locate template with the specified id."));
                                                        
        

        if(!targetTemplate.getCode().equals(template.getCode()))
            targetTemplate.setCode(template.getCode());
        
        if(!targetTemplate.getDescription().equals(template.getDescription()))
            targetTemplate.setDescription(template.getDescription());
        
        
        this.chatsRepository.save(targetChatEntity);
        
        
        return targetTemplate.getId();
     
    }

    @Override
    public int deleteChatTemplate(int chatId, int templateId) throws ChatsServiceException {
        
         if(chatId <= 0)
           throw new ChatsServiceException("An invalid Chat id was specified.");
         
         if(templateId <= 0)
              throw new ChatsServiceException("An invalid Chat Template id was specified.");
         
         
         //soft the delete the specified template entry
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         return this.chatsRepository.softDeleteChatTemplate(targetChatEntity,templateId);
        
    }

    @Override
    public TransformerData<TemplateEntry> listChatTemplateEntries(int chatId, int templateId,ListOptions listOptions) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to list Chat Templates Entries an invalid Chat id was specified.");
        
        
        if(templateId <= 0)
            throw new ChatsServiceException("Unable to list Chat Templates Entries an invalid Chat Template id was specified.");
        
       
        //check cache first.... and return from there where available...
        
        
        //otherwise...
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        List<TemplateEntry> templateEntries = targetChatEntity.getTemplates()
                                                              .stream()
                                                              .filter(ce -> !ce.isDeleted())
                                                              .filter(ce -> ce.getId() == templateId)
                                                              .findFirst()
                                                              .orElseThrow(()-> new NullPointerException())
                                                              .getEntries()
                                                              .stream()
                                                              .filter(!listOptions.isIncludeDeleted()? ce -> !ce.isDeleted() : ce-> true)
                                                              .map(ce -> ce.toDTO())
                                                              .collect(Collectors.toList());
                                                              
                                                              
        //store list of entries to the cache..
        
        
        //if the user has elected to limit the results they would like to receive then we create a sublist
        templateEntries = (listOptions.getResultsLimit() > 0) ? templateEntries.subList(0,listOptions.getResultsLimit()) : templateEntries;
        
        
        //crucially here we DECOATRE the raw list data with one or more transformers which will 
        //each be applied to the data in the order in which they are specified as required, inside to out.
        //The data tranformers will be applied in the following order:
        //1)Filter
        //2)Sort
        //3)Paginate
        
        //Filter...
        TransformerData<TemplateEntry> transformerData = new TransformerData(templateEntries,TemplateEntry.class);
        FilteringDataTransformer filteringDataTransformer = new FilteringDataTransformer<>(transformerData,listOptions.getFilterByNameValuePairs());
        
        //DECORATE Filter with Sort.
        SortingDataTransformer sortingDataTransformer = new SortingDataTransformer(filteringDataTransformer,listOptions.getOrderByNameValuePairs());
        
        //DECORATE Sort with Pagination. 
        PaginationDataTransformer<TemplateEntry> paginationDataTransformer = new PaginationDataTransformer(sortingDataTransformer,listOptions.getPaginationNameValuePairs());
       
        //call paginate which will inturn call all nested transformation routines.
        return paginationDataTransformer.transform();
      
        
    }

    @Override
    
    public int createChatTemplateEntry(int chatId, int templateId, TemplateEntry templateEntry) throws ChatsServiceException {
       
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to create Chat Templates Entries an invalid Chat id was specified.");
        
        
        if(templateId <= 0)
            throw new ChatsServiceException("Unable to create Chat Templates Entries an invalid Chat Template id was specified.");
        
        
        //set template entry id to null where one has been specified.
        if(templateEntry == null)
            throw new NullPointerException("Entry could not be saved, a null entry was specified.");
        
        
       
        
        
        //firstly locate the Chats template the entry should be appended to.
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        TemplateEntity targetTemplate  = targetChatEntity.getTemplates()
                                                         .stream()
                                                         .filter(ce -> !ce.isDeleted())
                                                         .filter(ce -> ce.getId() == templateId)
                                                         .findFirst()
                                                         .orElseThrow(()-> new NullPointerException());
        
        
        //convert the model to a an equivilent entity
        TemplateEntryEntity templateEntryEntity = templateEntry.toEntity();
        
        //set id to 0 where it hs been erroneously set on the front end
        if(templateEntryEntity.getId() > 0)
            templateEntryEntity.setId(0);
        
        this.templateEntriesRepository.save(templateEntryEntity);
        
        //append new template to list of entries
        targetTemplate.getEntries()
                      .add(templateEntryEntity);
        
        //persist the top-level Chat instance which should inturn persist the Template and ultimately 
        //the new TemplateEntry via the JPA persistence cascade mechanism. 
        //TODO: Refactor this direct call to a utility method which will flush the cache on successful execution
        this.chatsRepository.save(targetChatEntity);
        
        //return the id of the newly saved entry
        return templateEntryEntity.getId();
    }

    @Override
    
    public int updateChatTemplateEntry(int chatId, int templateId, TemplateEntry templateEntry) throws ChatsServiceException {
       
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to update Chat Templates Entries an invalid Chat id was specified.");
        
        
        if(templateId <= 0)
            throw new ChatsServiceException("Unable to update Chat Templates Entries an invalid Chat Template id was specified.");
        

        if(templateEntry == null)
            throw new NullPointerException("Entry could not be updated, a null entry was specified.");
        
        
        //firstly we locate the Chat Template Entry in question..
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         TemplateEntryEntity targetTemplateEntity = targetChatEntity.getTemplates()
                                                                    .stream()
                                                                    .filter(ce -> !ce.isDeleted())
                                                                    .filter(ce -> ce.getId() == templateId)
                                                                    .findFirst()
                                                                    .orElseThrow(()-> new NullPointerException())
                                                                    .getEntries()
                                                                    .stream()
                                                                    .filter(cte-> !cte.isDeleted()) //entry must not be deleted to be updated.
                                                                    .findFirst()
                                                                    .orElseThrow(()-> new NullPointerException("Chat Template Entry update failed, the specified entry could not be found. "));
         
         
         //ok if we have arrvied here then we have successfully located the the TemplateEntry
         //next providing at lease one field has been changed we initiate an update to the
         //uderlying data store which will inturn also flush the in-memory cache.
         boolean updateDetected = false;
         if(templateEntry.getCode() != null
                 && !templateEntry.getCode().equals(targetTemplateEntity.getCode())){
             
             //set our flag to true
             updateDetected = true;
             
             //update the entry code
            targetTemplateEntity.setCode(templateEntry.getCode());
             
         }
         if(templateEntry.getDescription() != null
                 && !templateEntry.getDescription().equals(targetTemplateEntity.getDescription())){
             
             //set our flag to true
             updateDetected = true;
             
             //update the entry code
            targetTemplateEntity.setDescription(templateEntry.getDescription());
             
         }
         
         if(templateEntry.getText() != null
                 && !templateEntry.getText().equals(targetTemplateEntity.getText())){
             
             //set our flag to true
             updateDetected = true;
             
             //update the entry code
            targetTemplateEntity.setText(templateEntry.getText());
             
         }
         
         
        //providsing at least one field has been updated write back to the datastore
        if(updateDetected){
            this.chatsRepository.save(targetChatEntity);
            return targetTemplateEntity.getId();
        }
        else
            return -1;

    }

    
    @Override
    public int deleteChatTemplateEntry(int chatId, int templateId, int templateEntryId) throws ChatsServiceException {
        
        
         //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Templates Entry an invalid Chat id was specified.");
        
        
        if(templateId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Templates Entry an invalid Chat Template id was specified.");
        
        
        if(templateEntryId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Templates Entry an invalid Chat Template Entry id was specified.");
        
        
        //soft delete the template entry and flush the cache.
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        return this.chatsRepository.softDeleteChatTemplateEntry(targetChatEntity,templateId,templateEntryId);

    }

    @Override
    public TransformerData<Network> listChatNetworks(int chatId,ListOptions listOptions) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to update Chat Templates Entries an invalid Chat id was specified.");
        
        
        //check cache first and return from there if available....
        
        //otherwise...
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        List<Network> networks = targetChatEntity.getNetworks()
                                                 .stream()
                                                 .filter(!listOptions.isIncludeDeleted()? e-> !e.isDeleted() : e->true)
                                                 .map(e-> e.toDTO())
                                                 .collect(Collectors.toList());
                        
        
       //store the list data to the cache 
       
       //limit returned results if necessary
        networks = (listOptions.getResultsLimit() > 0) ? networks.subList(0,listOptions.getResultsLimit()) : networks;
        
        
        //crucially here we DECOATRE the raw list data with one or more transformers which will 
        //each be applied to the data in the order in which they are specified as required, inside to out.
        //The data tranformers will be applied in the following order:
        //1)Filter
        //2)Sort
        //3)Paginate
        
        //Filter...
        TransformerData<Network> transformerData = new TransformerData(networks,Network.class);
        FilteringDataTransformer filteringDataTransformer = new FilteringDataTransformer<>(transformerData,listOptions.getFilterByNameValuePairs());
        
        //DECORATE Filter with Sort.
        SortingDataTransformer sortingDataTransformer = new SortingDataTransformer(filteringDataTransformer,listOptions.getOrderByNameValuePairs());
        
        //DECORATE Sort with Pagination. 
        PaginationDataTransformer<Network> paginationDataTransformer = new PaginationDataTransformer(sortingDataTransformer,listOptions.getPaginationNameValuePairs());
       
        //call paginate which will inturn call all nested transformation routines.
        return paginationDataTransformer.transform();

    }

    @Override
    public int createChatNetwork(int chatId, Network network) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to create Network, an invalid Chat id was specified.");
        
        if(network == null)
           throw new NullPointerException("Unable to create Network, a null entry was specified.");
        
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        
        //prepare entity
        NetworkEntity networkEntity = network.toEntity();
        if(networkEntity.getId() > 0)
            networkEntity.setId(0);
        
        
        this.networksRepository.save(networkEntity);

        targetChatEntity.getNetworks()
                        .add(networkEntity);
        
        //update and flush cache
        this.chatsRepository.save(targetChatEntity);
        
        //return the id of the newly created Chat Network
        return networkEntity.getId();
    }

    @Override
    
    public int updateChatNetwork(int chatId, Network network) throws ChatsServiceException {
       
         //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to update Network, an invalid Chat id was specified.");
        
        if(network == null)
           throw new NullPointerException("Unable to update  Network, a null entry was specified.");
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        
        //attempt to locate network to update
        NetworkEntity targetNetworkEntity = targetChatEntity.getNetworks()
                                                            .stream()
                                                            .filter(e-> e.getId() == network.getId())
                                                            .findFirst()
                                                            .orElseThrow(()-> new NullPointerException("Network update failed, unable to locate Network with the specified id."));
        
        
        //if we have arrived here we have located the Network and thus providing
        //at least one field has been updated we proceed to update it to the underlying store.
        boolean updatedDetected = false;
        
        if(network.isAllowPrePay() != targetNetworkEntity.isAllowPrePay()){
            
            targetNetworkEntity.setAllowPrePay(network.isAllowPrePay());
            
            updatedDetected = true;
        }
        
        
        if(network.isAllowReverseBill() != targetNetworkEntity.isAllowReverseBill()){
            
            targetNetworkEntity.setAllowReverseBill(network.isAllowReverseBill());
            
            updatedDetected = true;
        }
        
        
        //if update has been detect write to underlying store and flush cache.
        if(updatedDetected){
            
            this.chatsRepository.save(targetChatEntity);
            
            return targetNetworkEntity.getId();
        }
        else
            return -1;
            
    }

    @Override
    public int deleteChatNetwork(int chatId, int networkId) throws ChatsServiceException {
       
         //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Network, an invalid Chat id was specified.");
        
        if(networkId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Network, an invalid Chat Network id was specified.");
        
        //soft delete the Network
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        return this.chatsRepository.softDeleteChatNetwork(targetChatEntity,networkId);
 
    }

    @Override
    public TransformerData<Route> listChatRoutes(int chatId,ListOptions listOptions) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to list Chat Routes an invalid Chat id was specified.");
        
        
        //check cache first, return data from there where it exists
        
        
        //otherwise... 
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        List<Route> chatRoutes = targetChatEntity.getRoutes()
                                                     .stream()
                                                     .filter(!listOptions.isIncludeDeleted() ? e-> !e.isDeleted() : e->true)
                                                     .map(e-> e.toDTO())
                                                     .collect(Collectors.toList());
        
        
        //back-fill the cache to make route data available to service any subsequent request efficiently
        chatRoutes = (listOptions.getResultsLimit() > 0) ? chatRoutes.subList(0,listOptions.getResultsLimit()) : chatRoutes;
        
        
        //crucially here we DECOATRE the raw list data with one or more transformers which will 
        //each be applied to the data in the order in which they are specified as required, inside to out.
        //The data tranformers will be applied in the following order:
        //1)Filter
        //2)Sort
        //3)Paginate
        
        //Filter...
        TransformerData<Route> transformerData = new TransformerData(chatRoutes,Route.class);
        FilteringDataTransformer filteringDataTransformer = new FilteringDataTransformer<>(transformerData,listOptions.getFilterByNameValuePairs());
        
        //DECORATE Filter with Sort.
        SortingDataTransformer sortingDataTransformer = new SortingDataTransformer(filteringDataTransformer,listOptions.getOrderByNameValuePairs());
        
        //DECORATE Sort with Pagination. 
        PaginationDataTransformer<Route> paginationDataTransformer = new PaginationDataTransformer(sortingDataTransformer,listOptions.getPaginationNameValuePairs());
       
        //call paginate which will inturn call all nested transformation routines.
        return paginationDataTransformer.transform();
                                                     
    }

    @Override
    public Route getChatRoute(int chatId, int routeId) throws ChatsServiceException {
      
         //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to get Chat Route an invalid Chat id was specified.");
        
        if(routeId <= 0)
            throw new ChatsServiceException("Unable to get Chat Route an invalid Chat Route id was specified.");
        
        
        //check cache return entry from there where available
        
        
        //otherwise attempt to fetch the entry from the underlying  datastore
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         Route targetChatRoute = targetChatEntity.getRoutes()
                                                 .stream()
                                                 .filter(e-> e.getId() == routeId)
                                                 .map(e-> e.toDTO())
                                                 .findFirst()
                                                 .orElseThrow(()-> new NullPointerException("Unable to locate Chat Route with the specified id"));
         
         
         return targetChatRoute;
                                                 
    }

    @Override
    
    public int createChatRoute(int chatId, Route route) throws ChatsServiceException {
        
        
         //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to create Chat Route an invalid Chat id was specified.");
        
         if(route == null)
            throw new NullPointerException("Unable to create Chat Route a null Chat instance was specified.");
         
         
         
         //prepare the route entity
         RouteEntity routeEntity = route.toEntity();
         if(routeEntity.getId() > 0)
             routeEntity.setId(0);
         
         this.routesRepository.save(routeEntity);
         
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         targetChatEntity.getRoutes()
                         .add(routeEntity);
         
         
         this.chatsRepository.save(targetChatEntity);
                                                  
         
         return routeEntity.getId();

    }

    @Override
    
    public int updateChatRoute(int chatId, Route route) throws ChatsServiceException {
       
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to update Chat Route an invalid Chat id was specified.");
        
         if(route == null)
            throw new NullPointerException("Unable to update Chat Route a null Chat instance was specified.");
         
         
          //locate chat route in question
          ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
          RouteEntity targetRouteEntity = targetChatEntity.getRoutes()
                                                          .stream()
                                                          .filter(e-> e.getId() == route.getId())
                                                          .findFirst()
                                                          .orElseThrow(() -> new NullPointerException("Unable to update route, a route with the specified id could not be found."));
                     
          
          //if one or mopre values have changed then proceed to update route in the data
          boolean updateDetected = false;
          
          if(route.getInRev() != null 
                  && !route.getInRev().equals(targetRouteEntity.getInRev())){
              
              
              targetRouteEntity.setInRev(route.getInRev());
              
              updateDetected = true;
          }
          
          if(route.getMmsCost() != null 
                  && !route.getMmsCost().equals(targetRouteEntity.getMmsCost())){
              
              
              targetRouteEntity.setMmsCost(route.getMmsCost());
              
              updateDetected = true;
          }
          
          if(route.getOutRev() != null 
                  && !route.getOutRev().equals(targetRouteEntity.getOutRev())){
              
              
              targetRouteEntity.setOutRev(route.getOutRev());
              
              updateDetected = true;
          }
          
          if(route.getRouteName() != null 
                  && !route.getRouteName().equals(targetRouteEntity.getRouteName())){
              
              
              targetRouteEntity.setRouteName(route.getRouteName());
              
              updateDetected = true;
          }
          
         
          if(route.getSmsCost() != null 
                  && !route.getSmsCost().equals(targetRouteEntity.getSmsCost())){
              
              
              targetRouteEntity.setSmsCost(route.getSmsCost());
              
              updateDetected = true;
          }
          
          
          if(updateDetected){
              
              this.chatsRepository.save(targetChatEntity);
              
              return targetRouteEntity.getId();
          }
          else
              return -1;
                  
    }

    @Override
    public int deleteChatRoute(int chatId, int routeId) throws ChatsServiceException {
       
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Route an invalid Chat id was specified.");
        
        if(routeId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Route an invalid Chat Route id was specified.");
        
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        return this.chatsRepository.softDeleteChatRoute(targetChatEntity,routeId);
        
    }

    
    
    
    
    
    @Override
    public TransformerData<Keyword> listChatKeywords(int chatId,ListOptions listOptions) throws ChatsServiceException {
        
       //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to list Chat Keywords an invalid Chat id was specified.");
        
        
        //check cache first and return data from there where available..
        
        //otherwise...
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        List<Keyword> chatKeywordsList = targetChatEntity.getKeywords()
                                                         .stream()
                                                         .filter(!listOptions.isIncludeDeleted() ? e-> !e.isDeleted() : e->true)
                                                         .map(e-> e.toDTO())
                                                         .collect(Collectors.toList());
        
        
        //store keyword list data to cache so it is available to efficiently service any subsequent requests.
        
        chatKeywordsList = (listOptions.getResultsLimit() > 0) ? chatKeywordsList.subList(0,listOptions.getResultsLimit()) : chatKeywordsList;
        
        
        //crucially here we DECOATRE the raw list data with one or more transformers which will 
        //each be applied to the data in the order in which they are specified as required, inside to out.
        //The data tranformers will be applied in the following order:
        //1)Filter
        //2)Sort
        //3)Paginate
        
        //Filter...
        TransformerData<Keyword> transformerData = new TransformerData(chatKeywordsList,Keyword.class);
        FilteringDataTransformer filteringDataTransformer = new FilteringDataTransformer<>(transformerData,listOptions.getFilterByNameValuePairs());
        
        //DECORATE Filter with Sort.
        SortingDataTransformer sortingDataTransformer = new SortingDataTransformer(filteringDataTransformer,listOptions.getOrderByNameValuePairs());
        
        //DECORATE Sort with Pagination. 
        PaginationDataTransformer<Keyword> paginationDataTransformer = new PaginationDataTransformer(sortingDataTransformer,listOptions.getPaginationNameValuePairs());
       
        //call paginate which will inturn call all nested transformation routines.
        return paginationDataTransformer.transform();
        
    }

    @Override
    public Keyword getChatKeyword(int chatId, int keywordId) throws ChatsServiceException {
       
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to get Chat Keyword an invalid Chat id was specified.");
        
        if(keywordId<= 0)
            throw new ChatsServiceException("Unable to get Chat Keyword an invalid Chat Keyword id was specified.");
        
        
        //check cache return entry from there where available
        
        
        //otherwise attempt to fetch the entry from the underlying  datastore
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         Keyword targetChatKeyword = targetChatEntity.getKeywords()
                                                     .stream()
                                                     .filter(e-> e.getId() == keywordId)
                                                     .map(e-> e.toDTO())
                                                     .findFirst()
                                                     .orElseThrow(()-> new NullPointerException("Unable to locate Chat Keyword with the specified id"));
         
         
         return targetChatKeyword;
    }

    @Override
    public int createChatKeyword(int chatId, Keyword keyword) throws ChatsServiceException {
        
         //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to create Chat Keyword an invalid Chat id was specified.");
        
         if(keyword == null)
            throw new NullPointerException("Unable to create Chat Keyword a null Chat instance was specified.");
         
         
         
         //prepare the route entity
         if(keyword.getId() >= 0 )
             keyword.setId(0);
         
         KeywordEntity keywordEntity = keyword.toEntity();
         
         this.keywordsRepository.save(keywordEntity);
         
         //Session session = this.sessionFactory.openSession();
         //Transaction tx = session.beginTransaction();
         
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         targetChatEntity.getKeywords()
                         .add(keywordEntity);
         
         
         this.chatsRepository.save(targetChatEntity);
       
         
         //tx.commit();
        
         
         return keywordEntity.getId();
    }

    @Override
    
    public int updateChatKeyword(int chatId, Keyword keyword) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to update Chat Keyowrd an invalid Chat id was specified.");
        
         if(keyword == null)
            throw new NullPointerException("Unable to update Chat Keyword a null Chat Keyword instance was specified.");
         
         
          //locate chat keyword in question
          ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
          KeywordEntity targetKeywordEntity = targetChatEntity.getKeywords()
                                                              .stream()
                                                              .filter(e-> e.getId() == keyword.getId())
                                                              .findFirst()
                                                              .orElseThrow(() -> new NullPointerException("Unable to update route, a route with the specified id could not be found."));
                     
          
          //if one or mopre values have changed then proceed to update route in the data
          boolean updateDetected = false;
          
          if(keyword.getName()!= null 
                  && !keyword.getName().equals(targetKeywordEntity.getName())){
              
              
              targetKeywordEntity.setName(keyword.getName());
              
              updateDetected = true;
          }
          
          if(keyword.getCommand() != null 
                  && !keyword.getCommand().equals(targetKeywordEntity.getCommand())){
              
              
              targetKeywordEntity.setCommand(keyword.getCommand());
              
              updateDetected = true;
          }
         
          
         
          if(keyword.isChatBlock() != targetKeywordEntity.isChatBlock()){
              
              
              targetKeywordEntity.setChatBlock(keyword.isChatBlock());
              
              updateDetected = true;
          }
          
          if(keyword.isChatInfo() != targetKeywordEntity.isChatInfo()){
              
              
              targetKeywordEntity.setChatInfo(keyword.isChatInfo());
              
              updateDetected = true;
          }
          
          if(keyword.isGlobal() != targetKeywordEntity.isGlobal()){
              
              
              targetKeywordEntity.setGlobal(keyword.isGlobal());
              
              updateDetected = true;
          }
          
          if(keyword.isNoCreditCheck() != targetKeywordEntity.isNoCreditCheck()){
              
              
              targetKeywordEntity.setNoCreditCheck(keyword.isNoCreditCheck());
              
              updateDetected = true;
          }
          
          
          if(updateDetected){
              
              this.chatsRepository.save(targetChatEntity);
              
              return targetKeywordEntity.getId();
          }
          else
              return -1;
    }

    @Override
    
    public int deleteChatKeyword(int chatId, int keywordId) throws ChatsServiceException {
        
         //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Keyword an invalid Chat id was specified.");
        
        if(keywordId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Keyword an invalid Chat Route id was specified.");
        
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        return this.chatsRepository.softDeleteChatKeyword(targetChatEntity,keywordId);
    }

    
    
    
    
    
    
    @Override
    public TransformerData<NoteName> listChatNoteNames(int chatId,ListOptions listOptions) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to list Chat NoteNames an invalid Chat id was specified.");
        
        
        //check cache first and return data from there where available..
        
        //otherwise...
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        List<NoteName> chatNoteNameList = targetChatEntity.getNoteNames()
                                                          .stream()
                                                          .filter(!listOptions.isIncludeDeleted() ? e-> !e.isDeleted() : e->true)
                                                          .map(e-> e.toDTO())
                                                          .collect(Collectors.toList());
        
        
        //store keyword list data to cache so it is available to efficiently service any subsequent requests.
        
         chatNoteNameList = (listOptions.getResultsLimit() > 0) ? chatNoteNameList.subList(0,listOptions.getResultsLimit()) : chatNoteNameList;
        
        
        //crucially here we DECOATRE the raw list data with one or more transformers which will 
        //each be applied to the data in the order in which they are specified as required, inside to out.
        //The data tranformers will be applied in the following order:
        //1)Filter
        //2)Sort
        //3)Paginate
        
        //Filter...
        TransformerData<NoteName> transformerData = new TransformerData(chatNoteNameList,NoteName.class);
        FilteringDataTransformer filteringDataTransformer = new FilteringDataTransformer<>(transformerData,listOptions.getFilterByNameValuePairs());
        
        //DECORATE Filter with Sort.
        SortingDataTransformer sortingDataTransformer = new SortingDataTransformer(filteringDataTransformer,listOptions.getOrderByNameValuePairs());
        
        //DECORATE Sort with Pagination. 
        PaginationDataTransformer<NoteName> paginationDataTransformer = new PaginationDataTransformer(sortingDataTransformer,listOptions.getPaginationNameValuePairs());
       
        //call paginate which will inturn call all nested transformation routines.
        return paginationDataTransformer.transform();
        
   
    }

    @Override
    public NoteName getChatNoteName(int chatId, int noteNameId) throws ChatsServiceException {
        
         //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to get Chat NoteName an invalid Chat id was specified.");
        
        if(noteNameId<= 0)
            throw new ChatsServiceException("Unable to get Chat Keyword an invalid Chat NoteName id was specified.");
        
        
        //check cache return entry from there where available
        
        
        //otherwise attempt to fetch the entry from the underlying  datastore
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         NoteName targetChatNoteName = targetChatEntity.getNoteNames()
                                                      .stream()
                                                      .filter(e-> e.getId() == noteNameId)
                                                      .map(e-> e.toDTO())
                                                      .findFirst()
                                                      .orElseThrow(()-> new NullPointerException("Unable to locate Chat Keyword with the specified id"));
         
         
         return targetChatNoteName;
    }

    @Override
    
    public int createChatNoteName(int chatId, NoteName noteName) throws ChatsServiceException {
      
         //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to create Chat NoteName an invalid Chat id was specified.");
        
         if(noteName == null)
            throw new NullPointerException("Unable to create Chat NoteName a null Chat NoteName instance was specified.");
         
         
         
         //prepare the route entity
         NoteNameEntity noteNameEntity = noteName.toEntity();
         if(noteNameEntity.getId() > 0)
             noteNameEntity.setId(0);
         
         
         this.notenamesRepository.save(noteNameEntity);
         
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         targetChatEntity.getNoteNames()
                         .add(noteNameEntity);
         
         
         this.chatsRepository.save(targetChatEntity);
                                                  
         
         return noteNameEntity.getId();
    }

    @Override
    
    public int updateChatNoteName(int chatId, NoteName noteName) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to update Chat NoteName an invalid Chat id was specified.");
        
         if(noteName == null)
            throw new NullPointerException("Unable to update Chat NoteName a null Chat NoteName instance was specified.");
         
         
          //locate chat keyword in question
          ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
          NoteNameEntity targetNoteNameEntity = targetChatEntity.getNoteNames()
                                                               .stream()
                                                               .filter(e-> e.getId() == noteName.getId())
                                                               .findFirst()
                                                               .orElseThrow(() -> new NullPointerException("Unable to update NoteName, a NoteName with the specified id could not be found."));
                     
          
          //if one or mopre values have changed then proceed to update route in the data
          boolean updateDetected = false;
          
          if(noteName.getName()!= null 
                  && !noteName.getName().equals(targetNoteNameEntity.getName())){
              
              
              targetNoteNameEntity.setName(noteName.getName());
              
              updateDetected = true;
          }
          
          if(noteName.getOrdinal() != targetNoteNameEntity.getOrdinal()){
              
              
              targetNoteNameEntity.setOrdinal(noteName.getOrdinal());
              
              updateDetected = true;
          }
          
          

          if(updateDetected){
              
              this.chatsRepository.save(targetChatEntity);
              
              return targetNoteNameEntity.getId();
          }
          else
              return -1;
    }

    @Override
    public int deleteChatNoteName(int chatId, int noteNamesId) throws ChatsServiceException {
       
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to delete Chat NoteName an invalid Chat id was specified.");
        
        if(noteNamesId <= 0)
            throw new ChatsServiceException("Unable to delete Chat NoteName an invalid Chat NoteName id was specified.");
        
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        return this.chatsRepository.softDeleteChatNoteName(targetChatEntity,noteNamesId);
    }

    
    
    
    
    
    @Override
    public TransformerData<Promo> listChatPromos(int chatId,ListOptions listOptions) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to list Chat Promo an invalid Chat id was specified.");
        
        
        //check cache first and return data from there where available..
        
        //otherwise...
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        List<Promo> chatPromoList = targetChatEntity.getPromos()
                                                    .stream()
                                                    .filter(!listOptions.isIncludeDeleted()? e-> !e.isDeleted() : e->true)
                                                    .map(e-> e.toDTO())
                                                    .collect(Collectors.toList());
        
        
        //store keyword list data to cache so it is available to efficiently service any subsequent requests.
        
        chatPromoList = (listOptions.getResultsLimit() > 0) ? chatPromoList.subList(0,listOptions.getResultsLimit()) : chatPromoList;
        
        
        //crucially here we DECOATRE the raw list data with one or more transformers which will 
        //each be applied to the data in the order in which they are specified as required, inside to out.
        //The data tranformers will be applied in the following order:
        //1)Filter
        //2)Sort
        //3)Paginate
        
        //Filter...
        TransformerData<Promo> transformerData = new TransformerData(chatPromoList,Promo.class);
        FilteringDataTransformer filteringDataTransformer = new FilteringDataTransformer<>(transformerData,listOptions.getFilterByNameValuePairs());
        
        //DECORATE Filter with Sort.
        SortingDataTransformer sortingDataTransformer = new SortingDataTransformer(filteringDataTransformer,listOptions.getOrderByNameValuePairs());
        
        //DECORATE Sort with Pagination. 
        PaginationDataTransformer<Promo> paginationDataTransformer = new PaginationDataTransformer(sortingDataTransformer,listOptions.getPaginationNameValuePairs());
       
        //call paginate which will inturn call all nested transformation routines.
        return paginationDataTransformer.transform();
    }

    
    @Override
    public Promo getChatPromo(int chatId, int promoId) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to get Chat Promo an invalid Chat id was specified.");
        
        if(promoId <= 0)
            throw new ChatsServiceException("Unable to get Chat Promo an invalid Chat Promo id was specified.");
        
        
        //check cache return entry from there where available
        
        
        //otherwise attempt to fetch the entry from the underlying  datastore
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         Promo targetChatPromo = targetChatEntity.getPromos()
                                                 .stream()
                                                 .filter(e-> e.getId() == promoId)
                                                 .map(e-> e.toDTO())
                                                 .findFirst()
                                                 .orElseThrow(()-> new NullPointerException("Unable to locate Chat Promo with the specified id"));
         
         
         return targetChatPromo;
    }

    
    @Override
    
    public int createChatPromo(int chatId, Promo promo) throws ChatsServiceException {
        
         //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to create Chat Promo an invalid Chat id was specified.");
        
         if(promo == null)
            throw new NullPointerException("Unable to create Chat Promo a null Chat Promo instance was specified.");
         
         
         
         //prepare the route entity
         PromoEntity promoEntity = promo.toEntity();
         if(promoEntity.getId() > 0)
             promoEntity.setId(0);
         
         this.promosRepository.save(promoEntity);
         
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         targetChatEntity.getPromos()
                         .add(promoEntity);
         
         
         this.chatsRepository.save(targetChatEntity);
                                                  
         
         return promoEntity.getId();
    }

    @Override
    
    public int updateChatPromo(int chatId, Promo promo) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to update Chat Promo an invalid Chat id was specified.");
        
         if(promo == null)
            throw new NullPointerException("Unable to update Chat Promo a null Chat NoteName instance was specified.");
         
         
          //locate chat keyword in question
          ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
          PromoEntity targetPromoEntity = targetChatEntity.getPromos()
                                                          .stream()
                                                          .filter(e-> e.getId() == promo.getId())
                                                          .findFirst()
                                                          .orElseThrow(() -> new NullPointerException("Unable to update Promo, a Promo with the specified id could not be found."));
                     
          
          //if one or mopre values have changed then proceed to update route in the data
          boolean updateDetected = false;
          
          if(promo.getName()!= null 
                  && !promo.getName().equals(targetPromoEntity.getName())){
              
              
              targetPromoEntity.setName(promo.getName());
              
              updateDetected = true;
          }
          
          if(promo.getAlreadyClaimedText()!= null 
                  && !promo.getAlreadyClaimedText().equals(targetPromoEntity.getAlreadyClaimedText())){
              
              
              targetPromoEntity.setAlreadyClaimedText(promo.getAlreadyClaimedText());
              
              updateDetected = true;
          }
          
          
          if(promo.getAmount()!= null 
                  && !promo.getAmount().equals(targetPromoEntity.getAmount())){
              
              
              targetPromoEntity.setAmount(promo.getAmount());
              
              updateDetected = true;
          }
          
          if(promo.getDescription() != null 
                  && !promo.getDescription().equals(targetPromoEntity.getDescription())){
              
              
              targetPromoEntity.setDescription(promo.getDescription());
              
              updateDetected = true;
          }
          
          if(promo.getPromoEndedText() != null 
                  && !promo.getPromoEndedText().equals(targetPromoEntity.getPromoEndedText())){
              
              
              targetPromoEntity.setPromoEndedText(promo.getPromoEndedText());
              
              updateDetected = true;
          }
          
          
          if(promo.getPromoNotStartedText() != null 
                  && !promo.getPromoNotStartedText().equals(targetPromoEntity.getPromoNotStartedText())){
              
              
              targetPromoEntity.setPromoNotStartedText(promo.getPromoNotStartedText());
              
              updateDetected = true;
          }
          
          
          if(promo.getEndTime() != null 
                  && !promo.getEndTime().equals(targetPromoEntity.getEndTime())){
              
              
              targetPromoEntity.setEndTime(promo.getEndTime());
              
              updateDetected = true;
          }
          
          
          
          
          if(promo.getStartTime() != null 
                  && !promo.getStartTime().equals(targetPromoEntity.getStartTime())){
              
              
              targetPromoEntity.setStartTime(promo.getStartTime());
              
              updateDetected = true;
          }
          
          
           if(promo.getSuccessText() != null 
                  && !promo.getSuccessText().equals(targetPromoEntity.getSuccessText())){
              
              
              targetPromoEntity.setSuccessText(promo.getSuccessText());
              
              updateDetected = true;
          }
          
          
          if(promo.isJoinChat() != targetPromoEntity.isJoinChat()){
              
              
              targetPromoEntity.setJoinChat(promo.isJoinChat());
              
              updateDetected = true;
          }
          
          if(promo.isJoinDating() != targetPromoEntity.isJoinDating()){
              
              
              targetPromoEntity.setJoinDating(promo.isJoinDating());
              
              updateDetected = true;
          }
          
          

          if(updateDetected){
              
              this.chatsRepository.save(targetChatEntity);
              
              return targetPromoEntity.getId();
          }
          else
              return -1;
    }

    @Override
    public int deleteChatPromo(int chatId, int promoId) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Promo an invalid Chat id was specified.");
        
        if(promoId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Promo an invalid Chat Promo id was specified.");
        
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        return this.chatsRepository.softDeleteChatPromo(targetChatEntity,promoId);
    }

    
    
    
    
    @Override
    public TransformerData<Category> listChatCategories(int chatId,ListOptions listOptions) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to list Chat Categories an invalid Chat id was specified.");
        
        
        //check cache first and return data from there where available..
        
        //otherwise...
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        List<Category> chatCategoriesList = targetChatEntity.getCategories()
                                                            .stream()
                                                            .filter(!listOptions.isIncludeDeleted() ? e-> !e.isDeleted() : e->true)
                                                            .map(e-> e.toDTO())
                                                            .collect(Collectors.toList());
        
        
        //store keyword list data to cache so it is available to efficiently service any subsequent requests.
        
        chatCategoriesList = (listOptions.getResultsLimit() > 0) ? chatCategoriesList.subList(0,listOptions.getResultsLimit()) : chatCategoriesList;
        
        
        //crucially here we DECOATRE the raw list data with one or more transformers which will 
        //each be applied to the data in the order in which they are specified as required, inside to out.
        //The data tranformers will be applied in the following order:
        //1)Filter
        //2)Sort
        //3)Paginate
        
        //Filter...
        TransformerData<Category> transformerData = new TransformerData(chatCategoriesList,Category.class);
        FilteringDataTransformer filteringDataTransformer = new FilteringDataTransformer<>(transformerData,listOptions.getFilterByNameValuePairs());
        
        //DECORATE Filter with Sort.
        SortingDataTransformer sortingDataTransformer = new SortingDataTransformer(filteringDataTransformer,listOptions.getOrderByNameValuePairs());
        
        //DECORATE Sort with Pagination. 
        PaginationDataTransformer<Category> paginationDataTransformer = new PaginationDataTransformer(sortingDataTransformer,listOptions.getPaginationNameValuePairs());
       
        //call paginate which will inturn call all nested transformation routines.
        return paginationDataTransformer.transform();
    }

    @Override
    public Category getChatCategory(int chatId, int categoryId) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to get Chat Category an invalid Chat id was specified.");
        
        if(categoryId <= 0)
            throw new ChatsServiceException("Unable to get Chat Category an invalid Chat Category id was specified.");
        
        
        //check cache return entry from there where available
        
        
        //otherwise attempt to fetch the entry from the underlying  datastore
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         Category targetChatCategory = targetChatEntity.getCategories()
                                                       .stream()
                                                       .filter(e-> e.getId() == categoryId)
                                                       .map(e-> e.toDTO())
                                                       .findFirst()
                                                       .orElseThrow(()-> new NullPointerException("Unable to locate Chat Category with the specified id"));
         
         
         return targetChatCategory;
    }

    @Override
    
    public int createChatCategory(int chatId, Category category) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to create Chat Categroy an invalid Chat id was specified.");
        
         if(category == null)
            throw new NullPointerException("Unable to create Chat Category a null Chat Category instance was specified.");
         
         
         
         //prepare the route entity
         CategoryEntity categoryEntity = category.toEntity();
         if(categoryEntity.getId() > 0)
             categoryEntity.setId(0);
         
         
         this.categoryRepository.save(categoryEntity);
         
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         targetChatEntity.getCategories()
                         .add(categoryEntity);
         
         
         this.chatsRepository.save(targetChatEntity);
                                                  
         
         return categoryEntity.getId();
    }

    @Override
    
    public int updateChatCategory(int chatId, Category category) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to update Category, an invalid Chat id was specified.");
        
        if(category == null)
           throw new NullPointerException("Unable to update  Category, a null entry was specified.");
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        
        //attempt to locate network to update
        CategoryEntity targetCategoryEntity = targetChatEntity.getCategories()
                                                              .stream()
                                                              .filter(e-> e.getId() == category.getId())
                                                              .findFirst()
                                                              .orElseThrow(()-> new NullPointerException("Category update failed, unable to locate Category with the specified id."));
         
        
        //if we have arrived here we have located the Network and thus providing
        //at least one field has been updated we proceed to update it to the underlying store.
        boolean updatedDetected = false;
        
        if(category.getDescription()!= null
                && !category.getDescription().equals(targetCategoryEntity.getDescription())){
            
            targetCategoryEntity.setDescription(category.getDescription());
            
            updatedDetected = true;
        }
        
        if(category.getName() != null
                && !category.getName().equals(targetCategoryEntity.getName())){
            
            targetCategoryEntity.setName(category.getName());
            
            updatedDetected = true;
        }
        
      
        
        
        //if update has been detect write to underlying store and flush cache.
        if(updatedDetected){
            
            this.chatsRepository.save(targetChatEntity);
            
            return targetCategoryEntity.getId();
        }
        else
            return -1;
    }

    @Override
    
    public int deleteChatCategory(int chatId, int categoryId) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Cateogry an invalid Chat id was specified.");
        
        if(categoryId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Category an invalid Chat Category id was specified.");
        
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        return this.chatsRepository.softDeleteChatCategory(targetChatEntity,categoryId);
    }

    
    
    
    
    
    
    @Override
    public TransformerData<Affiliate> listChatAffiliates(int chatId,ListOptions listOptions) throws ChatsServiceException {
       
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to list Chat Affiliate an invalid Chat id was specified.");
        
        
        //check cache first and return data from there where available..
        
        //otherwise...
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        List<Affiliate> chatAffiliateList = targetChatEntity.getAffiliates()
                                                            .stream()
                                                            .filter(!listOptions.isIncludeDeleted() ? e-> !e.isDeleted() : e->true)
                                                            .map(e-> e.toDTO())
                                                            .collect(Collectors.toList());
        
        
        //store keyword list data to cache so it is available to efficiently service any subsequent requests.
        
         chatAffiliateList = (listOptions.getResultsLimit() > 0) ? chatAffiliateList.subList(0,listOptions.getResultsLimit()) : chatAffiliateList;
        
        
        //crucially here we DECOATRE the raw list data with one or more transformers which will 
        //each be applied to the data in the order in which they are specified as required, inside to out.
        //The data tranformers will be applied in the following order:
        //1)Filter
        //2)Sort
        //3)Paginate
        
        //Filter...
        TransformerData<Affiliate> transformerData = new TransformerData(chatAffiliateList,Promo.class);
        FilteringDataTransformer filteringDataTransformer = new FilteringDataTransformer<>(transformerData,listOptions.getFilterByNameValuePairs());
        
        //DECORATE Filter with Sort.
        SortingDataTransformer sortingDataTransformer = new SortingDataTransformer(filteringDataTransformer,listOptions.getOrderByNameValuePairs());
        
        //DECORATE Sort with Pagination. 
        PaginationDataTransformer<Affiliate> paginationDataTransformer = new PaginationDataTransformer(sortingDataTransformer,listOptions.getPaginationNameValuePairs());
       
        //call paginate which will inturn call all nested transformation routines.
        return paginationDataTransformer.transform();
    }

    @Override
    public Affiliate getChatAffiliate(int chatId, int affiliateId) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to get Chat Affiliate an invalid Chat id was specified.");
        
        if(affiliateId <= 0)
            throw new ChatsServiceException("Unable to get Chat Affiliate an invalid Chat Affiliate id was specified.");
        
        
        //check cache return entry from there where available
        
        
        //otherwise attempt to fetch the entry from the underlying  datastore
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         Affiliate targetChatAffiliate = targetChatEntity.getAffiliates()
                                                         .stream()
                                                         .filter(e-> e.getId() == affiliateId)
                                                         .map(e-> e.toDTO())
                                                         .findFirst()
                                                         .orElseThrow(()-> new NullPointerException("Unable to locate Chat Affiliate with the specified id"));
         
         
         return targetChatAffiliate;
        
    }

    @Override
    
    public int createChatAffiliate(int chatId, Affiliate affiliate) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to create Chat Affilaite an invalid Chat id was specified.");
        
         if(affiliate == null)
            throw new NullPointerException("Unable to create Chat Affiliate a null Chat Affiliate instance was specified.");
         
         
         
         //prepare the route entity
         AffiliateEntity affiliateEntity = affiliate.toEntity();
         if(affiliateEntity.getId() > 0)
             affiliateEntity.setId(0);
         
         
        this.affiliateRepository.save(affiliateEntity);
         
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         targetChatEntity.getAffiliates()
                         .add(affiliateEntity);
         
         
         this.chatsRepository.save(targetChatEntity);
                                                  
         
         return affiliateEntity.getId();
    }

    @Override
    
    public int updateChatAffiliate(int chatId, Affiliate affiliate) throws ChatsServiceException {
        
         //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to update Affiliate, an invalid Chat id was specified.");
        
        if(affiliate == null)
           throw new NullPointerException("Unable to update Affiliate, a null entry was specified.");
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        
        //attempt to locate network to update
        AffiliateEntity targetAffiliateEntity = targetChatEntity.getAffiliates()
                                                                .stream()
                                                                .filter(e-> e.getId() == affiliate.getId())
                                                                .findFirst()
                                                                .orElseThrow(()-> new NullPointerException("Affiliate update failed, unable to locate Affiliate with the specified id."));
         
        
        //if we have arrived here we have located the Network and thus providing
        //at least one field has been updated we proceed to update it to the underlying store.
        boolean updatedDetected = false;
        
        if(affiliate.getDescription()!= null
                && !affiliate.getDescription().equals(targetAffiliateEntity.getDescription())){
            
            targetAffiliateEntity.setDescription(affiliate.getDescription());
            
            updatedDetected = true;
        }
        
        if(affiliate.getBrandName()!= null
                && !affiliate.getBrandName().equals(targetAffiliateEntity.getBrandName())){
            
            targetAffiliateEntity.setBrandName(affiliate.getBrandName());
            
            updatedDetected = true;
        }
        
        //to check affiliate category mapping we merely check that the entry is not
        //null and that the id is different where this is the case we may proceed to
        //update the affiliate category.
        if(affiliate.getCategory() != null
                && affiliate.getCategory().getId() != targetAffiliateEntity.getCategory().getId()){
            
            
            targetAffiliateEntity.setCategory(affiliate.getCategory().toEntity());
            
            updatedDetected = true;
            
        }
        
        
        //again here it will be sufficient to merely check that the provided
        //"scheme" sub entitiy is not null and has a differing id
         if(affiliate.getScheme() != null
                && affiliate.getScheme().getId() != targetAffiliateEntity.getScheme().getId()){
            
            
            targetAffiliateEntity.setScheme(affiliate.getScheme().toEntity());
            
            updatedDetected = true;
            
        }
        
        
        if(affiliate.getName()!= null
                && !affiliate.getName().equals(targetAffiliateEntity.getName())){
            
            targetAffiliateEntity.setName(affiliate.getName());
            
            updatedDetected = true;
        }
     
      
        
        
        //if update has been detect write to underlying store and flush cache.
        if(updatedDetected){
            
            this.chatsRepository.save(targetChatEntity);
            
            return targetAffiliateEntity.getId();
        }
        else
            return -1;
    }

    @Override
    
    public int deleteChatAffiliate(int chatId, int affiliateId) throws ChatsServiceException {
        
        //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Affiliate an invalid Chat id was specified.");
        
        if(affiliateId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Affiliate an invalid Chat Affiliate id was specified.");
        
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        return this.chatsRepository.softDeleteChatAffiliate(targetChatEntity,affiliateId);
        
    }

    
    
    
    @Override
    public TransformerData<Scheme> listChatSchemes(int chatId,ListOptions listOptions) throws ChatsServiceException {
        
         //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to list Chat Schemes an invalid Chat id was specified.");
        
        
        //check cache first and return data from there where available..
        
        //otherwise...
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        List<Scheme> chatSchemeList = targetChatEntity.getSchemes()
                                                      .stream()
                                                      .filter(!listOptions.isIncludeDeleted() ? e-> !e.isDeleted() : e->true)
                                                      .map(e-> e.toDTO())
                                                      .collect(Collectors.toList());
        
        
        //store keyword list data to cache so it is available to efficiently service any subsequent requests.
        
        chatSchemeList = (listOptions.getResultsLimit() > 0) ? chatSchemeList.subList(0,listOptions.getResultsLimit()) : chatSchemeList;
        
        
        //crucially here we DECOATRE the raw list data with one or more transformers which will 
        //each be applied to the data in the order in which they are specified as required, inside to out.
        //The data tranformers will be applied in the following order:
        //1)Filter
        //2)Sort
        //3)Paginate
        
        //Filter...
        TransformerData<Scheme> transformerData = new TransformerData(chatSchemeList,Scheme.class);
        FilteringDataTransformer filteringDataTransformer = new FilteringDataTransformer<>(transformerData,listOptions.getFilterByNameValuePairs());
        
        //DECORATE Filter with Sort.
        SortingDataTransformer sortingDataTransformer = new SortingDataTransformer(filteringDataTransformer,listOptions.getOrderByNameValuePairs());
        
        //DECORATE Sort with Pagination. 
        PaginationDataTransformer<Scheme> paginationDataTransformer = new PaginationDataTransformer(sortingDataTransformer,listOptions.getPaginationNameValuePairs());
       
        //call paginate which will inturn call all nested transformation routines.
        return paginationDataTransformer.transform();
    }

    @Override
    public Scheme getChatScheme(int chatId, int schemeId) throws ChatsServiceException {
       
          //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to get Chat Scheme an invalid Chat id was specified.");
        
        if(schemeId <= 0)
            throw new ChatsServiceException("Unable to get Chat Scheme an invalid Chat Scheme id was specified.");
        
        
        //check cache return entry from there where available
        
        
        //otherwise attempt to fetch the entry from the underlying  datastore
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         Scheme targetChatScheme = targetChatEntity.getSchemes()
                                                   .stream()
                                                   .filter(e-> e.getId() == schemeId)
                                                   .map(e-> e.toDTO())
                                                   .findFirst()
                                                   .orElseThrow(()-> new NullPointerException("Unable to locate Chat Scheme with the specified id"));
         
         
         return targetChatScheme;
    }

    @Override
    
    public int createChatScheme(int chatId, Scheme scheme) throws ChatsServiceException {
       
        
         //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to create Chat Scheme an invalid Chat id was specified.");
        
         if(scheme == null)
            throw new NullPointerException("Unable to create Chat Scheme a null Chat Scheme instance was specified.");
         
         
         
         //prepare the route entity
         SchemeEntity schemeEntity = scheme.toEntity();
         if(schemeEntity.getId() > 0)
             schemeEntity.setId(0);
         
         this.schemesRepository.save(schemeEntity);
         
         ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
         targetChatEntity.getSchemes()
                         .add(schemeEntity);
         
         
         this.chatsRepository.save(targetChatEntity);
                                                  
         
         return schemeEntity.getId();
    }

    @Override
    
    public int updateChatScheme(int chatId, Scheme scheme) throws ChatsServiceException {
       
         //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to update Scheme, an invalid Chat id was specified.");
        
        if(scheme == null)
           throw new NullPointerException("Unable to update Scheme, a null entry was specified.");
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        
        //attempt to locate network to update
        SchemeEntity targetSchemeEntity = targetChatEntity.getSchemes()
                                                          .stream()
                                                          .filter(e-> e.getId() == scheme.getId())
                                                          .findFirst()
                                                          .orElseThrow(()-> new NullPointerException("Scheme update failed, unable to locate Scheme with the specified id."));
         
        
        //if we have arrived here we have located the Network and thus providing
        //at least one field has been updated we proceed to update it to the underlying store.
        boolean updatedDetected = false;
        
        if(scheme.getDescription()!= null
                && !scheme.getDescription().equals(targetSchemeEntity.getDescription())){
            
            targetSchemeEntity.setDescription(scheme.getDescription());
            
            updatedDetected = true;
        }
        
        
         if(scheme.getName()!= null
                && !scheme.getName().equals(targetSchemeEntity.getName())){
            
            targetSchemeEntity.setName(scheme.getName());
            
            updatedDetected = true;
        }
        

        //if update has been detect write to underlying store and flush cache.
        if(updatedDetected){
            
            this.chatsRepository.save(targetChatEntity);
            
            return targetSchemeEntity.getId();
        }
        else
            return -1;
    }

    
    
    @Override
    public int deleteChatScheme(int chatId, int schemeId) throws ChatsServiceException {
        
         //undertake preliminary input validation
        if(chatId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Scheme an invalid Chat id was specified.");
        
        if(schemeId <= 0)
            throw new ChatsServiceException("Unable to delete Chat Scheme an invalid Chat Scheme id was specified.");
        
        
        ChatEntity targetChatEntity = this.loadChatEntry(chatId,false);
        return this.chatsRepository.softDeleteChatScheme(targetChatEntity,schemeId);
        
        
    }

    
    
    
    
    private List<ChatEntity> loadAllEntries(boolean includeDeleted,int resultLimit) {
       
        //for now (initial testing phase) we go direct to the database, in production
       //we will firstly want to check the cache. (Ignite, Memcached, etc);
       return (List<ChatEntity>)this.chatsRepository
                                    .findAll(includeDeleted,resultLimit);

    }

    
    /** 
     * Private Helper method, Attempts to load the Chat entry with the specified id
     * will firstly attempt to fetch the record from the in-memory cache and ONLY
     * fall back to the database where the entry is unavailable. (i.e a cache-miss occurs)
     */
    private ChatEntity loadChatEntry(int chatId,boolean includeDeleted) {
       
        //for now (initial build) we go direct tthe database
        ChatEntity chatEntity = this.chatsRepository
                                    //.findById(includeDeleted,chatId)
                                    .findById(chatId)
                                    .orElseThrow(() -> new NullPointerException("The specified Chat entry with id: "+chatId+" could not be found."));
        
       
        return chatEntity;
    }
    
    
   
    
    
   
    /** 
        Updates all non-null fields that have been modified in the new Chat
        to the existing ChatEntity, where no field have been modified then 
        a call to this method will essentially result in a no-op (i.e will return immediately) 
        NOTE: ONLY top level fields are updated via this method, each nested object will
        *     be updated via its own respective update method as per the public API
    */
    private int updateChatEntry(ChatEntity currentChatEntity, Chat newChat) {
        
        boolean changeDetected = false;
        
        if(newChat.getName() != null 
                && newChat.getName().length() > 0
                && !(newChat.getName().equals(currentChatEntity.getName()))){
            currentChatEntity.setName(newChat.getName());
        
            //set the change detect flag to true
            changeDetected = true;
         
        }
        
        
        if(newChat.getDescription() != null 
                && newChat.getDescription().length() > 0
                && !(newChat.getDescription().equals(currentChatEntity.getDescription()))){
            currentChatEntity.setDescription(newChat.getDescription());
            
            
            //set the change detect flag to true
            changeDetected = true;
            
        }
        
        
        if(newChat.getSlice() != null 
                && newChat.getSlice().length() > 0
                && !(newChat.getSlice().equals(currentChatEntity.getSlice()))){
            currentChatEntity.setSlice(newChat.getSlice());
            
            
            //set the change detect flag to true
            changeDetected = true;
            
        }
       
       
        //providing at least one value as changed as denoted byu a true "changeDetetcted" value
        //update the entity to the underlying database 
        if(changeDetected){
            
            //persit the change
            this.chatsRepository.save(currentChatEntity);
        
           
            //return the id of the entry we updated
            return currentChatEntity.getId();
            
        }
        //otherwise return -1 to indicate that no entries were updated
        else
            return -1;
    }

   
    
}
