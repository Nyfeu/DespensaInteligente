package server.prompts;

import server.ServerApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerPrompt extends Thread {

    @Override
    public void run() {

        System.out.println("\n========== SERVER PROMPT ==================\n");
        System.out.println("SERVER::PROMPT::INITIALIZED");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String command;

        while (true) {

            try {

                command = reader.readLine();
                if ("users_number".equalsIgnoreCase(command)) System.out.println(ServerApp.getUsersNumber());
                if ("stop".equalsIgnoreCase(command)) break;

            } catch (IOException e) {

                System.out.println("PROMPT::READ_LINE::FAILED");

            }

        }

        System.out.println("SERVER::PROMPT::FINISHED");

        // Finalizando servidor:
        ServerApp.shutdownServer();

    }

}
