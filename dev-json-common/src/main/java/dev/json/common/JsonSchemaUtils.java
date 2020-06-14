package dev.json.common;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayInputStream;

public class JsonSchemaUtils {

    public static void validateSchema(byte[] bytes) {

        validateSchema("schema.json", bytes);
    }

    public static void validateSchema(String schemaFile,  byte[] bytes) {

        JSONObject jsonSchema = new JSONObject(new JSONTokener(JsonSchemaUtils.class.getClassLoader().getResourceAsStream(schemaFile)));
        JSONObject jsonSubject = new JSONObject(new JSONTokener(new ByteArrayInputStream(bytes)));
        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonSubject);
    }
}
