package dev.sorokin.eventnotificator;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payload_id", nullable = false)
    private NotificationEventPayloadEntity payload;

    public NotificationEntity() {
    }

    public NotificationEntity(Long id, Long userId, boolean isRead, LocalDateTime createdAt, LocalDateTime readAt, NotificationEventPayloadEntity payload) {
        this.id = id;
        this.userId = userId;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.readAt = readAt;
        this.payload = payload;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public NotificationEventPayloadEntity getPayload() {
        return payload;
    }

    public void setPayload(NotificationEventPayloadEntity payload) {
        this.payload = payload;
    }
}
