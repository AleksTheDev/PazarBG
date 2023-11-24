package bg.pazar.pazarbg.model.dto.offer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddOfferBindingModel {
    @NotEmpty(message = "Title cannot be empty")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters long")
    private String title;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @DecimalMin(value = "0.01", message = "Price must be minimum 0,01 BGN.")
    private double price;

    @NotEmpty(message = "Invalid category")
    private String category;

    @NotEmpty(message = "No images uploaded")
    private List<MultipartFile> images;
}
