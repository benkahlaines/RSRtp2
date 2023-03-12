import java.io.Serializable;

public class Utilisateur implements Serializable {
    private String id;
    private String pass;

    public Utilisateur(String id, String pass) {
        this.id = id;
        this.pass = pass;
    }

    public String getId() {
        return id;
    }

    public String getPass() {
        return pass;
    }
}


