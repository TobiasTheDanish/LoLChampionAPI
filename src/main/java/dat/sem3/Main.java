package dat.sem3;

import dat.sem3.api.application.IRestApplication;
import dat.sem3.api.application.JavalinRestAPI;

public class Main {
    public static void main(String[] args) {
        IRestApplication app = new JavalinRestAPI();
        app.setup();
        app.start(42069);
    }
}
