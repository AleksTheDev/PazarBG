package bg.pazar.pazarbg.model.entity;

import bg.pazar.pazarbg.model.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 20)
    private String username;

    @NotNull
    private String password;

    @NotNull
    @Column(unique = true)
    @Email
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean markedForDeletion;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "createdBy")
    private Set<Offer> offers;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "boughtBy")
    private Set<Offer> boughtOffers;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "uploadedBy")
    private Set<Image> images;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "from")
    private Set<Message> sentMessages;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "to")
    private Set<Message> receivedMessages;

    public UserEntity() {
        this.offers = new HashSet<>();
        this.boughtOffers = new HashSet<>();
        this.images = new HashSet<>();
        this.sentMessages = new HashSet<>();
    }
}
