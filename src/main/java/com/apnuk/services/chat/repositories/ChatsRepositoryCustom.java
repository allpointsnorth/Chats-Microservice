/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.repositories;

import com.apnuk.services.chat.entities.ChatEntity;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;


/**
 * Provides custom Chats domain-specific operations over and above
 * the default CRUD operations provided by the spring-data framework; the principal
 * reason for the creation and use of this custom repository is to account for the
 * fact that Chats Microservice data is only ever "Soft Deleted"  (i.e marked as deleted via
 * a boolean flag) rather than actually removed from the system and thus as well as the soft deletion methods
 * for each entity in the Chats object graph, "lookup-aware" operations also need to be provided that will
 * take into account whether or not an entry has been soft deleted.
 * 
 * 
 * @author gilesthompson
 */
public interface ChatsRepositoryCustom{
    
     //soft deletion
     public int softDeleteChat(ChatEntity chatEntityToDelete);
     
     public int softDeleteChatTemplate(ChatEntity chatEntity,int templateEntityId);
     
     public int softDeleteChatTemplateEntry(ChatEntity chatEntity,int templateEntityId,int templateEntryEntityId);
     
     public int softDeleteChatAffiliate(ChatEntity chatEntity,int affiliateEntityId);
     
     public int softDeleteChatCategory(ChatEntity chatEntity,int categoryEntityId);
     
     public int softDeleteChatKeyword(ChatEntity chatEntity,int keywordEntityId);
     
     public int softDeleteChatNetwork(ChatEntity chatEntity,int networkEntityId);
     
     public int softDeleteChatNoteName(ChatEntity chatEntity,int noteNameEntityId);
     
     public int softDeleteChatPromo(ChatEntity chatEntity,int promoEntityId);
     
     public int softDeleteChatRoute(ChatEntity chatEntity,int routeEntityId);
     
     public int softDeleteChatScheme(ChatEntity chatEntity,int schemeEntityId);
     
     
     //look ups that take soft deletion into account and provide ranging.
     public List<ChatEntity> findAll(boolean includeDeleted,int recordLimit);
     
     public Optional<ChatEntity> findById(boolean includeDeleted,int anId);
    
     public EntityManager getEntityManager();
     
     
     
     
   
     
     
     
     
     
     
     
     
     

    
}
