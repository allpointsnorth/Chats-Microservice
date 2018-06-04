/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.repositories;

import com.apnuk.services.chat.entities.ChatEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * ChatProvider Data Access Object used by the application to issue
 * calls into the persistence layer in an abstracted, platform agnostic
 * manner; the application of the Data Access Object patterns enables
 * the underlying data store and persistence frameworks to be modified
 * as required completely transparently to the application.
 * @author gilesthompson
 */
@Repository
public interface ChatsRepository extends CrudRepository<ChatEntity,Integer>,ChatsRepositoryCustom{
   
            
    
}
