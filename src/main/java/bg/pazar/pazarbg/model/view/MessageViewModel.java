package bg.pazar.pazarbg.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MessageViewModel {
    private Long id;
    private String from;
    private String offerTitle;
    private String content;
    private boolean isReply;
}
