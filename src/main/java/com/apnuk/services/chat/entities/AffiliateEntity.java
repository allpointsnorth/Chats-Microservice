/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.entities;


import com.apnuk.services.chat.dtos.Affiliate;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Models a single Chat affiliate.
 * @author gilesthompson
 */
@Entity
@Table(name="affiliates")
public class AffiliateEntity implements Serializable, SoftDeletable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    private SchemeEntity scheme;
    
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    private CategoryEntity category;
    
    private String name;
    
    private String description;
    
    private String brandName;
    
    private boolean deleted;
    
    

    public AffiliateEntity() {
    }

    public AffiliateEntity(int id, SchemeEntity scheme, CategoryEntity category, String name, String description, String brandName) {
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

    public SchemeEntity getScheme() {
        return scheme;
    }

    public void setScheme(SchemeEntity scheme) {
        this.scheme = scheme;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
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

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    
    
    public Affiliate toDTO(){
        
        return new Affiliate(this.getId(),
                             this.getScheme().toDTO(),
                             this.getCategory().toDTO(),
                             this.getName(),
                             this.getDescription(), 
                             this.getBrandName());
    }
    
    
    
}
