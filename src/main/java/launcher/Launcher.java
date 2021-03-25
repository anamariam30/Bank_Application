package launcher;

import database.Bootstrapper;

import java.sql.SQLException;

public class Launcher {
    private static final boolean BOOTSTRAP = false;

    public static void main(String[] args) {
        boostrap();
        ComponentFactory componentFactory = new ComponentFactory(false);
        componentFactory.getLoginView().setVisible(true);
    }

    private static void boostrap() {
        if (BOOTSTRAP) {
            try {
                new Bootstrapper().execute();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
