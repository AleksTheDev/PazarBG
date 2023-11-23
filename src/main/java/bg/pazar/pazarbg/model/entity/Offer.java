package bg.pazar.pazarbg.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    @NotNull
    private Category category;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private List<String> imagePaths;

    public Offer() {
        this.imagePaths = new ArrayList<>();
    }
}
