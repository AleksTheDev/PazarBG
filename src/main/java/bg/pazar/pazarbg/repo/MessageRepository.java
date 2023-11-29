package bg.pazar.pazarbg.repo;

import bg.pazar.pazarbg.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByToId(Long to_id);
    List<Message> findAllByFromId(Long from_id);
}
