/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.entities;


import com.apnuk.services.chat.dtos.TemplateEntry;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Models a single TemplateEntryEntity
 * @author gilesthompson
 */
@Entity
@Table(name="template_entries")
public class TemplateEntryEntity implements Serializable,SoftDeletable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String code;
    
    private String description;
    
    private String text;
    
    private boolean deleted;
    
    

    public TemplateEntryEntity() {
    }

    public TemplateEntryEntity(int id, String code, String description, String text, boolean deleted) {
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

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    
    public TemplateEntry toDTO(){
        
        return new TemplateEntry(this.getId(),
                                 this.getCode(), 
                                 this.getDescription(), 
                                 this.getText(), 
                                 this.isDeleted());
    }
            
      
}
