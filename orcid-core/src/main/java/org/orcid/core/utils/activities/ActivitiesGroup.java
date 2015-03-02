/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2014 ORCID, Inc.
 * Licensed under an MIT-Style License (MIT)
 * http://orcid.org/open-source-license
 *
 * This copyright and license information (including a link to the full license)
 * shall be included in its entirety in all copies or substantial portion of
 * the software.
 *
 * =============================================================================
 */
package org.orcid.core.utils.activities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.orcid.jaxb.model.record.ActivityWithExternalIdentifiers;
import org.orcid.jaxb.model.record.ExternalIdentifier;
import org.orcid.jaxb.model.record.ExternalIdentifiersContainer;


public class ActivitiesGroup {
    private Set<ExternalIdentifier> externalIdentifiers;
    private SortedSet<ActivityWithExternalIdentifiers> activities;         
    
    public ActivitiesGroup(ActivityWithExternalIdentifiers activity) {        
        externalIdentifiers = new HashSet<ExternalIdentifier>();        
        activities = new TreeSet<ActivityWithExternalIdentifiers>();
        
        if(activity != null) {
            ExternalIdentifiersContainer container = activity.getExternalIdentifiers();
            if(container != null) {
                List<? extends ExternalIdentifier> extIds = (List<? extends ExternalIdentifier>)container.getExternalIdentifier();
                for(ExternalIdentifier extId : extIds) {
                    //Dont add ext ids that dont pass the validation
                    if(extId.passGroupingValidation())
                        externalIdentifiers.add(extId);
                }
            }
        }
        
        activities.add(activity);
    }
            
    public Set<ExternalIdentifier> getExternalIdentifiers() {
        if(externalIdentifiers == null)
            externalIdentifiers = new HashSet<ExternalIdentifier>();
        return externalIdentifiers;
    }

    public Set<ActivityWithExternalIdentifiers> getActivities() {
        if(activities == null)
            activities = new TreeSet<ActivityWithExternalIdentifiers>();
        return activities;
    }

    public void add(ActivityWithExternalIdentifiers activity) {                
                //Add new external identifiers
        ExternalIdentifiersContainer container = activity.getExternalIdentifiers();
        if(container != null) {
            List<? extends ExternalIdentifier> extIds = (List<? extends ExternalIdentifier>)container.getExternalIdentifier();
            for(ExternalIdentifier extId : extIds) {
                //Dont add ext ids that dont pass the grouping validation
                if(extId.passGroupingValidation())
                    if(!externalIdentifiers.contains(extId))
                        externalIdentifiers.add(extId);
            }
        }
        
        //Add activity
        activities.add(activity);
    }
    
    public boolean belongsToGroup(ActivityWithExternalIdentifiers activity) {
        //If there are no external ids
        if(externalIdentifiers == null || externalIdentifiers.isEmpty()) {
            //Check if the activity dont have ext ids
            if(activity.getExternalIdentifiers() == null || activity.getExternalIdentifiers().getExternalIdentifier() == null || activity.getExternalIdentifiers().getExternalIdentifier().isEmpty()) {
                //If the activity doesn't have any external identifier, check if the activity is in the group
                if(activities.contains(activity))
                    return true;
                else 
                    return false;                            
            } else {
                //If any of the activities pass the grouping validation, the activity must belong to other group
                for(ExternalIdentifier extId : activity.getExternalIdentifiers().getExternalIdentifier()) {
                    if(extId.passGroupingValidation())
                        return false;
                }
                
                //If none of the activities pass the groupings validation, so, lets check if the group actually contains the activity
                if(activities.contains(activity))
                    return true;
                else 
                    return false;                
            }
        }                        
        
        //Check existing external identifiers 
        ExternalIdentifiersContainer container = activity.getExternalIdentifiers();
        if(container != null) {
            List<? extends ExternalIdentifier> extIds = (List<? extends ExternalIdentifier>)container.getExternalIdentifier();
            for(ExternalIdentifier extId : extIds) {
                //First check external identifiers restrictions
                if(extId.passGroupingValidation()) {
                    //If any of the ext ids already exists on this group, return true
                    if(externalIdentifiers.contains(extId))
                        return true;
                }
            }
        }
        
        return false;
    }
    
    public void merge(ActivitiesGroup group) {
        Set<ActivityWithExternalIdentifiers> otherActivities = group.getActivities();
        Set<ExternalIdentifier> otherExtIds = group.getExternalIdentifiers();
        
        //The incoming groups should always contain at least one ext id, we should not merge activities without ext ids
        if(otherExtIds.isEmpty()) 
            throw new IllegalArgumentException("Unable to merge a group without external identifiers");
        
        //The incoming group should always contains at least one activity, we should not merge empty activities
        
        //Merge external identifiers
        for(ExternalIdentifier otherExtId: otherExtIds) {
            if(!externalIdentifiers.contains(otherExtId))
                externalIdentifiers.add(otherExtId);
        }
        
        //Merge activities
        for(ActivityWithExternalIdentifiers activity : otherActivities) {
            //We assume the activity is not already there, anyway it is a set
            activities.add(activity);
        }
    }
}