/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.dtos;

import com.apnuk.services.chat.entities.ChatEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.List;

/**
 * Chat Data Transfer Object.
 * @author gilesthompson
 */
public class Chat implements Serializable {
    
    private int id;
    
    private String name;
    
    private String slice;
    
    private String description;
    
    private boolean deleted;
    
    @JsonInclude(Include.NON_NULL)
    private Settings settings;
    
    @JsonInclude(Include.NON_NULL)
    private List<Affiliate> affiliates;
    
    @JsonInclude(Include.NON_NULL)
    private List<Category> categories;
    
    @JsonInclude(Include.NON_NULL)
    private List<Keyword> keywords;
    
    @JsonInclude(Include.NON_NULL)
    private List<Network> networks;
    
    @JsonInclude(Include.NON_NULL)
    private List<NoteName> noteNames;
    
    @JsonInclude(Include.NON_NULL)
    private List<Promo> promos;
    
    @JsonInclude(Include.NON_NULL)
    private List<Route> routes;
    
    @JsonInclude(Include.NON_NULL)
    private List<Scheme> schemes;
    
    @JsonInclude(Include.NON_NULL)
    private List<Template> templates;

       
    public Chat() {
    }
    

    public Chat(int id, String name, String slice, String description, boolean deleted, Settings settings, List<Affiliate> affiliates, List<Category> categories, List<Keyword> keywords, List<Network> networks, List<NoteName> noteNames, List<Promo> promos, List<Route> routes, List<Scheme> schemes, List<Template> templates) {
        this.id = id;
        this.name = name;
        this.slice = slice;
        this.description = description;
        this.deleted = deleted;
        this.settings = settings;
        this.affiliates = affiliates;
        this.categories = categories;
        this.keywords = keywords;
        this.networks = networks;
        this.noteNames = noteNames;
        this.promos = promos;
        this.routes = routes;
        this.schemes = schemes;
        this.templates = templates;
       
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlice() {
        return slice;
    }

    public void setSlice(String slice) {
        this.slice = slice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public List<Affiliate> getAffiliates() {
        return affiliates;
    }

    public void setAffiliates(List<Affiliate> affiliates) {
        this.affiliates = affiliates;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public List<Network> getNetworks() {
        return networks;
    }

    public void setNetworks(List<Network> networks) {
        this.networks = networks;
    }

    public List<NoteName> getNoteNames() {
        return noteNames;
    }

    public void setNoteNames(List<NoteName> noteNames) {
        this.noteNames = noteNames;
    }

    public List<Promo> getPromos() {
        return promos;
    }

    public void setPromos(List<Promo> promos) {
        this.promos = promos;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Scheme> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<Scheme> schemes) {
        this.schemes = schemes;
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }
    
    
    
    
   /** 
      Returns a ChatEntity representation of this Chat model instance.
      @return ChatEntity A ChatEntity representation of this Chat model. 
    */
    public ChatEntity toEntity(){
        
        ChatEntity ce =  new ChatEntity();
        ce.setId(this.getId());
        ce.setName(this.getName());
        ce.setDescription(this.getDescription());
        ce.setSlice(this.getSlice());
        
        
        return ce;
    }
    
    
    
    
    
    
}
