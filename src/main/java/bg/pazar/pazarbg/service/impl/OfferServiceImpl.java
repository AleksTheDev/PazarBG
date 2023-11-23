package bg.pazar.pazarbg.service.impl;

import bg.pazar.pazarbg.model.dto.offer.AddOfferBindingModel;
import bg.pazar.pazarbg.model.entity.Offer;
import bg.pazar.pazarbg.repo.CategoryRepository;
import bg.pazar.pazarbg.repo.OfferRepository;
import bg.pazar.pazarbg.service.ImageService;
import bg.pazar.pazarbg.service.OfferService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OfferServiceImpl implements OfferService {


    private final ImageService imageService;
    private final OfferRepository offerRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public OfferServiceImpl(ImageService imageService, OfferRepository offerRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.imageService = imageService;
        this.offerRepository = offerRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addOffer(AddOfferBindingModel addOfferBindingModel) throws IOException {
        Offer offer = modelMapper.map(addOfferBindingModel, Offer.class);

        offer.setCategory(categoryRepository.findByName(addOfferBindingModel.getCategory()));

        offer = offerRepository.save(offer);

        offer.setImagePaths(imageService.saveImages(addOfferBindingModel.getImages(), offer.getId()));

        offerRepository.save(offer);
    }
}
