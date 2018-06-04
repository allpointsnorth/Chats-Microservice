/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.entities;

/**
 * This interface should be implemented by all Entity instance that have a requirement to be soft deleted.
 * @author gilesthompson
 */
public interface SoftDeletable {
    
    
    public void setDeleted(boolean deleted);
    
    public boolean isDeleted();
    
    public int getId();
}
