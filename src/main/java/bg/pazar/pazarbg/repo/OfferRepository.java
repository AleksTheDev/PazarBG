package bg.pazar.pazarbg.repo;

import bg.pazar.pazarbg.model.entity.Offer;
import bg.pazar.pazarbg.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findAllByBoughtByIsNull();
    List<Offer> findAllByBoughtBy(UserEntity boughtBy);
}
