package dev.sorokin.eventmanager.registration;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
public class RegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at")
    private LocalDateTime created;

    public RegistrationEntity() {
    }

    public RegistrationEntity(Long id, Long eventId, Long userId, LocalDateTime created) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
