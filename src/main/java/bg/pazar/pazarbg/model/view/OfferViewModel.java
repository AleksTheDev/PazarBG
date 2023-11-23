package bg.pazar.pazarbg.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class OfferViewModel {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String createdBy;
    private String category;
    private int imagesCount;
}
