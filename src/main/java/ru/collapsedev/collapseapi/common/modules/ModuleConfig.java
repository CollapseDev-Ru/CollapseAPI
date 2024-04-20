package ru.collapsedev.collapseapi.common.modules;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.FileConfiguration;
import org.yaml.snakeyaml.error.YAMLException;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModuleConfig {

    final String main, name;
    String version;
    List<String> authors;
    String description;

    private ModuleConfig(String main, String name) {
        this.main = main;
        this.name = name;
    }

    public static ModuleConfig of(FileConfiguration module) {

        String main = module.getString("main");
        if (main == null) {
            ModuleConfig.yamlException("main");
        }

        String name = module.getString("name");
        if (name == null) {
            ModuleConfig.yamlException("name");
        }

        ModuleConfig moduleWrapper = new ModuleConfig(main, name);

        List<String> authors = new ArrayList<>(module.getStringList("author"));
        if (authors.isEmpty() && module.isString("author")) {
            authors.add(module.getString("author"));
        }
        moduleWrapper.setAuthors(authors);

        String description = module.getString("description");
        if (description != null) {
            moduleWrapper.setDescription(description);
        }

        return moduleWrapper;
    }

    private static void yamlException(String name) {
        throw new YAMLException(String.format("The required '%s' field was not found", name));
    }

}