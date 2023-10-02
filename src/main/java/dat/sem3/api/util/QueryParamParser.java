package dat.sem3.api.util;

import dat.sem3.parsing.*;
import dat.sem3.parsing.exceptions.ParseException;

import java.util.List;
import java.util.function.Predicate;

public class QueryParamParser {

    /**
     * Parses a list of filter strings for a specific format.
     * An example could look like this:
     * <p>
     *      'owner.name = Steve'
     * <p>
     * Here 'owner.name' represent nested objects within an object of type K.
     * <p>
     * Each value on the left hand side of the '=' (the operation), is considered a field within a class.
     * Here 'owner' would be a field within a class of type K, and name is a field within the class of 'owner'.
     * <p>
     * Multiple nested fields is allowed. However, the final field must always be a string value.
     * <p>
     * <p>
     * There are 3 different operations to choose from.
     * '=' is the equals operator, and matches whether the value of the final field, is equal to the value on the right hand side.
     * '!=' is the not equals operator, and matches whether the fields values are not equal.
     * '*=' is the contains operator, and matches if the final fields value contains the value.
     * <p>
     * Whitespace is skipped and therefore irrelevant for the final result.
     * @param filterStrings a list of strings matching the specified format.
     * @param cls the class of type K, of which the first field is a member of.
     * @return a list of predicates for type K which follows the rules of the filter string.
     * @param <K> the type of which is going to be filtered by.
     */
    public static <K> List<Predicate<K>> parseFilters(List<String> filterStrings, Class<K> cls) {
        List<Predicate<K>> predicates;

        String input = String.join(", ", filterStrings);

        Lexer lexer = new LexerImpl(input);
        Parser parser = new ParserImpl(lexer, 2);
        try {
            AstNode root = parser.parse();
            predicates = new PredicateVisitor<K>().visit(root, cls);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return predicates;
    }
}
