package dat.sem3.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dat.sem3.persistence.dto.ChampionDetailDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "champion_details")
@Entity
public class ChampionDetails implements IEntity<String> {
    @JsonIgnore
    @Id
    private String id;
    private String positions;
    private String resource;
    private String rangeType;
    private String adaptiveType;

    @JsonIgnore
    @ToString.Exclude
    @MapsId
    @OneToOne
    private Champion champion;

    @Builder
    public ChampionDetails(String positions, String resource, String rangeType, String adaptiveType) {
        this.positions = positions;
        this.resource = resource;
        this.rangeType = rangeType;
        this.adaptiveType = adaptiveType;
    }

    public ChampionDetails(ChampionDetailDTO dto) {
        this.positions = dto.getPositions();
        this.resource = dto.getResource();
        this.rangeType = dto.getRangeType();
        this.adaptiveType = dto.getAdaptiveType();
    }
}
