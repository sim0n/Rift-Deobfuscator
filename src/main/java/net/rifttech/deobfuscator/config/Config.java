package net.rifttech.deobfuscator.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.rifttech.deobfuscator.Deobfuscator;

import java.io.*;

@Getter
public class Config {
    public static final Gson GSON_NON_PRETTY = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .disableHtmlEscaping()
            .create();

    public static final Gson GSON_PRETTY = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    private final Deobfuscator deobfuscator = Deobfuscator.getInstance();

    private Conf conf;

    public void start() {
        try {
            conf = loadConf(new File("config.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Conf loadConf(File file) throws IOException {
        try (FileReader fileReader = new FileReader(file)) {
            return GSON_NON_PRETTY.fromJson(fileReader, Conf.class);
        } catch (FileNotFoundException ex) {
            deobfuscator.getLogger().warn("Couldn't find a default config, saving default...");

            Conf conf = new Conf();

            saveConf(conf, new File("config.json"));

            return conf;
        }
    }

    private void saveConf(Conf conf, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            GSON_PRETTY.toJson(conf, writer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
