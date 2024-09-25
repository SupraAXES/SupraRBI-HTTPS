package com.supra.rbi.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.supra.rbi.domain.SessionInfo;
import com.supra.rbi.service.SessionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SessionTask {

    @Autowired
    private SessionService sessionService;
    
    @Scheduled(cron = "0 0/2 * ? * ?")
    public void checkRecycle() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -2);

        List<SessionInfo> list = new ArrayList<>(sessionService.getSessions());

        List<SessionInfo> expireList = new ArrayList<>();
        for (SessionInfo item : list) {
            Calendar update = Calendar.getInstance();
            update.setTime(item.getUpdateTime());
            if (calendar.after(update)) {
                expireList.add(item);
            }
        }

        if (expireList.size() > 0) sessionService.removeSessions(expireList);
    }

    @Scheduled(cron = "0 0/10 * ? * ?")
    public void checkAutofill() {
        sessionService.updateSiteMap();
    }

}
