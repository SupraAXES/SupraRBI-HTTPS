package com.supra.rbi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SimpleSessionInfo {
    
    private String id;

    private String protocol;

    private String resourceName;

    private Integer copy;

    private Integer paste;

    private Integer download;

    private Integer upload;    

    private Integer view;

    private Integer edit;

    private Integer keyboard = 1;

    private Integer mouse = 1;

    public SimpleSessionInfo(SessionInfo session) {
        this.id = session.getUuid();
        this.resourceName = session.getResourceName();
        this.protocol = session.getProtocol();

        ResourceRule rule = session.getRule();
        if (rule != null) {
            this.copy = rule.getCopy();
            this.paste = rule.getPaste();
            this.download = rule.getDownload();
            this.upload = rule.getUpload();
            this.view = rule.getView();
            this.edit = rule.getEdit();
            this.keyboard = rule.getKeyboard();
            this.mouse = rule.getMouse();
        }
    }
}
