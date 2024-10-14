package env;

import com.epam.commons.DataClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Environment extends DataClass<Environment> {
    private static Environment instance;
    public String serverUrl;

    private Environment() {
    }

    public static Environment get() {
        if (instance == null) {
            instance = init();
        }
        return instance;
    }

    private static Environment init() {
        String envName = System.getProperty("environment");
        if (envName == null) {
            envName = "dev";
        }
        Properties props = readProperties(envName);
        return new Environment().set(env -> {
            env.serverUrl = props.getProperty("serverUrl");
        });
    }

    public static Properties readProperties(String envName) {
        Properties props = new Properties();
        try {
            InputStream input = new FileInputStream("src/main/resources/env/" + envName.toLowerCase() + ".properties");
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return props;
    }
}