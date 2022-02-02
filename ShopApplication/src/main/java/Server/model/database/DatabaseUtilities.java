package Server.model.database;

import Server.model.account.Account;
import Server.model.chat.Chat;
import Server.model.request.Request;
import Server.model.sellable.Sellable;
import Server.model.sellable.SubSellable;
import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Scanner;

class DatabaseUtilities {

    static void createMissingDirectory(String name) {
        File directory = Path.of("src/main/resources/server_resources/" + name).toFile();
        directory.mkdirs();
    }

    static File getFile(String fileName) {
        File file = Path.of("src/main/resources/server_resources/" + fileName).toFile();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    static Scanner getScanner(String fileName) {
        File file = getFile(fileName);
        try {
            return new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    static PrintWriter getPrintWriter(String fileName) {
        File file = getFile(fileName);
        try {
            return new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Account.class, new Adapter<Account>());
        builder.registerTypeAdapter(Request.class, new Adapter<Request>());
        builder.registerTypeAdapter(Sellable.class, new Adapter<Sellable>());
        builder.registerTypeAdapter(SubSellable.class, new Adapter<SubSellable>());
        builder.registerTypeAdapter(Chat.class, new Adapter<Chat>());
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
