package bg.pazar.pazarbg.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class HomeViewModel {
    private CategoryViewModel currentCategory;
    private List<CategoryViewModel> categories;
    private List<OfferViewModel> offers;
}
