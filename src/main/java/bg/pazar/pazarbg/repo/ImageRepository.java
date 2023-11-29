package bg.pazar.pazarbg.repo;

import bg.pazar.pazarbg.model.entity.Image;
import bg.pazar.pazarbg.model.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByOffer(Offer offer);
}
