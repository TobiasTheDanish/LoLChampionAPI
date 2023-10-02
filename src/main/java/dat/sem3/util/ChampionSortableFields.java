package dat.sem3.util;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ChampionSortableFields {
    NAME("name"),
    TITLE("title"),
    RELEASE_DATE("releaseDate"),
    POSITIONS("positions"),
    RESOURCE("resource"),
    RANGE_TYPE("rangeType"),
    ADAPTIVE_TYPE("adaptiveType");

    private final String field;

    ChampionSortableFields(String field) {
        this.field = field;
    }

    public static ChampionSortableFields get(String field) throws IllegalArgumentException {
        return Arrays.stream(ChampionSortableFields.values())
                .filter(e -> e.getField().equalsIgnoreCase(field))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Non valid sortable field. Valid fields are: " +
                        "'name', 'title', 'releaseDate', 'positions', 'resource', 'rangeType', 'adaptiveType'."));
    }
}
