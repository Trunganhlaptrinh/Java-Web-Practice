package util;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

/**
 * Chuan hoa JSON response tra ve cho client, format thong nhat:
 * { "success": true/false, "message": "...", "data": ... }
 * Moi Controller deu dung class nay de tra JSON, tranh moi noi tu build 1 kieu
 */


// dùng để Chuẩn hóa JSON trả về
public class JsonUtil {

    private static final Gson gson = new Gson();

    public static String success(String message, Object data) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", message);
        res.put("data", data);
        return gson.toJson(res);
    }

    public static String error(String message) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", false);
        res.put("message", message);
        res.put("data", null);
        return gson.toJson(res);
    }
}
