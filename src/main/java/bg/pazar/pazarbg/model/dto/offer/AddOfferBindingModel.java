package bg.pazar.pazarbg.model.dto.offer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddOfferBindingModel {
    @NotEmpty(message = "Title cannot be empty")
    @Size(min = 3, max = 20, message = "Title must be between 3 and 20 characters long")
    private String title;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @DecimalMin("0.01")
    private double price;
}
