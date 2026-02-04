package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;

@Entity
public class ContactInformation extends Audit{

    @Id
    private String Id;

    private String phoneNumber;
    private String businessEmail;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "username")
    private User user;

    public String getId() {
        return this.Id;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBusinessEmail() {
        return this.businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
