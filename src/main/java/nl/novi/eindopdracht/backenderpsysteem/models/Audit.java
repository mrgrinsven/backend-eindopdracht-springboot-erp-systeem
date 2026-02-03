package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            return auth.getName();
        }
        return "SYSTEM";
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public String getModifiedBy() {
        return this.modifiedBy;
    }

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public LocalDateTime getModificationDate() {
        return this.modificationDate;
    }
}
