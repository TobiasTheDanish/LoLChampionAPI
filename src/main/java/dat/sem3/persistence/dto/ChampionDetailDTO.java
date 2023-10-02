package dat.sem3.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChampionDetailDTO {
    private String positions;
    private String resource;
    private String rangeType;
    private String adaptiveType;
}
