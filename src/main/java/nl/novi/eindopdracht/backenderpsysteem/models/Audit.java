package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;
import org.apache.catalina.User;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Audit {
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(updatable = false)
    private LocalDateTime creationDate;

    private LocalDateTime modificationDate;

    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDateTime.now();
        this.modificationDate = LocalDateTime.now();
        this.createdBy = getUser();
        this.modifiedBy = getUser();
    }

    @PreUpdate
    public void preUpdate() {
        this.modificationDate = LocalDateTime.now();
        this.modifiedBy = getUser();
    }

    private String getUser() {
        return "UserPlaceholder";
    }
}
