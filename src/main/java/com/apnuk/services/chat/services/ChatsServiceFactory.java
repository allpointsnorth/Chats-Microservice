/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.services;

/**
 * Responsible for returning new ChatsService implementation instances.
 * @author gilesthompson
 */
public class ChatsServiceFactory {
    
    public static ChatsService newInstance(){
        
        return new DefaultChatsService();
    }
}
