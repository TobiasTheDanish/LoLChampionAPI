package dat.sem3.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChampionDTO {
    private String name;
    private String title;
    private String detailLink;
    private String imageLink;
    private LocalDate releaseDate;
}
