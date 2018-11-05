package main.utility.state_json;

import com.google.gson.Gson;
import main.utility.ReadWrite;
import main.utility.state_json.json_container.MasterState;

public class MasterJsonUtil {
    public static MasterState jsonObj;
    public static Gson gson = new Gson();

    public static String winDirPath = "C:\\Users\\leozh\\Desktop\\masterjson.txt";
    public static String linDirPath;

    static {
        //@todo use as needed
        String winJson = ReadWrite.readFromFile(winDirPath);
        String linJson = ReadWrite.readFromFile(linDirPath);

        jsonObj = gson.fromJson(winJson, MasterState.class);
    }

    //dump state
    public static void writeState() {

    }
    //dump state with params
    public static void writeState(String ... strings) {

    }
}
