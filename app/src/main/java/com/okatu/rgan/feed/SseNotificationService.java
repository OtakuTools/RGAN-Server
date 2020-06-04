package com.okatu.rgan.feed;

import com.okatu.rgan.user.model.RganUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseNotificationService {

    // about the keep-alive time
    // https://medium.com/@bethecodewithyou/server-sent-events-concept-f5d34b3c2ecc
    // https://golb.hplar.ch/2017/03/Server-Sent-Events-with-Spring.html

    // <userId, sseConnection>
    private static ConcurrentHashMap<Long, SseEmitter> sseEmitterConcurrentHashMap = new ConcurrentHashMap<>();

    private static Logger logger = LoggerFactory.getLogger(SseNotificationService.class);

    public SseEmitter createConnection(@NonNull RganUser user){
        // 30min
        // I'm still confused with this shit
        // https://stackoverflow.com/questions/62191420/in-exactly-what-situation-a-spring-sseemitter-instance-will-time-out
        SseEmitter sseEmitter = new SseEmitter(1800_000L);
        sseEmitter.onCompletion(() -> sseEmitterConcurrentHashMap.remove(user.getId()));
        sseEmitter.onTimeout(() -> sseEmitterConcurrentHashMap.remove(user.getId()));
        sseEmitter.onError(throwable -> {
            logger.error("Sse connection error", throwable);
            sseEmitterConcurrentHashMap.remove(user.getId());
        });

        return sseEmitter;
    }

    public void sendMessage(@NonNull RganUser user, String eventName, Object message){
        SseEmitter sseEmitter = sseEmitterConcurrentHashMap.get(user.getId());
        if(sseEmitter != null){
            try {
                // from https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/servlet/mvc/method/annotation/ResponseBodyEmitter.html#send-java.lang.Object-
                // Note: if the send fails with an IOException,
                // you do not need to call completeWithError(Throwable) in order to clean up.
                // Instead the Servlet container creates a notification that results in a dispatch where Spring MVC invokes exception resolvers
                // and completes processing.
                sseEmitter.send(
                    SseEmitter.event().name(eventName)
                    .data(message, MediaType.APPLICATION_JSON)
                );
            }catch (Exception e){
                logger.error("Exception while send message to userId: {}, eventName: {}", user.getId(), eventName, e);
                // complete method seems no help here, since it will check sendFail status
                sseEmitterConcurrentHashMap.remove(user.getId());
                sseEmitter.complete();
            }
        }
    }

    public void cleanConnection(@NonNull RganUser user){
        SseEmitter sseEmitter = sseEmitterConcurrentHashMap.get(user.getId());
        if(sseEmitter != null){
            sseEmitter.complete();
        }
    }
}
