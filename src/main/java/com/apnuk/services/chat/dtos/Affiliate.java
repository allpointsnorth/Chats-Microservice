/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.dtos;

import com.apnuk.services.chat.entities.AffiliateEntity;
import java.io.Serializable;

/**
 * Models a single Chat affiliate.
 * @author gilesthompson
 */
public class Affiliate implements Serializable {
    
    
    private int id;
    
    private Scheme scheme;
    
    private Category category;
    
    private String name;
    
    private String description;
    
    private String brandName;
    
    

    public Affiliate() {
    }
    
    

    public Affiliate(int id, Scheme scheme, Category category, String name, String description, String brandName) {
        this.id = id;
        this.scheme = scheme;
        this.category = category;
        this.name = name;
        this.description = description;
        this.brandName = brandName;
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    
    
    
    public AffiliateEntity toEntity(){
        
        return new AffiliateEntity(this.getId(),
                                   (this.getScheme() == null) ? null : this.getScheme().toEntity(),
                                   (this.getCategory() == null) ? null : this.getCategory().toEntity(),
                                   this.getName(),
                                   this.getDescription(),
                                   this.getBrandName());
    }
    
    
    
}
