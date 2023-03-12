import java.net.*;
import java.io.*;
import java.util.*;

public class ClientMatrice {
    public static void main(String[] args) throws IOException {
        String hostName = "localhost";
        int portNumber = 4444;

        try (
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            Utilisateur user = authenticate(in, out, stdIn);
            if (user == null) {
                return;
            }

            System.out.println("Vous êtes maintenant connecté au serveur.");

            String operation = "";
            while (!operation.equals("exit")) {
                System.out.println("Entrez une opération (sum, product, transpose ou exit) : ");
                operation = stdIn.readLine();

                switch (operation) {
                    case "sum":
                        System.out.println("Entrez la première matrice (séparée par des ';') : ");
                        String mat1 = stdIn.readLine();
                        System.out.println("Entrez la deuxième matrice (séparée par des ';') : ");
                        String mat2 = stdIn.readLine();
                        out.println("sum");
                        out.println(mat1);
                        out.println(mat2);
                        break;
                    case "product":
                        System.out.println("Entrez la première matrice (séparée par des ';') : ");
                        mat1 = stdIn.readLine();
                        System.out.println("Entrez la deuxième matrice (séparée par des ';') : ");
                        mat2 = stdIn.readLine();
                        out.println("product");
                        out.println(mat1);
                        out.println(mat2);
                        break;
                    case "transpose":
                        System.out.println("Entrez la matrice (séparée par des ';') : ");
                        String mat = stdIn.readLine();
                        out.println("transpose");
                        out.println(mat);
                        break;
                    case "exit":
                        System.out.println("Déconnexion.");
                        break;
                    default:
                        System.out.println("Opération invalide.");
                        break;
                }

                String response = in.readLine();
                if (response.equals("result")) {
                    System.out.println("Résultat :");
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                } else {
                    System.out.println("Erreur : " + response);
                }
            }

            out.close();
            in.close();
            stdIn.close();
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Host inconnu : " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Connexion impossible à " + hostName);
            System.exit(1);
        }
    }

    private static Utilisateur authenticate(BufferedReader in, PrintWriter out, BufferedReader stdIn) throws IOException {
        out.println("Authentification");

        System.out.println("Entrez votre nom d'utilisateur : ");
        String username = stdIn.readLine();

        System.out.println("Entrez votre mot de passe : ");
        String password = stdIn.readLine();

        Utilisateur user = new Utilisateur(username, password);
        out.println(username);
        out.println(password);

        String response = in.readLine();
        if (response.startsWith("Erreur")) {
            System.out.println(response);
            return null;
        } else {
            System.out.println(response);
        }

        return user;
    }
}
