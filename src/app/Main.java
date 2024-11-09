package app;

import model.db.DB;
import view.AuthenticationView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(DB::closeConnectionPool));
        SwingUtilities.invokeLater(() -> new AuthenticationView().setVisible(true));

    }
}