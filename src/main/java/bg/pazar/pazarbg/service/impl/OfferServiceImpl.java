package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.exception.OfferNotFoundException;
import bg.pazar.pazarbg.model.dto.offer.AddOfferBindingModel;
import bg.pazar.pazarbg.model.entity.*;
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

    //Add offer
    @Override
    public Offer addOffer(AddOfferBindingModel addOfferBindingModel) throws IOException {
        Offer offer = modelMapper.map(addOfferBindingModel, Offer.class);

        offer.setCategory(categoryRepository.findByName(addOfferBindingModel.getCategory()));

        //Set author to the logged-in user
        UserEntity user = userRepository.findByUsername(authenticationService.getCurrentUserName());

        offer.setCreatedBy(user);

        //Save offer entity to database to generate id
        offer = offerRepository.save(offer);

        //Save the images for the offer
        imageService.saveImages(addOfferBindingModel.getImages(), offer, user);

        //Save the offer entity to database
        offerRepository.save(offer);

        //Return the offer entity for testing
        return offer;
    }

    //Buy offer
    @Override
    public Offer buyOffer(Long id) {
        //Check if offer exists
        Offer offer = offerRepository.findById(id).orElse(null);
        if (offer == null) throw new OfferNotFoundException();
        if (offer.getBoughtBy() != null) throw new OfferNotFoundException();

        //Set the buyer to the logged-in user
        UserEntity buyer = userRepository.findByUsername(authenticationService.getCurrentUserName());

        //Check if user is trying to buy his own offer
        if (offer.getCreatedBy().getUsername().equals(buyer.getUsername())) return null;

        offer.setBoughtBy(buyer);

        //Add offer to user's bought offers
        Set<Offer> boughtOffers = buyer.getBoughtOffers();
        boughtOffers.add(offer);
        buyer.setBoughtOffers(boughtOffers);

        offerRepository.save(offer);
        userRepository.save(buyer);

        //Return offer entity for testing
        return offer;
    }

    //Get all offers' view models for the homepage
    @Override
    public List<OfferViewModel> getAllOffersViewModels() {
        List<Offer> offers = offerRepository.findAllByBoughtByIsNull();
        List<OfferViewModel> models = new ArrayList<>();

        offers.forEach(offer -> {
            OfferViewModel model = generateViewModelForOffer(offer);
            if(model != null) models.add(model);
        });

        return models;
    }

    //Get bought offers' view models for the bought offers page
    @Override
    public List<OfferViewModel> getBoughtOffersViewModels() {
        UserEntity currentUser = userRepository.findByUsername(authenticationService.getCurrentUserName());

        List<Offer> offers = offerRepository.findAllByBoughtBy(currentUser);
        List<OfferViewModel> models = new ArrayList<>();

        offers.forEach(offer -> {
            OfferViewModel model = generateViewModelForOffer(offer);
            if(model != null) models.add(model);
        });

        return models;
    }

    //Get offers' view models from a specific category
    @Override
    public List<OfferViewModel> getAllOffersViewModelsByCategory(Category category) {
        Set<Offer> offers = category.getOffers();
        List<OfferViewModel> models = new ArrayList<>();

        offers.forEach(offer -> {
            if (offer.getBoughtBy() == null) {
                OfferViewModel model = generateViewModelForOffer(offer);
                if(model != null) models.add(model);
            }
        });

        return models;
    }

    @Override
    public OfferViewModel generateViewModelForOffer(Offer offer) {
        OfferViewModel model = modelMapper.map(offer, OfferViewModel.class);

        model.setCreatedBy(offer.getCreatedBy().getUsername());
        model.setCategory(offer.getCategory().getName());

        if(offer.getImages().isEmpty()) return null;

        List<Long> imagesIds = new ArrayList<>(offer.getImages().stream().map(Image::getId).toList());

        model.setFirstImageId(imagesIds.get(0));

        imagesIds.remove(0);

        model.setImagesIds(imagesIds);

        if (offer.getCreatedBy().getUsername().equals(authenticationService.getCurrentUserName())) {
            model.setUserOfferCreator(true);
        }

        return model;
    }
}
