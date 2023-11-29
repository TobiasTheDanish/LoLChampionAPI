package dat.sem3.parsing;

import dat.sem3.api.util.QueryParamParser;
import dat.sem3.parsing.exceptions.ParseException;
import dat.sem3.persistence.dao.ChampionDAO;
import dat.sem3.persistence.model.Champion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws ParseException {
        List<String> input = Arrays.stream(new String[]{"champion.details.positions = Middle", "champion.details.rangeType != Ranged", "champion.title *= the"}).collect(Collectors.toList());
        List<Predicate<ParserTest>> res = QueryParamParser.parseFilters(input, ParserTest.class);

        Stream<Champion> champions = new ChampionDAO().findAll().stream();
        List<ParserTest> tests = new ArrayList<>();
        champions.forEach(champion -> tests.add(new ParserTest(champion)));

        Stream<ParserTest> testStream = tests.stream();
        for (Predicate<ParserTest> predicate : res) {
            testStream = testStream.filter(predicate);
        }
        testStream.forEach(System.out::println);
    }
}
