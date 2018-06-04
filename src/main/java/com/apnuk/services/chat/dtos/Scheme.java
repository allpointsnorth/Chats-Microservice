/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.dtos;

import com.apnuk.services.chat.entities.SchemeEntity;
import java.io.Serializable;

/**
 * Models a single Chat Scheme.
 * @author gilesthompson
 */
public class Scheme implements Serializable {
    
    
    private int id;
    
    private String name;
    
    private String description;
    
    private boolean deleted;
    
        
    
    

    public Scheme() {
    }
    
    public Scheme(int id, String name, String description, boolean deleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deleted = deleted;
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
    
    
    
    public SchemeEntity toEntity(){
        
        return new SchemeEntity(this.getId(),
                                this.getName(),
                                this.getDescription(),
                                this.isDeleted());
    }
}
