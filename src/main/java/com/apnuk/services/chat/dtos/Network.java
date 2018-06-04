/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.dtos;

import com.apnuk.services.chat.entities.NetworkEntity;
import java.io.Serializable;

/**
 * Models a single ChatNetwork, a Chat may have several networks
 * associated with it at any one time.
 * @author gilesthompson
 */
public class Network implements Serializable {
    
    
    private int id;
    
    private String network;
    
    private boolean allowReverseBill;
    
    private boolean allowPrePay;
    
    private boolean deleted;
    
    

    public Network() {
    }
    
    

    public Network(int id, String network, boolean allowReverseBill, boolean allowPrePay, boolean deleted) {
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    
    public NetworkEntity toEntity(){
        
        return new NetworkEntity(this.getId(),
                                 this.getNetwork(),
                                 this.isAllowReverseBill(),
                                 this.isAllowPrePay(),
                                 this.isDeleted());
    }
    
}
