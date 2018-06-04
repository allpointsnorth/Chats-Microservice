/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.services;

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
import com.apnuk.services.chat.dtos.TemplateEntry;
import com.apnuk.services.chat.exceptions.ChatsServiceException;
import com.apnuk.services.chat.resources.ListOptions;
import com.apnuk.services.chat.utilities.transformations.TransformerData;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Chats service responsible for the creation and management of Chat data.
 * @author gilesthompson
 */
@Service
public interface ChatsService {
    
    //chat

    /**
     * Lists all currently registered chats
     * @param listOptions The options to apply to the listing operation.
     * @param includeDeleted flag which specifies whether or not previously deleted chats should be 
     *                       included in the results.
     * @param resultsLimit The maximum number of results to fetch, defaults to 1000 when not specified.
     * @return TransformerData<Chat> All currently registered Chats, with one or transformation applied
     *                               as specified in the provided ListOptions instance.
     * @throws ChatsServiceException Where an error occurs during the process of listing chats.
     *                          
     */
     public TransformerData<Chat> listChats(ListOptions listOptions) throws ChatsServiceException;
     
    /**
     * Gets the chat by the specified id
     * @param chatId The id of the chat to get
     * @param includeDeleted flag which specifies whether the get operation should include
     *                       previously deleted chats.
     * @return Chat The chat associated with the specified or null where no such Chat could be 
     *              found.
     * 
     * @throws ChatsServiceException Where an error occurs during the process of fetching the specified chat.
     */
     public Chat getChatById(int chatId,boolean includeDeleted) throws ChatsServiceException;
     
    /**
     * Creates new Chat.
     * @param chat The chat to append to the system.
     * @return String the Id of the newly created Chat.
     * @throws ChatsServiceException Where an error occurs during the process of creating the chat.
     */
     public int createChat(Chat chat) throws ChatsServiceException;
     
    /**
     * Updates the specified chat, the id associated with the supplied
     * chat must already exist.
     * @param chat The updated chat.
     * @return String the id of the chat that was updated or -1 where the 
     *                the id assigned to provided chat did not correspond to 
     *                a pre-existing chat.
     * 
     * @throws ChatsServiceException Where an error occurs during the process of updating the specified chat.
     */
     public int updateChat(Chat chat) throws ChatsServiceException;
     
    /**
     *Deletes the chat with the specified id from the system.
     * @param chatId The id of the chat to delete.
     * @return int The id of the chat that was deleted or -1 where the 
     *             chat could not be found.
     * 
     * @throws ChatsServiceException Where an error occurs during the process of deleting the specified chat.
     */
    public int deleteChat(int chatId) throws ChatsServiceException;
     
     
     //chat settings
    
    
    /** Creates new chat Settings and associates them with the 
        chat with the specified id; it is critical to note that
        only a single collection of settings may be associated with 
        a Chat at any one time and thus a call to this method will
        subsequently overwrite any existing settings associated with the
        chat.
        @param chatId The id of the Chat to associate the settings with.
        @param settings The settings to associate with the chat.
        @return int The id of the created Chat settings.
        @throws com.apnuk.services.chat.exceptions.ChatsServiceException Where an error occurs during the process of creating settings. */
    public int createChatSettings(int chatId,Settings settings) throws ChatsServiceException;
        
        
    
       

    /**
     * Gets the chat setting associated with the chat with the provided id;
     * chat settings are automatically created and associated with each new Chat
     * stored to the system.
     * @param chatId The id of the chat to fetch the settings for.
     * @return Settings The setting that relate to the Chat with the specified id.
     * @throws ChatsServiceException Where an error occurs during the process of fetching Settings
     *                               for the specified chat.
     */
     public Settings getChatSettings(int chatId) throws ChatsServiceException;
     
    /**
     * Updates the Settings for the Chat with the specified id.
     * @param chatId The id of the chat to update settings for 
     * @param settings The updated settings.
     * @return The id of the Chat that had it's settings updated.
     * 
     * @throws ChatsServiceException Where an error occurs during the process of updating settings
     *                               for the specified chat.
     */
    public int updateChatSettings(int chatId,Settings settings) throws ChatsServiceException;
     
     
     
     //chat templates

    /**
     * Gets all Templates associated with the chat with the specified id.
     * @param chatId Then id of the chat to get templates for.
     * @param includeDeleted indicates if deleted entries should be included in the results.
     * @return List<Template> List of templates associated with the chat with the specified id.
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch Templates 
     *                               for the Chat with the specified id.
     */
     public TransformerData<Template> listChatTemplates(int chatId,ListOptions listOptions) throws ChatsServiceException;
     
    /**
     * Gets the chat Template with the specified id associated with the Chat with 
     * the specified id.
     * @param chatId The id of the chat that the Template is associated with
     * @param templateId The id of the Template to fetch. 
     * @return Template The template associated with the specified id.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch Template with the provided id 
     *                               for the Chat with the specified id.
     */
     public Template getChatTemplate(int chatId, int templateId) throws ChatsServiceException;
     
    /**
     * Creates new Template and associates it with the chat with the specified id.
     * @param chatId The id of the chat to associate the template with.
     * @param template The new Template.
     * @return String the id assigned to the newly created Template.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to create Templates 
     *                               and associate it with the Chat with the specified id.
     */
     public int createChatTemplate(int chatId,Template template) throws ChatsServiceException;
     
    /**
     * Updates the chat Template associated with the chat with the specified id;
     * the specified template MUST have an id corresponding to a pre-existing
     * template in  the system
     * @param chatId The id of the chat that the Template to be updated is associated with.
     * @param template The updated template.
     * @return String the id of the template that was updated.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to update the specified Template 
     *                               of the Chat with the specified id.
     */
     public int updateChatTemplate(int chatId,Template template) throws ChatsServiceException;
     
    /**
     * Deletes the Template with the specified id from the Chat with the 
     * specified id.
     * @param chatId The id of the chat to which the template to be deleted relates.
     * @param templateId The id of the template to delete.
     * @return String the id of the deleted template.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to delete the Template
     *                               with the specified id from the Chat with the specified id.
     */
    public int deleteChatTemplate(int chatId,int templateId) throws ChatsServiceException;
     
    
    
     
     //chat template entries

    /**
     * List all TemplateEntries for the Template with the provided id associated with the Chat with 
     * the specified id.
     * @param chatId The id of the Chat the Template to list entries for relates.
     * @param templateId The id of the Template to list entries for.
     * @return List<TemplateEntry> List of Entries related to the template with the specified id.
     * @throws ChatsServiceException Where an error occurs during the process of listing entries
     *                               related to the specified Template.
     */
     public TransformerData<TemplateEntry> listChatTemplateEntries(int chatId,int templateId,ListOptions listOptions) throws ChatsServiceException;
     
    /**
     * Creates a new Template Entry and associates with the Template with the specified id.
     * @param chatId The of the Chat to which the Template relates.
     * @param templateId The id of the template to create an entry in.
     * @param templateEntry The entry to append to the template
     * @return  String The id of the newly created TemlateEntry.
     * @throws ChatsServiceException Where an error occurs during the process of creating the entry.
     */
    public int createChatTemplateEntry(int chatId,int templateId,TemplateEntry templateEntry) throws ChatsServiceException;
     
    /**
     * Updates a Template Entry.
     * @param chatId The of the Chat to which the Template relates.
     * @param templateId The id of the Template the Entry to be updated relates to.
     * @param templateEntry The updated Template Entry.
     * @return  String The id of the updated Template Entry
     * @throws ChatsServiceException Where an error occurs during the process of updating the Entry.
     */
    public int updateChatTemplateEntry(int chatId, int templateId, TemplateEntry templateEntry) throws ChatsServiceException;
     
    
     /**
     * Deletes a Template Entry.
     * @param chatId The of the Chat to which the Template relates.
     * @param templateId The id of the Template the Entry to be deleted relates to.
     * @param templateEntryId The id of the TemplateEntry to delete.
     * @return  String The id of the deleted Template Entry
     * @throws ChatsServiceException Where an error occurs during the process of deleting the Entry.
     */
    public int deleteChatTemplateEntry(int chatId, int templateId, int templateEntryId) throws ChatsServiceException;
     
     
    
    
    
     //chat networks
     
    
    /**
     * Gets all Networks associated with the chat with the specified id.
     * @param chatId Then id of the chat to get networks for.
     * @param includeDeleted indicates if deleted entries should be included in the results.
     * @return List<Network> List of networks associated with the chat with the specified id.
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch Networks 
     *                               for the Chat with the specified id.
     */

     public TransformerData<Network> listChatNetworks(int chatId,ListOptions listOptions) throws ChatsServiceException;
     
     
    /**
     * Creates new Network and associates it with the chat with the specified id.
     * @param chatId The id of the chat to associate the network with.
     * @param network The new Network.
     * @return String the id assigned to the newly created Network.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to create Networks 
     *                               and associate it with the Chat with the specified id.
     */
     public int createChatNetwork(int chatId,Network network) throws ChatsServiceException;
     
     
      /**
     * Updates the chat Network associated with the chat with the specified id;
     * the specified network MUST have an id corresponding to a pre-existing
     * network in  the system
     * @param chatId The id of the chat that the Network to be updated is associated with.
     * @param network The updated network.
     * @return String the id of the network that was updated.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to update the specified Network 
     *                               of the Chat with the specified id.
     */
     public int updateChatNetwork(int chatId,Network network) throws ChatsServiceException;
     
     
    /**
     * Deletes the Network with the specified id from the Chat with the 
     * specified id.
     * @param chatId The id of the chat to which the network to be deleted relates.
     * @param networkId The id of the network to delete.
     * @return String the id of the deleted network.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to delete the Network
     *                               with the specified id from the Chat with the specified id.
     */
     public int deleteChatNetwork(int chatId,int networkId) throws ChatsServiceException;
     
     
     
     
     //chat routes
     
     
    /**
     * Gets all Routes associated with the chat with the specified id.
     * @param chatId Then id of the chat to get routes for.
     * @param includeDeleted indicates if deleted entries should be included in the results.
     * @return List<Route> List of routes associated with the chat with the specified id.
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch Routes 
     *                               for the Chat with the specified id.
     */
     public TransformerData<Route> listChatRoutes(int chatId,ListOptions listOptions) throws ChatsServiceException;
      
     /**
     * Gets the chat Route with the specified id associated with the Chat with 
     * the specified id.
     * @param chatId The id of the chat that the Route is associated with
     * @param routeId The id of the Route to fetch. 
     * @return Route The route associated with the specified id.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch Route with the provided id 
     *                               for the Chat with the specified id.
     */
     public Route getChatRoute(int chatId,int routeId) throws ChatsServiceException;
     
     /**
     * Creates new Route and associates it with the chat with the specified id.
     * @param chatId The id of the chat to associate the route with.
     * @param route The new Route.
     * @return String the id assigned to the newly created Route.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to create Routes 
     *                               and associate it with the Chat with the specified id.
     */
     public int createChatRoute(int chatId,Route route) throws ChatsServiceException;
     
     /**
     * Updates the chat Route associated with the chat with the specified id;
     * the specified route MUST have an id corresponding to a pre-existing
     * route in  the system
     * @param chatId The id of the chat that the Route to be updated is associated with.
     * @param route The updated route.
     * @return String the id of the route that was updated.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to update the specified Route 
     *                               of the Chat with the specified id.
     */
     public int updateChatRoute(int chatId,Route route) throws ChatsServiceException;
     
     /**
     * Deletes the Route with the specified id from the Chat with the 
     * specified id.
     * @param chatId The id of the chat to which the route to be deleted relates.
     * @param routeId The id of the route to delete.
     * @return String the id of the deleted route.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to delete the Route
     *                               with the specified id from the Chat with the specified id.
     */
     public int deleteChatRoute(int chatId,int routeId) throws ChatsServiceException;
      
      
      
     
     
     
      //chat keywords
      
     
    /**
     * Gets all Keywords associated with the chat with the specified id.
     * @param chatId Then id of the chat to get keywords for.
     * @param includeDeleted indicates if deleted entries should be included in the results.
     * @return List<Keyword> List of keywords associated with the chat with the specified id.
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch Keywords 
     *                               for the Chat with the specified id.
     */
     public TransformerData<Keyword> listChatKeywords(int chatId,ListOptions listOptions) throws ChatsServiceException;
      
    /**
     * Gets the chat Keyword with the specified id associated with the Chat with 
     * the specified id.
     * @param chatId The id of the chat that the Keyword is associated with
     * @param keywordId The id of the Keyword to fetch. 
     * @return Keyword The keyword associated with the specified id.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch Keyword with the provided id 
     *                               for the Chat with the specified id.
     */
     public Keyword getChatKeyword(int chatId,int keywordId) throws ChatsServiceException;
     
    /**
     * Creates new Keyword and associates it with the chat with the specified id.
     * @param chatId The id of the chat to associate the keyword with.
     * @param keyword The new Keyword.
     * @return String the id assigned to the newly created Keyword.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to create Keywords 
     *                               and associate it with the Chat with the specified id.
     */
     public int createChatKeyword(int chatId,Keyword keyword) throws ChatsServiceException;
     
     /**
     * Updates the chat Keyword associated with the chat with the specified id;
     * the specified keyword MUST have an id corresponding to a pre-existing
     * keyword in  the system
     * @param chatId The id of the chat that the Keyword to be updated is associated with.
     * @param keyword The updated keyword.
     * @return String the id of the keyword that was updated.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to update the specified Keyword 
     *                               of the Chat with the specified id.
     */
     public int updateChatKeyword(int chatId,Keyword keyword) throws ChatsServiceException;
     
     /**
     * Deletes the Keyword with the specified id from the Chat with the 
     * specified id.
     * @param chatId The id of the chat to which the keyword to be deleted relates.
     * @param keywordId The id of the keyword to delete.
     * @return String the id of the deleted keyword.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to delete the Keyword
     *                               with the specified id from the Chat with the specified id.
     */
     public int deleteChatKeyword(int chatId,int keywordId) throws ChatsServiceException;
      
      
      
      
     
      //chat NoteNames
      
    /**
     * Gets all NoteNames associated with the chat with the specified id.
     * @param chatId Then id of the chat to get noteNames for.
     * @param includeDeleted indicates if deleted entries should be included in the results.
     * @return List<NoteName> List of noteNames associated with the chat with the specified id.
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch NoteNames 
     *                               for the Chat with the specified id.
     */
     public TransformerData<NoteName> listChatNoteNames(int chatId,ListOptions listOptions) throws ChatsServiceException;
      
    /**
     * Gets the chat NoteName with the specified id associated with the Chat with 
     * the specified id.
     * @param chatId The id of the chat that the NoteName is associated with
     * @param noteNameId The id of the NoteName to fetch. 
     * @return NoteName The noteName associated with the specified id.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch NoteName with the provided id 
     *                               for the Chat with the specified id.
     */
     public NoteName getChatNoteName(int chatId,int noteNameId) throws ChatsServiceException;
     
      /**
     * Creates new NoteName and associates it with the chat with the specified id.
     * @param chatId The id of the chat to associate the noteName with.
     * @param noteName The new NoteName.
     * @return String the id assigned to the newly created NoteName.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to create NoteNames 
     *                               and associate it with the Chat with the specified id.
     */
     public int createChatNoteName(int chatId,NoteName NoteName) throws ChatsServiceException;
     
      /**
     * Updates the chat NoteName associated with the chat with the specified id;
     * the specified noteName MUST have an id corresponding to a pre-existing
     * noteName in  the system
     * @param chatId The id of the chat that the NoteName to be updated is associated with.
     * @param noteName The updated noteName.
     * @return String the id of the noteName that was updated.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to update the specified NoteName 
     *                               of the Chat with the specified id.
     */
     public int updateChatNoteName(int chatId,NoteName NoteName) throws ChatsServiceException;
     
     
    /**
     * Deletes the NoteName with the specified id from the Chat with the 
     * specified id.
     * @param chatId The id of the chat to which the noteName to be deleted relates.
     * @param noteNameId The id of the noteName to delete.
     * @return String the id of the deleted noteName.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to delete the NoteName
     *                               with the specified id from the Chat with the specified id.
     */
     public int deleteChatNoteName(int chatId,int noteNamesId) throws ChatsServiceException;
      
      
      
     
     
      //chat Promos
      
     
    /**
     * Gets all Promos associated with the chat with the specified id.
     * @param chatId Then id of the chat to get promos for.
     * @param includeDeleted indicates if deleted entries should be included in the results.
     * @return List<Promo> List of promos associated with the chat with the specified id.
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch Promos 
     *                               for the Chat with the specified id.
     */
     public TransformerData<Promo> listChatPromos(int chatId,ListOptions listOptions) throws ChatsServiceException;
      
     /**
     * Gets the chat Promo with the specified id associated with the Chat with 
     * the specified id.
     * @param chatId The id of the chat that the Promo is associated with
     * @param promoId The id of the Promo to fetch. 
     * @return Promo The promo associated with the specified id.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch Promo with the provided id 
     *                               for the Chat with the specified id.
     */
     public Promo getChatPromo(int chatId,int promoId) throws ChatsServiceException;
     
      /**
     * Creates new Promo and associates it with the chat with the specified id.
     * @param chatId The id of the chat to associate the promo with.
     * @param promo The new Promo.
     * @return String the id assigned to the newly created Promo.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to create Promos 
     *                               and associate it with the Chat with the specified id.
     */
     public int createChatPromo(int chatId,Promo promo) throws ChatsServiceException;
     
     /**
     * Updates the chat Promo associated with the chat with the specified id;
     * the specified promo MUST have an id corresponding to a pre-existing
     * promo in  the system
     * @param chatId The id of the chat that the Promo to be updated is associated with.
     * @param promo The updated promo.
     * @return String the id of the promo that was updated.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to update the specified Promo 
     *                               of the Chat with the specified id.
     */
     public int updateChatPromo(int chatId,Promo promo) throws ChatsServiceException;
     
    /**
     * Deletes the Promo with the specified id from the Chat with the 
     * specified id.
     * @param chatId The id of the chat to which the promo to be deleted relates.
     * @param promoId The id of the promo to delete.
     * @return String the id of the deleted promo.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to delete the Promo
     *                               with the specified id from the Chat with the specified id.
     */
     public int deleteChatPromo(int chatId,int promoId) throws ChatsServiceException;
      
      
      
      //chat Category
      
    /**
     * Gets all Categories associated with the chat with the specified id.
     * @param chatId Then id of the chat to get categories for.
     * @param includeDeleted indicates if deleted entries should be included in the results.
     * @return List<Category> List of categories associated with the chat with the specified id.
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch Categories 
     *                               for the Chat with the specified id.
     */
     public TransformerData<Category> listChatCategories(int chatId,ListOptions listOptions) throws ChatsServiceException;
      
    /**
     * Gets the chat Category with the specified id associated with the Chat with 
     * the specified id.
     * @param chatId The id of the chat that the Category is associated with
     * @param categoryId The id of the Category to fetch. 
     * @return Category The category associated with the specified id.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch Category with the provided id 
     *                               for the Chat with the specified id.
     */
     public Category getChatCategory(int chatId,int categoryId) throws ChatsServiceException;
     
     
    /**
     * Creates new Category and associates it with the chat with the specified id.
     * @param chatId The id of the chat to associate the category with.
     * @param category The new Category.
     * @return String the id assigned to the newly created Category.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to create Categorys 
     *                               and associate it with the Chat with the specified id.
     */

     public int createChatCategory(int chatId,Category category) throws ChatsServiceException;
     
     /**
     * Updates the chat Category associated with the chat with the specified id;
     * the specified category MUST have an id corresponding to a pre-existing
     * category in  the system
     * @param chatId The id of the chat that the Category to be updated is associated with.
     * @param category The updated category.
     * @return String the id of the category that was updated.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to update the specified Category 
     *                               of the Chat with the specified id.
     */
     public int updateChatCategory(int chatId,Category category) throws ChatsServiceException;
     
     
    /**
     * Deletes the Category with the specified id from the Chat with the 
     * specified id.
     * @param chatId The id of the chat to which the category to be deleted relates.
     * @param categoryId The id of the category to delete.
     * @return String the id of the deleted category.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to delete the Category
     *                               with the specified id from the Chat with the specified id.
     */
     public int deleteChatCategory(int chatId,int CategoryId) throws ChatsServiceException;
      
      
      
     
     
     
      //chat Affiliate
      
    /**
     * Gets all Affiliates associated with the chat with the specified id.
     * @param chatId Then id of the chat to get affiliates for.
     * @param includeDeleted indicates if deleted entries should be included in the results.
     * @return List<Affiliate> List of affiliates associated with the chat with the specified id.
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch Affiliates 
     *                               for the Chat with the specified id.
     */
     public TransformerData<Affiliate> listChatAffiliates(int chatId,ListOptions listOptions) throws ChatsServiceException;
      
    /**
     * Gets the chat Affiliate with the specified id associated with the Chat with 
     * the specified id.
     * @param chatId The id of the chat that the Affiliate is associated with
     * @param affiliateId The id of the Affiliate to fetch. 
     * @return Affiliate The affiliate associated with the specified id.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch Affiliate with the provided id 
     *                               for the Chat with the specified id.
     */
     public Affiliate getChatAffiliate(int chatId,int affilaiteId) throws ChatsServiceException;
     
     
     /**
     * Creates new Affiliate and associates it with the chat with the specified id.
     * @param chatId The id of the chat to associate the affiliate with.
     * @param affiliate The new Affiliate.
     * @return String the id assigned to the newly created Affiliate.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to create Affiliates 
     *                               and associate it with the Chat with the specified id.
     */
     public int createChatAffiliate(int chatId,Affiliate affilaite) throws ChatsServiceException;
     
     /**
     * Updates the chat Affiliate associated with the chat with the specified id;
     * the specified affiliate MUST have an id corresponding to a pre-existing
     * affiliate in  the system
     * @param chatId The id of the chat that the Affiliate to be updated is associated with.
     * @param affiliate The updated affiliate.
     * @return String the id of the affiliate that was updated.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to update the specified Affiliate 
     *                               of the Chat with the specified id.
     */
     public int updateChatAffiliate(int chatId,Affiliate affiliate) throws ChatsServiceException;
     
    
     /**
     * Deletes the Affiliate with the specified id from the Chat with the 
     * specified id.
     * @param chatId The id of the chat to which the affiliate to be deleted relates.
     * @param affiliateId The id of the affiliate to delete.
     * @return String the id of the deleted affiliate.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to delete the Affiliate
     *                               with the specified id from the Chat with the specified id.
     */
     public int deleteChatAffiliate(int chatId,int affiliateId) throws ChatsServiceException;
      
      
      
      //chat Scheme
      
     
    /**
     * Gets all Schemes associated with the chat with the specified id.
     * @param chatId Then id of the chat to get schemes for.
     * @param includeDeleted indicates if deleted entries should be included in the results.
     * @return List<Scheme> List of schemes associated with the chat with the specified id.
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch Schemes 
     *                               for the Chat with the specified id.
     */
     public TransformerData<Scheme> listChatSchemes(int chatId,ListOptions listOptions) throws ChatsServiceException;
      
     
    /**
     * Gets the chat Scheme with the specified id associated with the Chat with 
     * the specified id.
     * @param chatId The id of the chat that the Scheme is associated with
     * @param schemeId The id of the Scheme to fetch. 
     * @return Scheme The scheme associated with the specified id.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to fetch Scheme with the provided id 
     *                               for the Chat with the specified id.
     */
     public Scheme getChatScheme(int chatId,int schemeId) throws ChatsServiceException;
     
     
      /**
     * Creates new Scheme and associates it with the chat with the specified id.
     * @param chatId The id of the chat to associate the scheme with.
     * @param scheme The new Scheme.
     * @return String the id assigned to the newly created Scheme.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to create Schemes 
     *                               and associate it with the Chat with the specified id.
     */
     public int createChatScheme(int chatId,Scheme scheme) throws ChatsServiceException;
     
    /**
     * Updates the chat Scheme associated with the chat with the specified id;
     * the specified scheme MUST have an id corresponding to a pre-existing
     * scheme in  the system
     * @param chatId The id of the chat that the Scheme to be updated is associated with.
     * @param scheme The updated scheme.
     * @return String the id of the scheme that was updated.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to update the specified Scheme 
     *                               of the Chat with the specified id.
     */
     public int updateChatScheme(int chatId,Scheme scheme) throws ChatsServiceException;
     
    /**
     * Deletes the Scheme with the specified id from the Chat with the 
     * specified id.
     * @param chatId The id of the chat to which the scheme to be deleted relates.
     * @param schemeId The id of the scheme to delete.
     * @return String the id of the deleted scheme.
     * 
     * @throws ChatsServiceException Where an error occurs whilst attempting to delete the Scheme
     *                               with the specified id from the Chat with the specified id.
     */
     public int deleteChatScheme(int chatId,int schemeId) throws ChatsServiceException;
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
     
     
     
     
     
     
     
     
     
     
     
     
     
     
    
}
