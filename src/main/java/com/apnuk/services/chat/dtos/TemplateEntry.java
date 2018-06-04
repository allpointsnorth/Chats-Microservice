/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.dtos;

import com.apnuk.services.chat.entities.TemplateEntryEntity;
import java.io.Serializable;

/**
 * Models a single TemplateEntry
 * @author gilesthompson
 */
public class TemplateEntry implements Serializable{
    
    private int id;
    
    private String code;
    
    private String description;
    
    private String text;
    
    private boolean deleted;
    
    

    public TemplateEntry() {
    }
    
    

    public TemplateEntry(int id, String code, String description, String text, boolean deleted) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.text = text;
        this.deleted = deleted;
       
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
            
      
    
    public TemplateEntryEntity toEntity(){
        
        return new TemplateEntryEntity(this.getId(), 
                                       this.getCode(), 
                                       this.getDescription(), 
                                       this.getText(), 
                                       this.isDeleted());
    }
}
