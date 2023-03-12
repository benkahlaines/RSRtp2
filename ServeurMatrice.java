import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ServeurMatrice {
    private static Map<String, String> users = new ConcurrentHashMap<String, String>();
    private static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        int port = 4444;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Le serveur est en attente de connexion sur le port " + port + "...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            pool.execute(new ClientHandler(clientSocket, users));
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Map<String, String> users;

    public ClientHandler(Socket socket, Map<String, String> users) {
        this.clientSocket = socket;
        this.users = users;
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Authentification
            Utilisateur user = authenticate(in, out);
            if (user == null) {
                return;
            }

            // Opération sur les matrices
            String operation = in.readLine();
            String[] params = in.readLine().split(",");

            double[][] result = null;

            switch (operation) {
                case "sum":
                    double[][] mat1 = getMatrix(params[0]);
                    double[][] mat2 = getMatrix(params[1]);
                    result = Matrix.sum(mat1, mat2);
                    break;
                case "product":
                    mat1 = getMatrix(params[0]);
                    mat2 = getMatrix(params[1]);
                    result = Matrix.product(mat1, mat2);
                    break;
                case "transpose":
                    double[][] mat = getMatrix(params[0]);
                    result = Matrix.transpose(mat);
                    break;
                default:
                    out.println("Unknown operation");
                    break;
            }

            // Envoi de la réponse
            if (result != null) {
                out.println("result");
                for (int i = 0; i < result.length; i++) {
                    out.println(Arrays.toString(result[i]));
                }
            }

            // Fermeture de la connexion
            out.close();
            in.close();
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Utilisateur authenticate(BufferedReader in, PrintWriter out) throws IOException {
        out.println("Please enter your username:");
        String username = in.readLine();

        out.println("Please enter your password:");
        String password = in.readLine();

        if (users.containsKey(username)) {
            if (!users.get(username).equals(password)) {
                out.println("Invalid password");
                return null;
            }
        } else {
            users.put(username, password);
        }

        Utilisateur user = new Utilisateur(username, password);
        out.println("You are now authenticated as " + username);
        return user;
    }

    private double[][] getMatrix(String matrixString) {
        String[] rows = matrixString.split(";");
        double[][] matrix = new double[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] cols = rows[i].split(",");
            matrix[i] = new double[cols.length];
            for (int j = 0; j < cols.length; j++) {
                matrix[i][j] = Double.parseDouble(cols[j]);
            }
        }
        return matrix;
    }
}
