package bg.pazar.pazarbg.service;

import bg.pazar.pazarbg.model.dto.offer.AddOfferBindingModel;
import bg.pazar.pazarbg.model.entity.Category;
import bg.pazar.pazarbg.model.entity.Offer;
import bg.pazar.pazarbg.model.view.OfferViewModel;

import java.io.IOException;
import java.util.List;

public interface OfferService {
    void addOffer(AddOfferBindingModel addOfferBindingModel) throws IOException;

    List<OfferViewModel> getAllOffersViewModels();

    List<OfferViewModel> getAllOffersViewModelsByCategory(Category category);

    OfferViewModel generateViewModelForOffer(Offer offer);
}
