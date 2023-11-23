package bg.pazar.pazarbg.repo;

import bg.pazar.pazarbg.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
