package com.okatu.rgan.feed.controller;

import com.okatu.rgan.feed.SseNotificationService;
import com.okatu.rgan.user.model.RganUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@RestController
@RequestMapping(("/notification"))
public class SseConnectionController {
    @Autowired
    private SseNotificationService sseNotificationService;

    @GetMapping("/connect")
    public SseEmitter createConnection(@AuthenticationPrincipal RganUser user, HttpServletResponse response){
        SseEmitter sseEmitter = sseNotificationService.createConnection(user);
        response.addHeader("X-Accel-Buffering", "no");
        response.addHeader("Test", "aaa");
        return sseEmitter;
    }

    // add a hook for client side?
    @DeleteMapping("/close")
    public void cleanConnection(@AuthenticationPrincipal RganUser user){
        sseNotificationService.cleanConnection(user);
    }
}
