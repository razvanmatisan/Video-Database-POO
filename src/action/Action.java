package action;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;

public interface Action {
    JSONObject callAction(Writer fileWriter) throws IOException;
}

