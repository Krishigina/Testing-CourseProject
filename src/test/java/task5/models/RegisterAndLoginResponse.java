package task5.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RegisterAndLoginResponse {
    private int id;
    private String token;
    private String error;
}
