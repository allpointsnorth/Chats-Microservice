/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.entities;


import com.apnuk.services.chat.dtos.Route;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Models a single Chat RouteEntity.
 * @author gilesthompson
 */
@Entity
@Table(name="routes")
public class RouteEntity implements Serializable,SoftDeletable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String routeName;
    
    private BigDecimal mmsCost;
    
    private BigDecimal smsCost;
    
    private BigDecimal inRev;
    
    private BigDecimal outRev;
    
    private boolean deleted;
    
    

    public RouteEntity() {
    }

    public RouteEntity(int id, String routeName, BigDecimal mmsCost, BigDecimal smsCost, BigDecimal inRev, BigDecimal outRev, boolean deleted) {
        this.id = id;
        this.routeName = routeName;
        this.mmsCost = mmsCost;
        this.smsCost = smsCost;
        this.inRev = inRev;
        this.outRev = outRev;
        this.deleted = deleted;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

 
   
    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public BigDecimal getMmsCost() {
        return mmsCost;
    }

    public void setMmsCost(BigDecimal mmsCost) {
        this.mmsCost = mmsCost;
    }

    public BigDecimal getSmsCost() {
        return smsCost;
    }

    public void setSmsCost(BigDecimal smsCost) {
        this.smsCost = smsCost;
    }

    public BigDecimal getInRev() {
        return inRev;
    }

    public void setInRev(BigDecimal inRev) {
        this.inRev = inRev;
    }

    public BigDecimal getOutRev() {
        return outRev;
    }

    public void setOutRev(BigDecimal outRev) {
        this.outRev = outRev;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    
    
    public Route toDTO(){
        
        return new Route(this.getId(),
                         this.getRouteName(),
                         this.getMmsCost(),
                         this.getSmsCost(),
                         this.getInRev(),
                         this.getOutRev(),
                         this.isDeleted());
    }
    
    
}
