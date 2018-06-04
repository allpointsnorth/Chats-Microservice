/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.dtos;

import com.apnuk.services.chat.entities.KeywordEntity;

/**
 * Models a single Chat Keyword.
 * @author gilesthompson
 */
public class Keyword {
    
    private int id;
    
    private String name;
    
    private boolean global;
    
    private boolean chatBlock;
    
    private boolean chatInfo;
    
    private String command;
    
    private boolean noCreditCheck;
    
    
    
    

    public Keyword() {
    }
    
    

    public Keyword(int id, String name, boolean global, boolean chatBlock, boolean chatInfo, String command, boolean noCreditCheck) {
        this.id = id;
        this.name = name;
        this.global = global;
        this.chatBlock = chatBlock;
        this.chatInfo = chatInfo;
        this.command = command;
        this.noCreditCheck = noCreditCheck;
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isChatBlock() {
        return chatBlock;
    }

    public void setChatBlock(boolean chatBlock) {
        this.chatBlock = chatBlock;
    }

    public boolean isChatInfo() {
        return chatInfo;
    }

    public void setChatInfo(boolean chatInfo) {
        this.chatInfo = chatInfo;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isNoCreditCheck() {
        return noCreditCheck;
    }

    public void setNoCreditCheck(boolean noCreditCheck) {
        this.noCreditCheck = noCreditCheck;
    }
    
    
    public KeywordEntity toEntity(){
        
        return new KeywordEntity(this.getId(), 
                                 this.getName(), 
                                 this.isGlobal(), 
                                 this.isChatBlock(), 
                                 this.isChatInfo(), 
                                 this.getCommand(), 
                                 this.isNoCreditCheck());
    }
    
}
