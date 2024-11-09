package client.view.utils;

import java.io.*;

public class Handler_IO <T> {

        private final String target_file;

        public Handler_IO(String file_path) {
            target_file = file_path;
        }

        public void writeFile(String data, boolean append) {
            
            try {
                // Define a codificação UTF-8 para suportar caracteres especiais
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target_file, append), "UTF-8"));
                writer.write(data);
                writer.close();
            
            } catch (IOException e) {
                
                System.out.println(e.getMessage());
                System.exit(1);
            
            }
        }

        public void addRecord(T instance) {

            writeFile(instance.toString() + "\n", true);

        }

        public String searchForRecord(int column, String target) {

            StringBuilder result = new StringBuilder();
            boolean found = false;

            try {
                BufferedReader reader = new BufferedReader(new FileReader(target_file));
                String line;

                while ((line = reader.readLine()) != null) {

                    String[] fields = line.split(",");

                    // Verifica se a coluna é válida e se o valor da coluna corresponde ao objetivo
                    if (column >= 0 && column < fields.length && fields[column].trim().equalsIgnoreCase(target.trim())) {

                        result.append(line);
                        found = true;

                    }

                }

                reader.close();

            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }

            // Retorna uma mensagem caso nenhum registro seja encontrado
            return found ? result.toString() : "Nenhum registro encontrado para: " + target;

        }

        public String readFile() {

            StringBuilder result = new StringBuilder();

            try {

                BufferedReader reader = new BufferedReader(new FileReader(target_file));
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }

                reader.close();

            } catch (IOException e) {

                System.out.println(e.getMessage());
                System.exit(1);

            }

            return result.toString();

        }

        public void clearFileContent() {

            writeFile("", false);

        }

}
