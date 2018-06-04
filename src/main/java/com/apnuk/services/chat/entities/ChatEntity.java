/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.entities;


import com.apnuk.services.chat.dtos.Affiliate;
import com.apnuk.services.chat.dtos.Category;
import com.apnuk.services.chat.dtos.Chat;
import com.apnuk.services.chat.dtos.Keyword;
import com.apnuk.services.chat.dtos.Network;
import com.apnuk.services.chat.dtos.NoteName;
import com.apnuk.services.chat.dtos.Promo;
import com.apnuk.services.chat.dtos.Route;
import com.apnuk.services.chat.dtos.Scheme;
import com.apnuk.services.chat.dtos.Settings;
import com.apnuk.services.chat.dtos.Template;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Chat entity class used for transferring data between the
 * application and its persistence layer.
 * @author gilesthompson
 */
@Entity
@Table(name="chats")
public class ChatEntity implements Serializable, SoftDeletable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String slice;
    
    private String name;
    
    private String description;
    
    private boolean deleted;
    
        
    
   
    /** Zero argument constructor for serialisation purposes. */
    public ChatEntity() {
    }

    public ChatEntity(int id, String slice, String description, boolean deleted, SettingsEntity settings, List<TemplateEntity> templates, List<SchemeEntity> schemes, List<NetworkEntity> networks, List<RouteEntity> routes, List<KeywordEntity> keywords, List<PromoEntity> promos, List<CategoryEntity> categories) {
        
        this.id = id;
        this.slice = slice;
        this.description = description;
        this.deleted = deleted;
        this.settings = settings;
        this.templates = templates;
        this.schemes = schemes;
        this.networks = networks;
        this.routes = routes;
        this.keywords = keywords;
        this.promos = promos;
        this.categories = categories;
    }

    
    
    
    
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    private SettingsEntity settings;
    
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    private List<TemplateEntity> templates;
    
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    private List<SchemeEntity> schemes;
    
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    private List<NetworkEntity> networks;
    
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    private List<RouteEntity> routes;
    
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    private List<KeywordEntity> keywords;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    private List<PromoEntity> promos;
    
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    private List<CategoryEntity> categories;
    
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    private List<AffiliateEntity> affiliates;
    
     @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    private List<NoteNameEntity> noteNames;
    

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

    public SettingsEntity getSettings() {
        return settings;
    }

    public void setSettings(SettingsEntity settings) {
        this.settings = settings;
    }

    public List<TemplateEntity> getTemplates() {
        return templates;
    }

    public void setTemplates(List<TemplateEntity> templates) {
        this.templates = templates;
    }

    public List<SchemeEntity> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<SchemeEntity> schemes) {
        this.schemes = schemes;
    }

    public List<NetworkEntity> getNetworks() {
        return networks;
    }

    public void setNetworks(List<NetworkEntity> networks) {
        this.networks = networks;
    }

    public List<RouteEntity> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteEntity> routes) {
        this.routes = routes;
    }

    public List<KeywordEntity> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<KeywordEntity> keywords) {
        this.keywords = keywords;
    }

    public List<PromoEntity> getPromos() {
        return promos;
    }

    public void setPromos(List<PromoEntity> promos) {
        this.promos = promos;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }
    
    public void setAffiliates(List<AffiliateEntity> affiliates) {
        this.affiliates = affiliates;
    }
    
    public List<AffiliateEntity> getAffiliates() {
        return affiliates;
    }

    public List<NoteNameEntity> getNoteNames() {
        return noteNames;
    }

    public void setNoteNames(List<NoteNameEntity> noteNames) {
        this.noteNames = noteNames;
    }
    
    
    /** 
        Converts the full ChatEntity object graph to a DTO.This may
        be used to pre-fetch all ChatEntity data for caching purposes. 
     */
    public Chat toFullDTO(){
        
        //convert top-level object via the alternate toDTO method.
        Chat chat = this.toDTO();
        
        List<Affiliate> affiliatesL = this.getAffiliates()
                                          .stream()
                                          .map(e-> e.toDTO())
                                          .collect(Collectors.toList());
        
        
        List<Category> categoriesL = this.getCategories()
                                        .stream()
                                        .map(e-> e.toDTO())
                                        .collect(Collectors.toList());
        
        
        List<Keyword> keywordsL = this.getKeywords()
                                      .stream()
                                      .map(e-> e.toDTO())
                                      .collect(Collectors.toList());
        
        
        List<Network> networksL = this.getNetworks()
                                      .stream()
                                      .map(e-> e.toDTO())
                                      .collect(Collectors.toList());
        
        
        List<NoteName> noteNamesL = this.getNoteNames()
                                        .stream()
                                        .map(e-> e.toDTO())
                                        .collect(Collectors.toList());
        
        
        
        List<Promo> promosL = this.getPromos()
                                  .stream()
                                  .map(e-> e.toDTO())
                                  .collect(Collectors.toList());
        
        
        List<Route> routesL = this.getRoutes()
                                  .stream()
                                  .map(e-> e.toDTO())
                                  .collect(Collectors.toList());
        
        
        List<Scheme> schemeL = this.getSchemes()
                                   .stream()
                                   .map(e-> e.toDTO())
                                   .collect(Collectors.toList());
        
        
        List<Template> templateL = this.getTemplates()
                                       .stream()
                                       .map(e-> e.toDTO())
                                       .collect(Collectors.toList());
        
        
        Settings settingsM = null;
        if(this.getSettings() != null)
             settingsM = this.getSettings().toDTO();
                                       
                                        
        chat.setAffiliates(affiliatesL);
        chat.setCategories(categoriesL);
        chat.setKeywords(keywordsL);
        chat.setNetworks(networksL);
        chat.setNoteNames(noteNamesL);
        chat.setPromos(promosL);
        chat.setRoutes(routesL);
        chat.setSchemes(schemeL);
        chat.setTemplates(templateL);
        
        if(settingsM != null)
            chat.setSettings(settingsM);
        
         
       
        return chat;
        
        
    }
    
    
    /**
       Returns a Data Transfer Object (DTO) representation of this ChatEntity; this
       "toDTO variant only converts the top-level Chat entity to a DTO, all
       sub entities are ignored. 
    */
    public Chat toDTO(){
        
        Chat chat = new Chat();
        chat.setId(this.getId());
        chat.setName(this.getName());
        chat.setDescription(this.getDescription());
        chat.setDeleted(this.isDeleted());
        return chat;

    }
    
    
    
    
    
    
    
    
    
}
