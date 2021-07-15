package skateKAPPA;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class GithubQuery {

    public static boolean checkMerged(String owner, String repo, int pr) throws IOException {
        URL url = new URL("https://api.github.com/repos/" + owner + "/" + repo + "/pulls/" + pr);
        Reader reader = new InputStreamReader(url.openStream());
        JsonObject json = SkateKAPPA.GSON.fromJson(reader, JsonObject.class);
        reader.close();
        return json.has("merged") && json.get("merged").getAsBoolean();
    }
}
