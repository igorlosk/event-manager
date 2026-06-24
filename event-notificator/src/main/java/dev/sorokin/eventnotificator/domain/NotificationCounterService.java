package dev.sorokin.eventnotificator.domain;

import dev.sorokin.eventnotificator.db.NotificationEntityRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationCounterService {

    private final StringRedisTemplate redisTemplate;
    private final NotificationEntityRepository nrRepository;

    public NotificationCounterService(StringRedisTemplate redisTemplate, NotificationEntityRepository nrRepository) {
        this.redisTemplate = redisTemplate;
        this.nrRepository = nrRepository;
    }


    public void incrementUnread(Long userId, long delta){
        redisTemplate.opsForValue().increment(unreadKey(userId), delta);
    }

    public void syncUnreadFromDatabase(Long userId){
        long unreadCount = nrRepository.countByUserIdAndReadFalse(userId);
        redisTemplate.opsForValue().set(unreadKey(userId), Long.toString(unreadCount));
    }

    private String unreadKey(Long userId) {
        return "notif:unread:" + userId;
    }
}
