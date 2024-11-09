package client;

import server.db.DB;
import client.view.AuthenticationView;

import javax.swing.*;

public class ClientApp {
    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(DB::closeConnectionPool));
        SwingUtilities.invokeLater(() -> new AuthenticationView().setVisible(true));

    }
}