package mv.sdd.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Lecture du fichier d'actions
public class ActionFileReader {
    public static List<Action> readActions(String filePath) throws IOException {
        List<Action> actions = new ArrayList<>();

        // TODO : Ajouter le code qui permet de lire et parser un fichier d'actions
        BufferedReader br = Files.newBufferedReader(Path.of(filePath));
        String ligne = br.readLine();

        while (ligne != null) {
            ligne = ligne.trim();
            boolean ajouter = true;

            if (ligne.isEmpty()) {
                ajouter = false;
            }

            if (ligne.startsWith("#")) {
                ajouter = false;
            }

            if (ajouter) {
                Action action = ActionParser.parseLigne(ligne);
                actions.add(action);
            }
            ligne = br.readLine();
        }

        br.close();
        return actions;
    }
}
