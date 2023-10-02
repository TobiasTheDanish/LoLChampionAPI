package dat.sem3.parsing;

import dat.sem3.persistence.model.Champion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParserTest {
    private final Champion champion;

    public ParserTest(Champion champion) {
        this.champion = champion;
    }

    @Override
    public String toString() {
        return "ParserTest{\n" + champion + "\n}";
    }
}
