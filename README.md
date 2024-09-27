SupraRBI-HTTPS is a Remote Browser Isolation (RBI) solution with web/https front end, based on [guacamole-server](https://github.com/apache/guacamole-server) and [SupraRBI-VNC](https://github.com/supraaxes/suprarbi-vnc) with docker.

## Usage
SupraRBI-HTTPS provides a web portal for direct user access and an API for integration with network/security solutions (e.g. gateway, proxy).
```
sudo docker compose up -d
```
### Web Portal
SupraRBI-HTTPS Web Portal lists all resources provided by the administration, organized by groups. When a user accesses SupraRBI-HTTPS Web Portal with a browser, each time she clicks the icon of a resource, a corresponding RBI session will be presented in a new tab.

<div style="display: flex;">
    <img src="img/resources.png" alt="Resources" style="height: 200px; margin: 5px;" />
    <img src="img/session.png" alt="Session" style="height: 200px; margin: 5px;" />
</div>

### API
Each time the SupraRBI-HTTPS server gets a valid RBI request, it will respond with Status Code 302 and a URL for the corresponding RBI session. End users can access the RBI seesion from a browser with the URL. 

Please check [API specification](/api.yml) for detailed info.

## Installation
SupraRBI-HTTPS is built with Service-Oriented Architecture (SOA) on docker, with mounts to the host for persistant storage (cofigurations, logs, icons.) and cross-service sharing (temp files for user uploads).

### Mounts 
1. **Tmp, /opt/supra/rbi/data/temp/**: temp files for user uploads<br>
2. **Logs, /opt/supra/rbi/logs/**: log files <br>
3. **Certificate, /opt/supra/rbi/cert**: certificate(tls.cert) and key(tls.key) for HTTPS. If not provided, a self-signed certificate and key will be automatically generated.<br>
4. **Configs, /opt/supra/rbi/conf/**: configuration files<br>
    - **Token**, token.json: list of strings as valid tokens. Each API request for a RBI seesion MUST have a valid token.<br>
    - **Policy**, policy/: file name as policy name (*.json), defining the policy to enforce for corresponding RBI sessions with key-value pairs as the value of key "rule". 
        - *keyboard*: whether the user is allowed to input with keyboard, 1 for allowed, 0 for not allowed. Default value is 0 if not set.
        - *mouse*: whether the user is allowed to input with mouse, 1 for allowed, 0 for not allowed. Default value is 0 if not set.
        - *download*: whether the user is allowed to download files, 1 for allowed, 0 for not allowed. Default value is 0 if not set.
        - *upload*: whether the user is allowed to upload files, 1 for allowed, 0 for not allowed. Default value is 0 if not set.
        - *copy*: whether the user is allowed to copy into the RBI seesion, 1 for allowed, 0 for not allowed. Default value is 0 if not set.
        - *paste*: whether the user is allowed to paste out from the RBI session, 1 for allowed, 0 for not allowed. Default value is 0 if not set.
        - *idleTime*: number of minutes to terminate the RBI session if the user stays idle and does not have any activity, default value is 15 if not set.

        *Note*: Each resource in resource.json or each API request for a RBI seesion SHOULD specify the name of a policy to be enforced, If not specified, the default policy defined by default.json will be applied. 
    - **Resource**, resource.json: list of resources in Web Portal.<br>
        - *name*: resource name presented in Web Portal, no default value.
        - *url*: target url with the resource, no default value.
        - *icon*: name of the icon file, no default value.
        - *group*: name of the group that the resource is organized in Web Portal, default value is "Default" if not set.
        - *policy*: name of the policy applied to RBI sessions for this resource, default value is "default" if not set.
        - *autofill*: key-value pairs for autofill with [projector-supra-web](https://github.com/SupraAXES/projector-supra-web), no default value and blank if not set.
    - **license**, license.json: license file for [projector-supra-web](https://github.com/SupraAXES/projector-supra-web) when used.
    - **Autofill**, autofill/: file name as site name (*.json) for autofill with [projector-supra-web](https://github.com/SupraAXES/projector-supra-web), please check [Autofill with SupraRBI-VNC](https://github.com/SupraAXES/projector-supra-web?tab=readme-ov-file#json-file-with-autofill-settings) for detailed info. <br>
    - **Guacd**, guacd_settings.conf: optional file with default settings for guacd, please check [VNC Settings for Guacamole](https://guacamole.apache.org/doc/gug/configuring-guacamole.html#vnc) for detailed info.
5. **Icons, /opt/supra/rbi/icons/**: icon files for resources in Web Portal, specified in resource.json for each resource.<br>

### Modules/Images 
- **rbi-portal**: web/https front end with Nginx.
- **rbi-admin**: app server with Java.
- **resc-agent**: [guacamole-server](https://github.com/apache/guacamole-server) for VNC connections to [SupraRBI-VNC](https://github.com/supraaxes/suprarbi-vnc) server. <br>
*Note*: the official guacamole/guacd image has [an issue with auto resize](https://github.com/SupraAXES/SupraRBI-VNC/blob/main/resources/guacamole.md#auto-resize-to-vnc-sessions), and it is recommanded to use supraaxes/supra-guacd image customized by [SupraAXES Inc.](https://www.supraaxes.com) before the issue is officially fixed.
- **vnc-rbi**:  [SupraRBI-VNC](https://github.com/supraaxes/suprarbi-vnc) server.
- **projector-chrome**: [open-source projector image](https://github.com/SupraAXES/projector-chrome) with Chrome in kiosk mode. 
- **projector-supra-web**: [customized projector image](https://github.com/SupraAXES/projector-supra-web) from [SupraAXES Inc.](https://www.supraaxes.com)