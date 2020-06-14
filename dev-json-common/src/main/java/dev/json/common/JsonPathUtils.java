package dev.json.common;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.util.Collection;

public class JsonPathUtils {

    /*
     * Check etc/ folder
     *  List<String> fields = new ArrayList<>();
     *  fields.add("$.data[*]..id");
     *  fields.add("$.data[*]..createdDate");
     *  fields.add("$.data[*]..createdBy");
     *  fields.add("$.data[*]..updatedDate");
     *  fields.add("$.data[*]..updatedBy");
     *
     *  fields.add(".data.ecaConditions[*].send");
     *  fields.add(".data.ecaConditions[*].receive");
     *  fields.add(".data.groups[*].passwordExpiration");
     *  fields.add(".data.groups[*].resources");
     */

    public static String deletePaths(String json, String... pathsToDelete) {

        DocumentContext documentContext = JsonPath.parse(json);

        for (String pathToDelete : pathsToDelete) {
            documentContext.delete(pathToDelete);
        }

        return documentContext.jsonString();
    }

    public static String deletePaths(String json, Collection<String> pathsToDelete) {

        DocumentContext documentContext = JsonPath.parse(json);

        for (String pathToDelete : pathsToDelete) {
            documentContext.delete(pathToDelete);
        }

        return documentContext.jsonString();
    }
}
