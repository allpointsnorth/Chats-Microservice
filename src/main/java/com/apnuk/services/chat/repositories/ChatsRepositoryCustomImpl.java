/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.repositories;

import com.apnuk.services.chat.entities.ChatEntity;
import com.apnuk.services.chat.entities.SoftDeletable;
import com.apnuk.services.chat.entities.TemplateEntity;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;



import org.springframework.stereotype.Repository;

/**
 * Reference implementation of a ChatsRepositoryCustom custom component.
 * @author gilesthompson
 */
@Repository
public class ChatsRepositoryCustomImpl implements ChatsRepositoryCustom{
    
    @PersistenceContext
    EntityManager entityManager;

    
    @Override
    public int softDeleteChat(ChatEntity chatEntityToDelete) {
        
        
        chatEntityToDelete.setDeleted(true);
        this.entityManager.persist(chatEntityToDelete);
        
        return chatEntityToDelete.getId();
                  
    }

    
    
    @Override
    public int softDeleteChatTemplate(ChatEntity chatEntity,int templateEntityId) {
        
     
        return this.softDeleteEntry(chatEntity,chatEntity.getTemplates(),templateEntityId);
        
 
    }
    
    
    @Override
    public int softDeleteChatAffiliate(ChatEntity chatEntity, int affiliateEntityId) {
       
        return this.softDeleteEntry(chatEntity,chatEntity.getAffiliates(),affiliateEntityId);
    }

    @Override
    public int softDeleteChatCategory(ChatEntity chatEntity, int categoryEntityId) {
        
       return this.softDeleteEntry(chatEntity,chatEntity.getCategories(),categoryEntityId);
    }

    
    @Override
    public int softDeleteChatKeyword(ChatEntity chatEntity, int keywordEntityId) {
       
      
      return this.softDeleteEntry(chatEntity,chatEntity.getKeywords(),keywordEntityId);
         
    }

    @Override
    public int softDeleteChatNetwork(ChatEntity chatEntity, int networkEntityId) {
       
         return this.softDeleteEntry(chatEntity,chatEntity.getNetworks(),networkEntityId);
    }

    @Override
    public int softDeleteChatNoteName(ChatEntity chatEntity, int noteNameEntityId) {
        
        return this.softDeleteEntry(chatEntity,chatEntity.getNoteNames(),noteNameEntityId);
    }

    @Override
    public int softDeleteChatPromo(ChatEntity chatEntity, int promoEntityId) {
        
       return this.softDeleteEntry(chatEntity,chatEntity.getPromos(),promoEntityId);
    }

    @Override
    public int softDeleteChatRoute(ChatEntity chatEntity, int routeEntityId) {
        
       return this.softDeleteEntry(chatEntity,chatEntity.getRoutes(),routeEntityId);
    }

    
    
    @Override
    public int softDeleteChatScheme(ChatEntity chatEntity, int schemeEntityId) {
        
        return this.softDeleteEntry(chatEntity,chatEntity.getSchemes(),schemeEntityId);
    }
   
    
    
    
    
    @Override
    public int softDeleteChatTemplateEntry(ChatEntity chatEntity,int templateEntityId,int templateEntryEntityId) {
        
      //find template
      TemplateEntity targetTemplate = chatEntity.getTemplates()
                                                .stream()
                                                .filter(curChatTemplateEntity -> curChatTemplateEntity.getId() == templateEntityId)
                                                .findFirst()
                                                .orElseThrow(() -> new NullPointerException("Unable to locate Chat Template with the specified id."));
        
      
      //delete template entry.
      return this.softDeleteEntry(chatEntity,targetTemplate.getEntries(),templateEntryEntityId);
                                                            
    }

    
   

    
    
    /** 
        Locates and soft deletes an entry in any SoftDeletable collection, All Entity classes implement 
        the SoftDeletable interface and thus may be "deleted" by this method. 
    */
    private int softDeleteEntry(ChatEntity aChatEntity,List<? extends SoftDeletable> entityList,int targetId){
        
       SoftDeletable targetEntity = entityList.stream()
                                              .filter(curEntity -> curEntity.getId() == targetId)
                                              .findFirst()
                                              .orElseThrow(() -> new NullPointerException("Unable to locate entity with the specified id."));
       
       
       targetEntity.setDeleted(true);
       this.entityManager.persist(aChatEntity);
       return targetEntity.getId();
    }
    
    
    
    @Override
    public List<ChatEntity> findAll(boolean includeDeleted, int recordLimit) {
        
        return this.entityManager
                   .createQuery((includeDeleted) ? "SELECT a FROM ChatEntity a" : "SELECT a FROM ChatEntity a WHERE a.deleted = false")
                   .setMaxResults((recordLimit > 0) ? recordLimit : Integer.MAX_VALUE) //set result limit to integer max when not specified, which is essentially all records.
                   .getResultList();
                   
    }

  
   
    @Override
    public Optional<ChatEntity>findById(boolean includeDeleted, int anId) {
        
        List<ChatEntity> matchingResults = this.entityManager
                                               .createQuery((includeDeleted) ? "SELECT a FROM ChatEntity a WHERE a.id = "+anId : "SELECT a FROM ChatEntity a WHERE a.deleted = false AND a.id = "+anId)
                                               .getResultList();
                                        
        
         if(matchingResults.size() < 1)
             throw new NullPointerException("An entry could not be found with the specified id");
         
         
        ChatEntity ce = matchingResults.get(0);
        
        return Optional.ofNullable(ce);
        
       
    }
    
    
   
    
    public EntityManager getEntityManager(){
        
        return this.entityManager;
    }
   
    
    
    
    
}
