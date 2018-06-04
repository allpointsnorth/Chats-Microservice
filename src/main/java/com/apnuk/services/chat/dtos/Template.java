/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.dtos;

import com.apnuk.services.chat.entities.TemplateEntity;
import com.apnuk.services.chat.entities.TemplateEntryEntity;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Models a single Chat Template.
 * @author gilesthompson
 */
public class Template implements Serializable{
    
    private int id;
    
    private String code;
    
    private String description;
    
    private boolean deleted;
    
    private List<TemplateEntry> entries;
    
        
    public Template() {
    }
    
    

    public Template(int id, String code, String description, boolean deleted, List<TemplateEntry> entries) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.deleted = deleted;
        this.entries = entries;
       
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<TemplateEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<TemplateEntry> entries) {
        this.entries = entries;
    }
    
    public TemplateEntity toEntity(){
        
        return new TemplateEntity(this.getId(),
                                  this.getCode(),
                                  this.getDescription(),
                                  this.isDeleted(),
                                  this.entriesToEntities());
    }
    
    public List<TemplateEntryEntity> entriesToEntities(){
        
        if(this.getEntries() == null || this.getEntries().size() < 1)
            return null;
        
        
        return this.getEntries()
                   .stream()
                   .map(e -> e.toEntity())
                   .collect(Collectors.toList());
    }
    
}
