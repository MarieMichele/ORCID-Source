package org.orcid.core.manager.v3.read_only;

import java.util.List;

import org.orcid.pojo.grouping.WorkGroupingSuggestion;

public interface GroupingSuggestionManagerReadOnly {
    
    List<WorkGroupingSuggestion> getGroupingSuggestions(String orcid);

}
