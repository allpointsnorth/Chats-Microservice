/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.entities;


import com.apnuk.services.chat.dtos.Keyword;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Models a single Chat KeywordEntity.
 * @author gilesthompson
 */
@Entity
@Table(name="keywords")
public class KeywordEntity implements Serializable,SoftDeletable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String name;
    
    private boolean global;
    
    private boolean chatBlock;
    
    private boolean chatInfo;
    
    private String command;
    
    private boolean noCreditCheck;
    
    private boolean deleted;
    
   

    public KeywordEntity() {
    }

    public KeywordEntity(int id, String name, boolean global, boolean chatBlock, boolean chatInfo, String command, boolean noCreditCheck) {
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    
    
    
    
    public Keyword toDTO(){
        
        return new Keyword(this.getId(),
                           this.getName(),
                           this.isGlobal(),
                           this.isChatBlock(),
                           this.isChatInfo(),
                           this.getCommand(),
                           this.isNoCreditCheck());
    }
    
}
