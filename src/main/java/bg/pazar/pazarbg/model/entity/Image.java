package bg.pazar.pazarbg.model.entity;

import bg.pazar.pazarbg.model.enums.ImageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;

    private ImageType type;

    @ManyToOne
    @JoinColumn(name = "uploaded_by_id")
    private UserEntity uploadedBy;

    @ManyToOne
    @JoinColumn(name = "offfer_id")
    private Offer offer;
}
