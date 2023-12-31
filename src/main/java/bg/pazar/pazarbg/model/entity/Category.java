package bg.pazar.pazarbg.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Lob
    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
    private Set<Offer> offers;

    public Category() {
        this.offers = new HashSet<>();
    }
}
