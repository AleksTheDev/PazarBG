package bg.pazar.pazarbg.repo;

import bg.pazar.pazarbg.model.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}
