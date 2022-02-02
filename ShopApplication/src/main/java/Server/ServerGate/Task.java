package Server.ServerGate;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import static Server.ServerGate.ServerRequestHandler.Session;

public abstract class Task {
    private final Type[] args;

    public Task(Type... args) {
        this.args = args;
    }

    public String execute(Session currentSession, String[] jsonArgs) {
        Object[] objectArgs = new Object[jsonArgs.length];
        Gson gson = new Gson();
        if (!jsonArgs[0].equals("")) {
            for (int i = 0; i < jsonArgs.length; i++) {
                objectArgs[i] = gson.fromJson(jsonArgs[i], args[i]);
            }
        }
        Object returnValue = executeMethod(currentSession, objectArgs);
        if (returnValue instanceof String && ((String) returnValue).startsWith("exception:")) {
            return (String) returnValue;
        } else return gson.toJson(returnValue);
    }

    public abstract Object executeMethod(Session currentSession, Object[] objectArgs);
}
