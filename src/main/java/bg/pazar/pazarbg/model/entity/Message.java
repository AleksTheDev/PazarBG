package bg.pazar.pazarbg.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_id")
    private UserEntity from;

    @ManyToOne
    @JoinColumn(name = "to_id")
    private UserEntity to;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    private String content;

    private boolean isReply;
}
