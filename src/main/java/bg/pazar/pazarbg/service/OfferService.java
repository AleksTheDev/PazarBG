package bg.pazar.pazarbg.service;

import bg.pazar.pazarbg.model.dto.offer.AddOfferBindingModel;
import bg.pazar.pazarbg.model.entity.Category;
import bg.pazar.pazarbg.model.entity.Offer;
import bg.pazar.pazarbg.model.view.OfferViewModel;

import java.io.IOException;
import java.util.List;

public interface OfferService {
    Offer addOffer(AddOfferBindingModel addOfferBindingModel) throws IOException;

    Offer buyOffer(Long id);

    List<OfferViewModel> getAllOffersViewModels();

    List<OfferViewModel> getBoughtOffersViewModels();

    List<OfferViewModel> getAllOffersViewModelsByCategory(Category category);

    OfferViewModel generateViewModelForOffer(Offer offer);
}
