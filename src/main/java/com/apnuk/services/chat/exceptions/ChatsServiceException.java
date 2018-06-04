/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.exceptions;

/**
 * This exception is raised where a ChatsService encounters an unrecoverable error.
 * @author gilesthompson
 */
public class ChatsServiceException extends Exception{

    public ChatsServiceException() {
        
        super();
    }
    
    public ChatsServiceException(String message) {
        
        super(message);
    }
   
    
    public ChatsServiceException(String message,Throwable cause) {
        
        super(message,cause);
    }
    
    
    
}
