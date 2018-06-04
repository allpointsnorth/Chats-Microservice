/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.entities;


import com.apnuk.services.chat.dtos.Template;
import com.apnuk.services.chat.dtos.TemplateEntry;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Models a single Chat TemplateEntity.
 * @author gilesthompson
 */
@Entity
@Table(name="templates")
public class TemplateEntity implements Serializable,SoftDeletable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String code;
    
    private String description;
    
    private boolean deleted;
    
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    private List<TemplateEntryEntity> entries;
    
    

    public TemplateEntity() {
    }

    public TemplateEntity(int id, String code, String description, boolean deleted, List<TemplateEntryEntity> entries) {
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

    public List<TemplateEntryEntity> getEntries() {
        return entries;
    }

    public void setEntries(List<TemplateEntryEntity> entries) {
        this.entries = entries;
    }
    
    
    public Template toDTO(){
        
        
        return new Template(this.getId(),
                            this.getCode(),
                            this.getDescription(),
                            this.isDeleted(),
                            this.entriesToDTOs());
    }
    
    
    private List<TemplateEntry> entriesToDTOs(){
        
         
        
        List<TemplateEntry> templateEntries = this.getEntries().stream()
                                                               .map(curTemplateEntryEntity -> curTemplateEntryEntity.toDTO())
                                                               .collect(Collectors.toList());
       
        
        return templateEntries;
    }
    
    
   
}
