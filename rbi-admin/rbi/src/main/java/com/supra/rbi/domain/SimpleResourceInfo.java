package com.supra.rbi.domain;

import com.supra.rbi.util.EmptyUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SimpleResourceInfo {
    
    private String id;
    
    private String name;

    private String icon;

    private String group;

    public SimpleResourceInfo(ResourceInfo resource) {
        this.id = resource.getName();
        this.name = resource.getName();
        this.icon = resource.getIcon();
        this.group = resource.getGroup();
        if (EmptyUtils.isNotEmpty(this.icon)) {
            this.icon = "/icon/" + this.icon;
        }
    }

}
