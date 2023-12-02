package dat.sem3.api.config;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.Header;
import io.javalin.security.RouteRole;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class AccessManager implements io.javalin.security.AccessManager {
    @Override
    public void manage(@NotNull Handler handler, @NotNull Context context, @NotNull Set<? extends RouteRole> set) throws Exception {
        context.header(Header.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        context.header(Header.ACCESS_CONTROL_ALLOW_METHODS, "GET");

        handler.handle(context);
    }
}
