/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.dtos;

import com.apnuk.services.chat.entities.RouteEntity;
import java.math.BigDecimal;

/**
 * Models a single Chat Route.
 * @author gilesthompson
 */
public class Route {
    
    private int id;
    
    private String routeName;
    
    private BigDecimal mmsCost;
    
    private BigDecimal smsCost;
    
    private BigDecimal inRev;
    
    private BigDecimal outRev;
    
    private boolean deleted;

    
    
    
    

    public Route() {
    }
    
   

    public Route(int id, String routeName, BigDecimal mmsCost, BigDecimal smsCost, BigDecimal inRev, BigDecimal outRev, boolean deleted) {
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    
    
    public RouteEntity toEntity(){
        
        return new RouteEntity(this.getId(),
                               this.getRouteName(),
                               this.getMmsCost(),
                               this.getSmsCost(),
                               this.getInRev(),
                               this.getOutRev(),
                               this.isDeleted());
    }  
    
    
}
