/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.repositories;

import com.apnuk.services.chat.entities.ChatEntity;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * A cache-centric implementation of a ChatsRepositoryCustom instance will decorate the
 standard ChatsRepositoryCustomNonCached to return entries from its internal cache
 where available and will only fall back to its delegate (i,e the standard ChatsRepositoryCustomNonCached)
 where a cache miss occurs in the event this happens the results obtained from the delegate will be
 used to back fill the cache so the data is available to service subsequent requests.
 * @author gilesthompson

@Repository
*/
public class ChatsRepositoryCustomImpl2 implements ChatsRepositoryCustom{
    
    
    private final ChatsRepositoryCustomImpl delegate;
    
    
    /**@Autowired */
    public ChatsRepositoryCustomImpl2(ChatsRepositoryCustomImpl delegate) {
        
        this.delegate = delegate;
        
    }
    
    @Override
    public int softDeleteChat(ChatEntity chatEntityToDelete) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int softDeleteChatTemplate(ChatEntity chatEntity, int templateEntityId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int softDeleteChatTemplateEntry(ChatEntity chatEntity, int templateEntityId, int templateEntryEntityId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int softDeleteChatAffiliate(ChatEntity chatEntity, int affiliateEntityId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int softDeleteChatCategory(ChatEntity chatEntity, int categoryEntityId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int softDeleteChatKeyword(ChatEntity chatEntity, int keywordEntityId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int softDeleteChatNetwork(ChatEntity chatEntity, int networkEntityId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int softDeleteChatNoteName(ChatEntity chatEntity, int noteNameEntityId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int softDeleteChatPromo(ChatEntity chatEntity, int promoEntityId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int softDeleteChatRoute(ChatEntity chatEntity, int routeEntityId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int softDeleteChatScheme(ChatEntity chatEntity, int schemeEntityId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ChatEntity> findAll(boolean includeDeleted, int recordLimit) {
        
        System.out.println("Cache Miss, loading records from data store");
        //actually here we will need to back-fill cache before returning.
        return this.delegate.findAll(includeDeleted,recordLimit);
        

    }

    
    @Override
    public Optional<ChatEntity> findById(boolean includeDeleted, int anId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityManager getEntityManager() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

   
    
}
