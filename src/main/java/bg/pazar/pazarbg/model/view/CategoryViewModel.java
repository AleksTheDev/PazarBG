package bg.pazar.pazarbg.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryViewModel {
    private Long id;
    private String name;
    private String description;
}
