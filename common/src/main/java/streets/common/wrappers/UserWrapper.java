package streets.common.wrappers;

import streets.common.entities.Role;
import streets.common.entities.Street;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWrapper {
    private long id;
    private String username;
    private String password;
    private Role role;
    private Street street;
}
