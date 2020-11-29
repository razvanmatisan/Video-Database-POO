package fileio;

import common.Constants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

/**
 * The class writes the output in files
 * <p>
 * DO NOT MODIFY
 */
public final class Writer {
    /**
     * The file where the data will be written
     */
    private final FileWriter file;

    public Writer(final String path) throws IOException {
        this.file = new FileWriter(path);
    }

    /**
     * Transforms the output in a JSONObject
     *
     * @param id      of the action
     * @param message to be written
     * @return An JSON Object
     */
    public JSONObject writeFile(final int id,
                                final String message) {
        JSONObject object = new JSONObject();

        // noinspection unchecked
        object.put(Constants.ID_STRING, id);

        // noinspection unchecked
        object.put(Constants.MESSAGE, message);

        return object;
    }

    /**
     * writes to the file and close it
     *
     * @param array of JSON
     */
    public void closeJSON(final JSONArray array) {
        try {
            file.write(array.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
