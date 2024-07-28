package coollaafi.wot.login.dto;


import coollaafi.wot.jwt.AuthTokens;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDTO {
    private Long id;
    private AuthTokens token;

    public LoginResponseDTO(Long id, AuthTokens token) {
        this.id = id;
        this.token = token;
    }

}
