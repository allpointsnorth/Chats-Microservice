/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.dtos;

import com.apnuk.services.chat.entities.NoteNameEntity;
import java.io.Serializable;

/**
 * Models a single Chat NoteName.
 * @author gilesthompson
 */
public class NoteName implements Serializable{
    
    
    private int id;
    
    private String name;
    
    private int ordinal; 
  
    //will dictate how the ui renders the input for the NoteName field.
    private DataType aDataType;
    
    private boolean deleted;
    
    

    public NoteName() {
    }
    
    

    public NoteName(int id, String name, int ordinal, DataType aDataType) {
        this.id = id;
        this.name = name;
        this.ordinal = ordinal;
        this.aDataType = aDataType;
        
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

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public DataType getaDataType() {
        return aDataType;
    }

    public void setaDataType(DataType aDataType) {
        this.aDataType = aDataType;
    }
    


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    public NoteNameEntity toEntity(){
        
        return new NoteNameEntity(this.getId(),
                                  this.getName(),
                                  this.getOrdinal(),
                                  NoteNameEntity.DataType.valueOf(this.getaDataType().name()));
    }

    
    public enum DataType{
        
        TEXT,
        LONG_TEXT,
        DATE,
        HOURS,
        GEO
       
    }
}
