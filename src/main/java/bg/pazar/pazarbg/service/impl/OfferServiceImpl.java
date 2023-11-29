package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.exception.OfferNotFoundException;
import bg.pazar.pazarbg.model.dto.offer.AddOfferBindingModel;
import bg.pazar.pazarbg.model.entity.*;
import bg.pazar.pazarbg.model.view.OfferViewModel;
import bg.pazar.pazarbg.repo.CategoryRepository;
import bg.pazar.pazarbg.repo.MessageRepository;
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
    private final MessageRepository messageRepository;
    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;

    public OfferServiceImpl(ImageService imageService, OfferRepository offerRepository, CategoryRepository categoryRepository, UserRepository userRepository, MessageRepository messageRepository, AuthenticationService authenticationService, ModelMapper modelMapper) {
        this.imageService = imageService;
        this.offerRepository = offerRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
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

        imageService.saveImages(addOfferBindingModel.getImages(), offer, user);

        offerRepository.save(offer);
    }

    @Override
    public void buyOffer(Long id) {
        Offer offer = offerRepository.findById(id).orElse(null);
        if(offer == null) throw new OfferNotFoundException();
        if(offer.getBoughtBy() != null) throw new OfferNotFoundException();

        UserEntity buyer = userRepository.findByUsername(authenticationService.getCurrentUserName());

        if(offer.getCreatedBy().getUsername().equals(buyer.getUsername())) return;

        offer.setBoughtBy(buyer);

        Set<Offer> boughtOffers = buyer.getBoughtOffers();
        boughtOffers.add(offer);
        buyer.setBoughtOffers(boughtOffers);

        offerRepository.save(offer);
        userRepository.save(buyer);
    }

    @Override
    public void sendMessage(Long offerID, String content) {
        if(content.length() < 20 || content.length() > 200) return;

        Offer offer = offerRepository.findById(offerID).orElse(null);
        if(offer == null) throw new OfferNotFoundException();

        UserEntity from = userRepository.findByUsername(authenticationService.getCurrentUserName());
        UserEntity to = offer.getCreatedBy();

        if(from.getUsername().equals(to.getUsername())) return;

        Message message = new Message();

        message.setFrom(from);
        message.setTo(to);
        message.setOffer(offer);
        message.setContent(content);

        messageRepository.save(message);
    }

    @Override
    public List<OfferViewModel> getAllOffersViewModels() {
        List<Offer> offers = offerRepository.findAllByBoughtByIsNull();
        List<OfferViewModel> models = new ArrayList<>();

        offers.forEach(offer -> models.add(generateViewModelForOffer(offer)));

        return models;
    }

    @Override
    public List<OfferViewModel> getBoughtOffersViewModels() {
        UserEntity currentUser = userRepository.findByUsername(authenticationService.getCurrentUserName());

        List<Offer> offers = offerRepository.findAllByBoughtBy(currentUser);
        List<OfferViewModel> models = new ArrayList<>();

        offers.forEach(offer -> models.add(generateViewModelForOffer(offer)));

        return models;
    }

    @Override
    public List<OfferViewModel> getAllOffersViewModelsByCategory(Category category) {
        Set<Offer> offers = category.getOffers();
        List<OfferViewModel> models = new ArrayList<>();

        offers.forEach(offer -> {
            if (offer.getBoughtBy() == null) models.add(generateViewModelForOffer(offer));
        });

        return models;
    }

    @Override
    public OfferViewModel generateViewModelForOffer(Offer offer) {
        OfferViewModel model = modelMapper.map(offer, OfferViewModel.class);

        model.setCreatedBy(offer.getCreatedBy().getUsername());
        model.setCategory(offer.getCategory().getName());

        List<Long> imagesIds = new ArrayList<>(offer.getImages().stream().map(Image::getId).toList());

        model.setFirstImageId(imagesIds.get(0));

        imagesIds.remove(0);

        model.setImagesIds(imagesIds);

        if(offer.getCreatedBy().getUsername().equals(authenticationService.getCurrentUserName())) {
            model.setUserOfferCreator(true);
        }

        return model;
    }
}
