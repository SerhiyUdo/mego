package utils;

import com.epam.http.response.RestResponse;
import com.jayway.jsonpath.internal.JsonFormatter;

import java.util.List;

public class ResponseUtil {

    public static void printResponseAsJson(RestResponse response) {
        printResponseHeaders(response);
        System.out.println(JsonFormatter.prettyPrint(String.valueOf(response)));
    }

    public static <T> List<T> getListFromResponse(RestResponse response, String path, Class<T> clazz) {
        return response.getRaResponse().jsonPath().getList(path, clazz);
    }

    public static Long getLongFromResponse(RestResponse response, String path) {
        return response.getRaResponse().getBody().jsonPath().getLong(path);
    }

    public static void printResponseHeaders(RestResponse response) {
        System.out.println("Response headers are ===> " + response.getRaResponse().headers());
    }


}
