package org.example.search;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
}
