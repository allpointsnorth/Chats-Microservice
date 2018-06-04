/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.entities;


import com.apnuk.services.chat.dtos.Network;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Models a single ChatNetwork, a Chat may have several networks
 * associated with it at any one time.
 * @author gilesthompson
 */
@Entity
@Table(name="networks")
public class NetworkEntity implements Serializable,SoftDeletable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String network;
    
    private boolean allowReverseBill;
    
    private boolean allowPrePay;
    
    private boolean deleted;
    
        
    
    

    public NetworkEntity() {
    }
    
   

    public NetworkEntity(int id, String network, boolean allowReverseBill, boolean allowPrePay, boolean deleted) {
        this.id = id;
        this.network = network;
        this.allowReverseBill = allowReverseBill;
        this.allowPrePay = allowPrePay;
        this.deleted = deleted;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public boolean isAllowReverseBill() {
        return allowReverseBill;
    }

    public void setAllowReverseBill(boolean allowReverseBill) {
        this.allowReverseBill = allowReverseBill;
    }

    public boolean isAllowPrePay() {
        return allowPrePay;
    }

    public void setAllowPrePay(boolean allowPrePay) {
        this.allowPrePay = allowPrePay;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    
    public Network toDTO(){
        
        return new Network(this.getId(), 
                           this.getNetwork(), 
                           this.isAllowReverseBill(), 
                           this.isAllowPrePay(), 
                           this.isDeleted());
    }
    
}
