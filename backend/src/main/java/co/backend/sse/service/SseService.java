package co.backend.sse.service;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SseService {

    private static final Logger logger = LoggerFactory.getLogger(SseService.class);
    private static final long TIMEOUT = 3600000L; // 1시간
    private static final long HEARTBEAT_INTERVAL = 15000; // 15초

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ExecutorService heartbeatExecutor = Executors.newCachedThreadPool();

    public SseEmitter createEmitter(String userId) {
        if (emitters.containsKey(userId)) {
            logger.info("기존 사용자 연결 종료: {}", userId);
            emitters.get(userId).complete();
        }

        SseEmitter emitter = new SseEmitter(TIMEOUT);
        logger.info("새로운 SSE 연결 생성. 사용자 ID: {}", userId);

        emitter.onCompletion(() -> removeEmitter(userId, "onCompletion 콜백 실행"));
        emitter.onTimeout(() -> removeEmitter(userId, "onTimeout 콜백 실행"));
        emitter.onError((e) -> removeEmitter(userId, "onError 콜백 실행"));

        try {
            emitter.send(SseEmitter.event().id("0").name("connect").data(userId + "님이 연결되었습니다."));
            emitters.put(userId, emitter);
            startHeartbeat(userId, emitter);
        } catch (IOException e) {
            logger.error("초기 연결 메시지 전송 중 오류 발생: {}", e.getMessage());
            emitter.completeWithError(e);
        }
        return emitter;
    }

    public void broadcastEvent(String eventName, Object data) {
        if (emitters.isEmpty()) {
            return;
        }
        emitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException e) {
                removeEmitter(userId, "broadcast event failed");
            }
        });
        logger.info("'{}' 이벤트가 모든 사용자에게 브로드캐스트되었습니다.", eventName);
    }

    private void startHeartbeat(String userId, SseEmitter emitter) {
        heartbeatExecutor.execute(() -> {
            while (emitters.get(userId) == emitter) {
                try {
                    Thread.sleep(HEARTBEAT_INTERVAL);
                    emitter.send(SseEmitter.event().name("heartbeat").data("ping"));
                } catch (Exception e) {
                    removeEmitter(userId, "하트비트 실패");
                    break;
                }
            }
        });
    }

    private void removeEmitter(String userId, String reason) {
        if (emitters.containsKey(userId)) {
            logger.info("SseEmitter 제거. 이유: [{}], 사용자 ID : [{}]", reason, userId);
            emitters.remove(userId);
        }
    }

    @PreDestroy
    public void cleanup() {
        logger.info("애플리케이션 종료 - 모든 SSE 연결 및 리소스 정리");
        heartbeatExecutor.shutdown();
        emitters.values().forEach(SseEmitter::complete);
        emitters.clear();
    }
}