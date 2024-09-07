package kr.ac.kopo.ecoalignbackend.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class Token {
    @SchemaProperty
    @NotNull
    private String grantType;

    @SchemaProperty
    @NotNull
    private String accessToken;
}
