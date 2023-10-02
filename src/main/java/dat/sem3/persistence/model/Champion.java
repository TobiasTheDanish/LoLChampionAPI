package dat.sem3.persistence.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import dat.sem3.persistence.dto.ChampionDTO;
import dat.sem3.persistence.dto.ChampionDetailDTO;
import dat.sem3.util.LocalDateSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "champions")
public class Champion implements IEntity<String>{
    @Id
    private String id;
    private String name;
    private String title;
    private String imageLink;
    private LocalDate releaseDate;

    @OneToOne(mappedBy = "champion", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private ChampionDetails details;
    public Champion(ChampionDTO championDTO, ChampionDetailDTO championDetailDTO) {
        this.id = championDTO.getName() + " " + championDTO.getTitle();
        this.name = championDTO.getName();
        this.title = championDTO.getTitle();
        this.imageLink = championDTO.getImageLink();
        this.releaseDate = championDTO.getReleaseDate();

        addDetails(new ChampionDetails(championDetailDTO));
    }

    public Champion(ChampionDTO championDTO) {
        this.name = championDTO.getName();
        this.title = championDTO.getTitle();
        this.imageLink = championDTO.getImageLink();
        this.releaseDate = championDTO.getReleaseDate();
        details = null;
    }

    @Builder
    public Champion(String name, String title, String imageLink, LocalDate releaseDate, ChampionDetails details) {
        this.name = name;
        this.title = title;
        this.imageLink = imageLink;
        this.releaseDate = releaseDate;
        this.details = details;
    }

    public void addDetails(ChampionDetails details) {
        this.details = details;
        details.setChampion(this);
    }
}
