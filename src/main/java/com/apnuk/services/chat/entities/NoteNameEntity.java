/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.entities;


import com.apnuk.services.chat.dtos.NoteName;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Models a single Chat NoteNameEntity.
 * @author gilesthompson
 */
@Entity
@Table(name="note_names")
public class NoteNameEntity implements Serializable, SoftDeletable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String name;
    
    private int ordinal; 
  
    //will dictate how the ui renders the input for the NoteNameEntity field.
    private DataType aDataType;
    
    private boolean deleted;

    public NoteNameEntity() {
    }
   

    public NoteNameEntity(int id, String name, int ordinal, DataType aDataType) {
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
    
    
    public NoteName toDTO(){
        
        return new NoteName(this.getId(),
                            this.getName(),
                            this.getOrdinal(),
                            NoteName.DataType.valueOf(this.getaDataType().name()));
    }

    
    public enum DataType{
        
        TEXT,
        LONG_TEXT,
        DATE,
        HOURS,
        GEO
       
    }
}
