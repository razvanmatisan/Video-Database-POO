package action;

import fileio.Writer;
import org.json.simple.JSONObject;

import java.io.IOException;

/**
 * Interface for calling an action.
 */
public interface Action {
    /**
     * @return a JSONObject for output file
     */
    JSONObject callAction(Writer fileWriter) throws IOException;
}

