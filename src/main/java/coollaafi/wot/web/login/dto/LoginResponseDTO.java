package coollaafi.wot.web.login.dto;


import coollaafi.wot.jwt.AuthTokens;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDTO {
    private Long id;
    private String name;
    private AuthTokens token;
    private String accessToken;

    public LoginResponseDTO(Long id, AuthTokens token, String accessToken) {
        this.id = id;
        this.token = token;
        this.accessToken = accessToken;
    }

}
