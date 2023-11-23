package bg.pazar.pazarbg.service;

import bg.pazar.pazarbg.model.dto.offer.AddOfferBindingModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OfferService {
    void addOffer(AddOfferBindingModel addOfferBindingModel) throws IOException;
}
