package ru.collapsedev.collapseapi.common.modules;

import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.collapsedev.collapseapi.APILoader;
import ru.collapsedev.collapseapi.api.module.PluginModule;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class PluginModules {
    Map<String, ModuleLoader> pluginModules = new ConcurrentHashMap<>();

    public PluginModules() {

        String pathModules = APILoader.getInstance().getDataFolder() + File.separator + "modules";
        File modulesDirectory = new File(pathModules);
        modulesDirectory.mkdirs();

        List<ModuleLoader> modules = getPlugins(modulesDirectory);
        modules.forEach(moduleLoader -> {
            ModuleConfig config = moduleLoader.getConfig();
            pluginModules.put(config.getName(), moduleLoader);
        });

        loadModules();
    }

    public void loadModules() {
        pluginModules.values().forEach(ModuleLoader::loadModule);
    }
    public void unloadModules() {
        pluginModules.values().forEach(ModuleLoader::unloadModule);
    }

    public ModuleLoader getModule(String name) {
        return pluginModules.get(name);
    }

    public List<ModuleLoader> getPlugins(File directory) {
        List<ModuleLoader> loadedPlugins = new ArrayList<>();

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".jar"));
        if (files == null) {
            return Collections.emptyList();
        }

        for (File file : files) {
            try {
                ModuleLoader module = initModule(file);
                loadedPlugins.add(module);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return loadedPlugins;
    }

    public ModuleLoader initModule(File file) throws Exception {
        @Cleanup JarFile jarFile = new JarFile(file);
        JarEntry entry = jarFile.getJarEntry("module.yml");

        if (entry == null) {
            throw new FileNotFoundException("module.yml not found in: " + file.getName());
        }

        @Cleanup InputStream inputStream = jarFile.getInputStream(entry);
        @Cleanup InputStreamReader reader = new InputStreamReader(inputStream);
        FileConfiguration cfgModule = YamlConfiguration.loadConfiguration(reader);

        ModuleConfig moduleConfig = ModuleConfig.of(cfgModule);
        System.out.println("moduleConfig.getMain() = " + moduleConfig.getMain());

        URL[] urls = {file.toURI().toURL()};
        System.out.println(Arrays.toString(urls));
        @Cleanup URLClassLoader classLoader = new URLClassLoader(urls);
        System.out.println(classLoader);
        Class<?> mainClass = classLoader.loadClass(moduleConfig.getMain());

        Constructor<?> constructor = mainClass.getConstructor();

        PluginModule pluginModule = (PluginModule) constructor.newInstance();
        return new ModuleLoader(pluginModule, moduleConfig);
    }
}
