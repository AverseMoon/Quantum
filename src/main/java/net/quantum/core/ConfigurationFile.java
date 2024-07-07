package net.quantum.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.quantum.Quantum;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class used for configurations.
 */
public class ConfigurationFile {
    /**
     * File path of the configuration file.
     * @see ConfigurationFile#ConfigurationFile(String)
     */
    public final Path path;
    /**
     * The loaded contents of the configuration file.
     * @see ConfigurationFile#setData(JsonObject)
     * @see ConfigurationFile#loadConfiguration()
     * @see ConfigurationFile#loadAndCheck()
     * @see ConfigurationFile#saveConfiguration()
     * @see ConfigurationFile#saveConfiguration(JsonObject)
     */
    public JsonObject data;
    /**
     * The default file contents of a newly created configuration file.
     * @see ConfigurationFile#setDefaultContents(JsonObject)
     * @see ConfigurationFile#loadAndCheck()
     */
    public JsonObject defaultContents = new JsonObject();

    /**
     * Create a new {@link ConfigurationFile}
     * @param filename The filename of the configuration file, the filename will automatically have .json appended to the end.
     */
    public ConfigurationFile(String filename) {
        path = Quantum.CONFIG_PATH.resolve(filename + ".json");
    }

    /**
     * Setter function of {@link ConfigurationFile#defaultContents}
     * @return this
     */
    public ConfigurationFile setDefaultContents(JsonObject defaultContents) {
        this.defaultContents = defaultContents;
        return this;
    }
    /**
     * Setter function of {@link ConfigurationFile#data}
     * @return this
     */
    public ConfigurationFile setData(JsonObject data) {
        this.data = data;
        return this;
    }

    /**
     * Loads the configuration file into {@link ConfigurationFile#data}
     * @see ConfigurationFile#loadAndCheck()
     * @see JsonObject
     * @return this
     */
    public ConfigurationFile loadConfiguration() throws IOException {
        data = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
        return this;
    }

    /**
     * Similar to {@link ConfigurationFile#loadConfiguration()} however if the file does not exist, it will create a new file,
     * you can set the default file contents with {@link ConfigurationFile#setDefaultContents(JsonObject)}, or just by setting
     * {@link ConfigurationFile#defaultContents} directly.
     * @see ConfigurationFile#loadConfiguration()
     * @return this
     */
    public ConfigurationFile loadAndCheck() throws IOException {
        if (!Files.exists(path)) createNewFile();
        if (!Files.isRegularFile(path)) throw new RuntimeException("'%s' must be a file".formatted(path.toString()));

        return loadConfiguration();
    }

    /**
     * Creates a new configuration file at {@link ConfigurationFile#path}, the contents of the new file will be {@link ConfigurationFile#defaultContents}
     * @see ConfigurationFile#defaultContents
     * @return this
     */
    public ConfigurationFile createNewFile() throws IOException {
        Files.createFile(path);
        saveConfiguration(defaultContents);
        return this;
    }

    /**
     * Saves {@link ConfigurationFile#data} to the file at {@link ConfigurationFile#path}.
     * @see ConfigurationFile#saveConfiguration(JsonObject)
     * @return this
     */
    public ConfigurationFile saveConfiguration() throws IOException {
        return saveConfiguration(data);
    }
    /**
     * Saves {@code data} to the file at {@link ConfigurationFile#path}.
     * @param data the data to be saved.
     * @see ConfigurationFile#saveConfiguration(JsonObject)
     * @return this
     */
    public ConfigurationFile saveConfiguration(JsonObject data) throws IOException {
        Files.writeString(path, data.toString());
        return this;
    }
}
