package util;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    private static final Gson GSON = new Gson();

    private JsonUtil() {
    }

    public static String success(String message, Object data) {
        return toJson(true, message, data);
    }

    public static String error(String message) {
        return toJson(false, message, null);
    }

    private static String toJson(boolean success, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        return GSON.toJson(response);
    }
}
