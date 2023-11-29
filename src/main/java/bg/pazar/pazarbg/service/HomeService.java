package bg.pazar.pazarbg.service;

import bg.pazar.pazarbg.model.view.HomeViewModel;

public interface HomeService {
    HomeViewModel getAllOffers();

    HomeViewModel getBoughtOffers();

    HomeViewModel getOffersByCategory(Long id);
}
