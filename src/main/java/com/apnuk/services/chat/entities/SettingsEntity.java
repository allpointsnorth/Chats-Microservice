/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apnuk.services.chat.entities;


import com.apnuk.services.chat.dtos.Settings;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Models a single set of Chat general settings, each chart provider will
 define a set of General SettingsEntity. ALl time variables are expected to be
 * specified in milliseconds unless otherwise explicitly stated.
 * @author gilesthompson
 */
@Entity
@Table(name="settings")
public class SettingsEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;	
    private String code;	
    private String name;	
    private int timeToLogoff;	
    private int minInfoTimeAfterReceive;	
    private int minInfoTimeAfterSend;	
    private int minInfoTimeAfterInfo;	
    private int timeBetweenNameHints;
    private int timeJoinBeforeNameHint;	
    private int timeBetweenPicHints;	
    private Date lastMonitorSwap;	
    private int timeBetweenMonitorSwaps;	
    private String locator;	
    private int minOutboundTimeAfterJoin;	
    private int maxOutboundTimeAfterJoin;	
    private int quietUserOutboundTime;	
    private int webAndiphoneAutomaticLogoffTime;	
    private int signupTimeout;	
    private int minimumMonitorMessageLength;	
    private boolean autoJoinChat;	
    private boolean autoJoinDating;	
    private boolean datingSendsEnabled;	
    private boolean joinWarningEnabled;	
    private boolean errorOnUnrecognisedKeyword;	
    private boolean sendWarningFromShortcode;	
    private boolean sendDobRequestFromShortcode;	
    private boolean billDuringJoin;	
    private boolean confirmCharges;	
    private int maximumAlarmTime;	
    private int maximumProfilesPerChatSession;	
    private String adultAdsChatService;	
    private int adultAdsTimeInMinutes;	
    private int automaticAdFirstTime;	
    private int automaticAdRegularTime;	
    private int automaticAdRegularCount;	
    private boolean billBlockedUsers;	
    private int billTimeLimitInDays;	
    private int chatQueuePreferredTime;	
    private int messageQueuePreferredTime;	
    private int helpQueuePreferredTime;	
    private int infoQueuePreferredTime;	
    private String currency;	
    private String numberFormat;	
    private String gazetteer;	
    private String defaultBrandsName;	
    private String timezone;
    
        
    
    

    public SettingsEntity() {
    }
    
    

    public SettingsEntity(int id, String code, String name, int timeToLogoff, int minInfoTimeAfterReceive, int minInfoTimeAfterSend, int minInfoTimeAfterInfo, int timeBetweenNameHints, int timeJoinBeforeNameHint, int timeBetweenPicHints, Date lastMonitorSwap, int timeBetweenMonitorSwaps, String locator, int minOutboundTimeAfterJoin, int maxOutboundTimeAfterJoin, int quietUserOutboundTime, int webAndiphoneAutomaticLogoffTime, int signupTimeout, int minimumMonitorMessageLength, boolean autoJoinChat, boolean autoJoinDating, boolean datingSendsEnabled, boolean joinWarningEnabled, boolean errorOnUnrecognisedKeyword, boolean sendWarningFromShortcode, boolean sendDobRequestFromShortcode, boolean billDuringJoin, boolean confirmCharges, int maximumAlarmTime, int maximumProfilesPerChatSession, String adultAdsChatService, int adultAdsTimeInMinutes, int automaticAdFirstTime, int automaticAdRegularTime, int automaticAdRegularCount, boolean billBlockedUsers, int billTimeLimitInDays, int chatQueuePreferredTime, int messageQueuePreferredTime, int helpQueuePreferredTime, int infoQueuePreferredTime, String currency, String numberFormat, String gazetteer, String defaultBrandsName, String timezone) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.timeToLogoff = timeToLogoff;
        this.minInfoTimeAfterReceive = minInfoTimeAfterReceive;
        this.minInfoTimeAfterSend = minInfoTimeAfterSend;
        this.minInfoTimeAfterInfo = minInfoTimeAfterInfo;
        this.timeBetweenNameHints = timeBetweenNameHints;
        this.timeJoinBeforeNameHint = timeJoinBeforeNameHint;
        this.timeBetweenPicHints = timeBetweenPicHints;
        this.lastMonitorSwap = lastMonitorSwap;
        this.timeBetweenMonitorSwaps = timeBetweenMonitorSwaps;
        this.locator = locator;
        this.minOutboundTimeAfterJoin = minOutboundTimeAfterJoin;
        this.maxOutboundTimeAfterJoin = maxOutboundTimeAfterJoin;
        this.quietUserOutboundTime = quietUserOutboundTime;
        this.webAndiphoneAutomaticLogoffTime = webAndiphoneAutomaticLogoffTime;
        this.signupTimeout = signupTimeout;
        this.minimumMonitorMessageLength = minimumMonitorMessageLength;
        this.autoJoinChat = autoJoinChat;
        this.autoJoinDating = autoJoinDating;
        this.datingSendsEnabled = datingSendsEnabled;
        this.joinWarningEnabled = joinWarningEnabled;
        this.errorOnUnrecognisedKeyword = errorOnUnrecognisedKeyword;
        this.sendWarningFromShortcode = sendWarningFromShortcode;
        this.sendDobRequestFromShortcode = sendDobRequestFromShortcode;
        this.billDuringJoin = billDuringJoin;
        this.confirmCharges = confirmCharges;
        this.maximumAlarmTime = maximumAlarmTime;
        this.maximumProfilesPerChatSession = maximumProfilesPerChatSession;
        this.adultAdsChatService = adultAdsChatService;
        this.adultAdsTimeInMinutes = adultAdsTimeInMinutes;
        this.automaticAdFirstTime = automaticAdFirstTime;
        this.automaticAdRegularTime = automaticAdRegularTime;
        this.automaticAdRegularCount = automaticAdRegularCount;
        this.billBlockedUsers = billBlockedUsers;
        this.billTimeLimitInDays = billTimeLimitInDays;
        this.chatQueuePreferredTime = chatQueuePreferredTime;
        this.messageQueuePreferredTime = messageQueuePreferredTime;
        this.helpQueuePreferredTime = helpQueuePreferredTime;
        this.infoQueuePreferredTime = infoQueuePreferredTime;
        this.currency = currency;
        this.numberFormat = numberFormat;
        this.gazetteer = gazetteer;
        this.defaultBrandsName = defaultBrandsName;
        this.timezone = timezone;
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

  
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

 

    public int getTimeToLogoff() {
        return timeToLogoff;
    }

    public void setTimeToLogoff(int timeToLogoff) {
        this.timeToLogoff = timeToLogoff;
    }

    public int getMinInfoTimeAfterReceive() {
        return minInfoTimeAfterReceive;
    }

    public void setMinInfoTimeAfterReceive(int minInfoTimeAfterReceive) {
        this.minInfoTimeAfterReceive = minInfoTimeAfterReceive;
    }

    public int getMinInfoTimeAfterSend() {
        return minInfoTimeAfterSend;
    }

    public void setMinInfoTimeAfterSend(int minInfoTimeAfterSend) {
        this.minInfoTimeAfterSend = minInfoTimeAfterSend;
    }

    public int getMinInfoTimeAfterInfo() {
        return minInfoTimeAfterInfo;
    }

    public void setMinInfoTimeAfterInfo(int minInfoTimeAfterInfo) {
        this.minInfoTimeAfterInfo = minInfoTimeAfterInfo;
    }

    public int getTimeBetweenNameHints() {
        return timeBetweenNameHints;
    }

    public void setTimeBetweenNameHints(int timeBetweenNameHints) {
        this.timeBetweenNameHints = timeBetweenNameHints;
    }

    public int getTimeJoinBeforeNameHint() {
        return timeJoinBeforeNameHint;
    }

    public void setTimeJoinBeforeNameHint(int timeJoinBeforeNameHint) {
        this.timeJoinBeforeNameHint = timeJoinBeforeNameHint;
    }

    public int getTimeBetweenPicHints() {
        return timeBetweenPicHints;
    }

    public void setTimeBetweenPicHints(int timeBetweenPicHints) {
        this.timeBetweenPicHints = timeBetweenPicHints;
    }

    public Date getLastMonitorSwap() {
        return lastMonitorSwap;
    }

    public void setLastMonitorSwap(Date lastMonitorSwap) {
        this.lastMonitorSwap = lastMonitorSwap;
    }

    public int getTimeBetweenMonitorSwaps() {
        return timeBetweenMonitorSwaps;
    }

    public void setTimeBetweenMonitorSwaps(int timeBetweenMonitorSwaps) {
        this.timeBetweenMonitorSwaps = timeBetweenMonitorSwaps;
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public int getMinOutboundTimeAfterJoin() {
        return minOutboundTimeAfterJoin;
    }

    public void setMinOutboundTimeAfterJoin(int minOutboundTimeAfterJoin) {
        this.minOutboundTimeAfterJoin = minOutboundTimeAfterJoin;
    }

    public int getMaxOutboundTimeAfterJoin() {
        return maxOutboundTimeAfterJoin;
    }

    public void setMaxOutboundTimeAfterJoin(int maxOutboundTimeAfterJoin) {
        this.maxOutboundTimeAfterJoin = maxOutboundTimeAfterJoin;
    }

    public int getQuietUserOutboundTime() {
        return quietUserOutboundTime;
    }

    public void setQuietUserOutboundTime(int quietUserOutboundTime) {
        this.quietUserOutboundTime = quietUserOutboundTime;
    }

    public int getWebAndiphoneAutomaticLogoffTime() {
        return webAndiphoneAutomaticLogoffTime;
    }

    public void setWebAndiphoneAutomaticLogoffTime(int webAndiphoneAutomaticLogoffTime) {
        this.webAndiphoneAutomaticLogoffTime = webAndiphoneAutomaticLogoffTime;
    }

    public int getSignupTimeout() {
        return signupTimeout;
    }

    public void setSignupTimeout(int signupTimeout) {
        this.signupTimeout = signupTimeout;
    }

    public int getMinimumMonitorMessageLength() {
        return minimumMonitorMessageLength;
    }

    public void setMinimumMonitorMessageLength(int minimumMonitorMessageLength) {
        this.minimumMonitorMessageLength = minimumMonitorMessageLength;
    }

    public boolean isAutoJoinChat() {
        return autoJoinChat;
    }

    public void setAutoJoinChat(boolean autoJoinChat) {
        this.autoJoinChat = autoJoinChat;
    }

    public boolean isAutoJoinDating() {
        return autoJoinDating;
    }

    public void setAutoJoinDating(boolean autoJoinDating) {
        this.autoJoinDating = autoJoinDating;
    }

    public boolean isDatingSendsEnabled() {
        return datingSendsEnabled;
    }

    public void setDatingSendsEnabled(boolean datingSendsEnabled) {
        this.datingSendsEnabled = datingSendsEnabled;
    }

    public boolean isJoinWarningEnabled() {
        return joinWarningEnabled;
    }

    public void setJoinWarningEnabled(boolean joinWarningEnabled) {
        this.joinWarningEnabled = joinWarningEnabled;
    }

    public boolean isErrorOnUnrecognisedKeyword() {
        return errorOnUnrecognisedKeyword;
    }

    public void setErrorOnUnrecognisedKeyword(boolean errorOnUnrecognisedKeyword) {
        this.errorOnUnrecognisedKeyword = errorOnUnrecognisedKeyword;
    }

    public boolean isSendWarningFromShortcode() {
        return sendWarningFromShortcode;
    }

    public void setSendWarningFromShortcode(boolean sendWarningFromShortcode) {
        this.sendWarningFromShortcode = sendWarningFromShortcode;
    }

    public boolean isSendDobRequestFromShortcode() {
        return sendDobRequestFromShortcode;
    }

    public void setSendDobRequestFromShortcode(boolean sendDobRequestFromShortcode) {
        this.sendDobRequestFromShortcode = sendDobRequestFromShortcode;
    }

    public boolean isBillDuringJoin() {
        return billDuringJoin;
    }

    public void setBillDuringJoin(boolean billDuringJoin) {
        this.billDuringJoin = billDuringJoin;
    }

    public boolean isConfirmCharges() {
        return confirmCharges;
    }

    public void setConfirmCharges(boolean confirmCharges) {
        this.confirmCharges = confirmCharges;
    }

    public int getMaximumAlarmTime() {
        return maximumAlarmTime;
    }

    public void setMaximumAlarmTime(int maximumAlarmTime) {
        this.maximumAlarmTime = maximumAlarmTime;
    }

    public int getMaximumProfilesPerChatSession() {
        return maximumProfilesPerChatSession;
    }

    public void setMaximumProfilesPerChatSession(int maximumProfilesPerChatSession) {
        this.maximumProfilesPerChatSession = maximumProfilesPerChatSession;
    }

    public String getAdultAdsChatService() {
        return adultAdsChatService;
    }

    public void setAdultAdsChatService(String adultAdsChatService) {
        this.adultAdsChatService = adultAdsChatService;
    }

    public int getAdultAdsTimeInMinutes() {
        return adultAdsTimeInMinutes;
    }

    public void setAdultAdsTimeInMinutes(int adultAdsTimeInMinutes) {
        this.adultAdsTimeInMinutes = adultAdsTimeInMinutes;
    }

    public int getAutomaticAdFirstTime() {
        return automaticAdFirstTime;
    }

    public void setAutomaticAdFirstTime(int automaticAdFirstTime) {
        this.automaticAdFirstTime = automaticAdFirstTime;
    }

    public int getAutomaticAdRegularTime() {
        return automaticAdRegularTime;
    }

    public void setAutomaticAdRegularTime(int automaticAdRegularTime) {
        this.automaticAdRegularTime = automaticAdRegularTime;
    }

    public int getAutomaticAdRegularCount() {
        return automaticAdRegularCount;
    }

    public void setAutomaticAdRegularCount(int automaticAdRegularCount) {
        this.automaticAdRegularCount = automaticAdRegularCount;
    }

    public boolean getBillBlockedUsers() {
        return billBlockedUsers;
    }

    public void setBillBlockedUsers(boolean billBlockedUsers) {
        this.billBlockedUsers = billBlockedUsers;
    }

    public int getBillTimeLimitInDays() {
        return billTimeLimitInDays;
    }

    public void setBillTimeLimitInDays(int billTimeLimitInDays) {
        this.billTimeLimitInDays = billTimeLimitInDays;
    }

    public int getChatQueuePreferredTime() {
        return chatQueuePreferredTime;
    }

    public void setChatQueuePreferredTime(int chatQueuePreferredTime) {
        this.chatQueuePreferredTime = chatQueuePreferredTime;
    }

    public int getMessageQueuePreferredTime() {
        return messageQueuePreferredTime;
    }

    public void setMessageQueuePreferredTime(int messageQueuePreferredTime) {
        this.messageQueuePreferredTime = messageQueuePreferredTime;
    }

    public int getHelpQueuePreferredTime() {
        return helpQueuePreferredTime;
    }

    public void setHelpQueuePreferredTime(int helpQueuePreferredTime) {
        this.helpQueuePreferredTime = helpQueuePreferredTime;
    }

    public int getInfoQueuePreferredTime() {
        return infoQueuePreferredTime;
    }

    public void setInfoQueuePreferredTime(int infoQueuePreferredTime) {
        this.infoQueuePreferredTime = infoQueuePreferredTime;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(String numberFormat) {
        this.numberFormat = numberFormat;
    }

    public String getGazetteer() {
        return gazetteer;
    }

    public void setGazetteer(String gazetteer) {
        this.gazetteer = gazetteer;
    }

    public String getDefaultBrandsName() {
        return defaultBrandsName;
    }

    public void setDefaultBrandsName(String defaultBrandsName) {
        this.defaultBrandsName = defaultBrandsName;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    

    public boolean isBillBlockedUsers() {
        return billBlockedUsers;
    }
    
    
    public Settings toDTO(){
        
        return new Settings(this.getId(),
                            this.getCode(),
                            this.getName(),
                            this.getTimeToLogoff(), 
                            this.getMinInfoTimeAfterReceive(),
                            this.getMinInfoTimeAfterSend(),
                            this.getMinInfoTimeAfterInfo(),
                            this.getTimeBetweenNameHints(),
                            this.getTimeJoinBeforeNameHint(),
                            this.getTimeBetweenPicHints(),
                            this.getLastMonitorSwap(),
                            this.getTimeBetweenMonitorSwaps(),
                            this.getLocator(),
                            this.getMinOutboundTimeAfterJoin(),
                            this.getMaxOutboundTimeAfterJoin(),
                            this.getQuietUserOutboundTime(),
                            this.getWebAndiphoneAutomaticLogoffTime(),
                            this.getSignupTimeout(),
                            this.getMinimumMonitorMessageLength(),
                            this.isAutoJoinChat(),
                            this.isAutoJoinDating(),
                            this.isDatingSendsEnabled(),
                            this.isJoinWarningEnabled(),
                            this.isErrorOnUnrecognisedKeyword(), 
                            this.isSendWarningFromShortcode(),
                            this.isSendDobRequestFromShortcode(),
                            this.isBillDuringJoin(),
                            this.isConfirmCharges(),
                            this.getMaximumAlarmTime(),
                            this.getMaximumProfilesPerChatSession(),
                            this.getAdultAdsChatService(),
                            this.getAdultAdsTimeInMinutes(),
                            this.getAutomaticAdFirstTime(),
                            this.getAutomaticAdRegularTime(),
                            this.getAutomaticAdRegularCount(),
                            this.isBillBlockedUsers(),
                            this.getBillTimeLimitInDays(),
                            this.getChatQueuePreferredTime(),
                            this.getMessageQueuePreferredTime(),
                            this.getHelpQueuePreferredTime(),
                            this.getInfoQueuePreferredTime(),
                            this.getCurrency(),
                            this.getNumberFormat(),
                            this.getGazetteer(),
                            this.getDefaultBrandsName(),
                            this.getTimezone());
    }


}
