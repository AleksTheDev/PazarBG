package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.model.dto.offer.AddOfferBindingModel;
import bg.pazar.pazarbg.model.entity.Category;
import bg.pazar.pazarbg.model.entity.Offer;
import bg.pazar.pazarbg.model.entity.UserEntity;
import bg.pazar.pazarbg.model.view.OfferViewModel;
import bg.pazar.pazarbg.repo.CategoryRepository;
import bg.pazar.pazarbg.repo.OfferRepository;
import bg.pazar.pazarbg.repo.UserRepository;
import bg.pazar.pazarbg.service.ImageService;
import bg.pazar.pazarbg.service.OfferService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class OfferServiceImpl implements OfferService {


    private final ImageService imageService;
    private final OfferRepository offerRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;

    public OfferServiceImpl(ImageService imageService, OfferRepository offerRepository, CategoryRepository categoryRepository, UserRepository userRepository, AuthenticationService authenticationService, ModelMapper modelMapper) {
        this.imageService = imageService;
        this.offerRepository = offerRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addOffer(AddOfferBindingModel addOfferBindingModel) throws IOException {
        Offer offer = modelMapper.map(addOfferBindingModel, Offer.class);

        offer.setCategory(categoryRepository.findByName(addOfferBindingModel.getCategory()));

        UserEntity user = userRepository.findByUsername(authenticationService.getCurrentUserName());

        offer.setCreatedBy(user);

        offer = offerRepository.save(offer);

        offer.setImagePaths(imageService.saveImages(addOfferBindingModel.getImages(), offer.getId()));

        offerRepository.save(offer);
    }

    @Override
    public List<OfferViewModel> getAllOffersViewModels() {
        List<Offer> offers = offerRepository.findAll();
        List<OfferViewModel> models = new ArrayList<>();

        offers.forEach(offer -> {
            models.add(generateViewModelForOffer(offer));
        });

        return models;
    }

    @Override
    public List<OfferViewModel> getAllOffersViewModelsByCategory(Category category) {
        Set<Offer> offers = category.getOffers();
        List<OfferViewModel> models = new ArrayList<>();

        offers.forEach(offer -> {
            models.add(generateViewModelForOffer(offer));
        });

        return models;
    }

    @Override
    public OfferViewModel generateViewModelForOffer(Offer offer) {
        OfferViewModel model = modelMapper.map(offer, OfferViewModel.class);

        model.setCreatedBy(offer.getCreatedBy().getUsername());
        model.setCategory(offer.getCategory().getName());
        model.setImagesCount(offer.getImagePaths().size());

        return model;
    }
}
