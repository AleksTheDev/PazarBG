package bg.pazar.pazarbg.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserViewModel {
    private Long id;
    private String username;
    private String email;
    private int offersAdded;
    private int offersBought;
    private int messagesSent;
    private boolean isAdmin;
}
