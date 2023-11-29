package bg.pazar.pazarbg.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    @DecimalMin("0.01")
    private double price;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull
    private Category category;

    @ManyToOne
    @JoinColumn(name = "created_by_id", nullable = false)
    @NotNull
    private UserEntity createdBy;

    @ManyToOne
    @JoinColumn(name = "bought_by_id")
    private UserEntity boughtBy;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "offer")
    private Set<Image> images;

    public Offer() {
        this.images = new HashSet<>();
    }
}
