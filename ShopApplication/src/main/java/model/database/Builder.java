package model.database;

import com.google.gson.*;
import model.account.Account;
import model.request.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Scanner;

class Builder {
    private static void createMissingFile(File file) {
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException ignored) {
        }
    }

    private static File getFile(String fileName) {
        return Path.of("src/main/resources/database/" + fileName).toFile();
    }

    static Scanner buildScanner(String fileName) {
        File file = getFile(fileName);
        try {
            return new Scanner(file);
        } catch (FileNotFoundException e) {
            createMissingFile(file);
            return buildScanner(fileName);
        }
    }

    static PrintWriter buildPrintWriter(String fileName) {
        File file = getFile(fileName);
        try {
            return new PrintWriter(file);
        } catch (FileNotFoundException e) {
            createMissingFile(file);
            return buildPrintWriter(fileName);
        }
    }

    static Gson buildGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Account.class, new Adapter<Account>());
        builder.registerTypeAdapter(Request.class, new Adapter<Request>());
        return builder.create();
    }

    private static class Adapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
        private static final String CLASSNAME = "CLASSNAME";
        private static final String INSTANCE = "INSTANCE";

        @Override
        public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject retValue = new JsonObject();
            String className = src.getClass().getName();
            retValue.addProperty(CLASSNAME, className);
            JsonElement elem = context.serialize(src);
            retValue.add(INSTANCE, elem);
            return retValue;
        }

        @Override
        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
            String className = prim.getAsString();

            Class<?> classType;
            try {
                classType = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new JsonParseException(e.getMessage());
            }
            return context.deserialize(jsonObject.get(INSTANCE), classType);
        }
    }
}
