package client;

import client.facade.ClientFacade;
import client.view.AuthenticationView;

import javax.swing.*;

public class ClientApp {

    public static ClientFacade clientFacade;

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(ClientApp::closeResources));
        SwingUtilities.invokeLater(() -> new AuthenticationView().setVisible(true));

    }

    public static void closeResources() {

        System.out.println("CLOSING::CLIENT::FACADE");
        ClientFacade.closeConnection();

    }

}