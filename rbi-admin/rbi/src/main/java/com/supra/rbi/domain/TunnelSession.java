package com.supra.rbi.domain;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supra.rbi.util.EmptyUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class TunnelSession {
    
    private String id;

    private String host;

    private int port;

    private String protocol;

    private String username;

    private String password;

    private String priKey;

    private Map<String, String> query;

    public TunnelSession(SessionInfo session) {
        this.id = session.getUuid();
        this.protocol = session.getProtocol();

        ResourceInfo resource = session.getResource();

        this.host = System.getenv().get("VNC_RBI_HOSTNAME");
        if (EmptyUtils.isEmpty(this.host)) {
            this.host = "vnc-rbi";
        }
        String p = System.getenv().get("VNC_RBI_PORT");
        if (EmptyUtils.isNotEmpty(p)) {
            this.port = Integer.parseInt(p);
        } else {
            this.port = 5900;
        }
        this.username = resource.getUrl();

        JSONObject password = new JSONObject();
        JSONObject idJson = new JSONObject();
        idJson.put("name", session.getUuid());
        idJson.put("type", "norm");
        password.put("id", idJson);
        password.put("idle-user", 0);
        if (session.getRule() != null) {
            password.put("idle-session", session.getRule().getIdleTime());
        }
        JSONArray mountsJson = new JSONArray();
        mountsJson.add("/opt/supra/rbi/data/sessions/norm-" + session.getUuid() + "/:/home/supra/:rw");
        if (session.getSiteName() != null) {
            mountsJson.add("/opt/supra/rbi/conf/autofill/" + session.getSiteName() + ".json:/opt/supra/conf/autofill.json:ro");
        }
        String license = System.getenv().get("LICENSE_PATH");
        if (EmptyUtils.isNotEmpty(license)) {
            mountsJson.add(license + ":/opt/supra/conf/license.json:ro");
        }

        password.put("mounts", mountsJson);
        JSONObject instanceSettingsJson = new JSONObject();
        if (session.getAutofill() != null) {
            for (String key : session.getAutofill().keySet()) {
                instanceSettingsJson.put("autofill-" + key, session.getAutofill().get(key));
            }
        }
        password.put("instance-settings", instanceSettingsJson);

        this.password = password.toJSONString();

        log.info("guaca username = " + this.username);
        log.info("guaca password = " + this.password);

        Map<String, String> query = new HashMap<>();

        query.put("drive-name", "MyFiles");
        if (EmptyUtils.isNotEmpty(session.getPath())) {
            query.put("drive-path", session.getPath().replace(
                "/opt/supra/data/files", "/opt/supra/data/user_data"));
        }

        if (EmptyUtils.isNotEmpty(this.username)) {
            query.put("username", this.username);
        }
        if (EmptyUtils.isNotEmpty(this.password)) {
            query.put("password", this.password);
        }

        query.put("audio-servername", "norm-" + session.getUuid());

        ResourceRule rule = session.getRule();
        if (rule != null) {
            if (EmptyUtils.isNotEmpty(rule.getAudioOut())) {
                if ("rdp".equals(protocol)) {
                    query.put("disable-audio", "false");
                } else if ("ssh".equals(protocol)) {
                } else {
                    query.put("enable-audio", "true");
                }
            }
            if (EmptyUtils.isNotEmpty(rule.getAudioIn())) {
                query.put("enable-audio-input", "true");
            }
            if (EmptyUtils.isEmpty(rule.getCopy())) {
                query.put("disable-copy", "true");
            }
            if (EmptyUtils.isEmpty(rule.getPaste())) {
                query.put("disable-paste", "true");
            }
            if (EmptyUtils.isEmpty(rule.getUpload())) {
                query.put("disable-upload", "true");
            }
            if (EmptyUtils.isEmpty(rule.getDownload())) {
                query.put("disable-download", "true");
            }
        }

        this.query = query;
    }
}
