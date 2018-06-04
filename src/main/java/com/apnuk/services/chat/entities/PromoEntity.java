/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.entities;


import com.apnuk.services.chat.dtos.Promo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
  Models a single Chat PromoEntity.
 * @author gilesthompson
 */
@Entity
@Table(name="promos")
public class PromoEntity implements Serializable,SoftDeletable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String name;
    
    private String description;
    
    private Date startTime;
    
    private Date endTime;
    
    private BigDecimal amount;
    
    private String successText;
    
    private String alreadyClaimedText;
    
    private String promoNotStartedText;
    
    private String promoEndedText;
    
    private boolean joinChat;
    
    private boolean joinDating;
    
    private boolean deleted;
    
    

    public PromoEntity() {
    }

    public PromoEntity(int id, String name, String description, Date startTime, Date endTime, BigDecimal amount, String successText, String alreadyClaimedText, String promoNotStartedText, String promoEndedText, boolean joinChat, boolean joinDating, boolean deleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.amount = amount;
        this.successText = successText;
        this.alreadyClaimedText = alreadyClaimedText;
        this.promoNotStartedText = promoNotStartedText;
        this.promoEndedText = promoEndedText;
        this.joinChat = joinChat;
        this.joinDating = joinDating;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSuccessText() {
        return successText;
    }

    public void setSuccessText(String successText) {
        this.successText = successText;
    }

    public String getAlreadyClaimedText() {
        return alreadyClaimedText;
    }

    public void setAlreadyClaimedText(String alreadyClaimedText) {
        this.alreadyClaimedText = alreadyClaimedText;
    }

    public String getPromoNotStartedText() {
        return promoNotStartedText;
    }

    public void setPromoNotStartedText(String promoNotStartedText) {
        this.promoNotStartedText = promoNotStartedText;
    }

    public String getPromoEndedText() {
        return promoEndedText;
    }

    public void setPromoEndedText(String promoEndedText) {
        this.promoEndedText = promoEndedText;
    }

    public boolean isJoinChat() {
        return joinChat;
    }

    public void setJoinChat(boolean joinChat) {
        this.joinChat = joinChat;
    }

    public boolean isJoinDating() {
        return joinDating;
    }

    public void setJoinDating(boolean joinDating) {
        this.joinDating = joinDating;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    
    
    public Promo toDTO(){
        
        return new Promo(this.getId(),
                         this.getName(),
                         this.getDescription(),
                         this.getStartTime(),
                         this.getEndTime(),
                         this.getAmount(),
                         this.getSuccessText(),
                         this.getAlreadyClaimedText(),
                         this.getPromoNotStartedText(),
                         this.getPromoEndedText(),
                         this.isJoinChat(),
                         this.isJoinDating(),
                         this.isDeleted());
    }
          
}
