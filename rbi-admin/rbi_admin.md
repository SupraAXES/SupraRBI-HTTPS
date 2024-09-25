## guacd settings for a VNC-RBI session
    
    hostname={server-address}
    port={5900}
    username={target-url}
    password={ 
        "id": {
            "type": "norm",
            "name": "{session-id}"
        },
        "idle-user": 0,
        "idle-connection": 30000,
        "mounts":[ 
            "/opt/supra/rbi/data/sessions/norm-{session-id}/:/home/supra/:rw", 
            "/opt/supra/rbi/conf/license.json:/opt/supra/conf/license.json:ro",
            "/opt/supra/rbi/conf/autofill/{site-name}.json:/opt/supra/conf/autofill.json:ro"
        ], 
        "instance-settings": { 
            "autofill-username": "{autofill-username}", 
            "autofill-password": "{autofill-password}" 
        }  
    }
    enable-audio=true
    audio-servername="rbi-norm-{session-id}"


**server-address**: set the VNC-RBI server address, from ENV and default value is "vnc-rbi"<br>
**target-url**: set the target url for a RBI session<br>
**session-id**: set the VNC-RBI session id<br>
**site-name**: set the JSON file name for autofill settings<br>
**autofill-username**: the username for autofill in the VNC-RBI session<br>
**autofill-password**: the password for autofill in the VNC-RBI session<br>