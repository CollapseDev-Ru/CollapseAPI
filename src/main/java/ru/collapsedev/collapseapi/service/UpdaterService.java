package ru.collapsedev.collapseapi.service;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class UpdaterService implements Listener {
    public static final String UPDATE_MESSAGE = "§8[§6Updater§8] §fУ вас устаревшая версия §6%s §7(%s)." +
            " §fНовая версия: §e%s§f, дата выпуска: §e%s§f. \n§8[§6Updater§8] §fСкачать новую версию: §c%s";
    public static final String LATEST_API_URL = "https://api.github.com/repos/%s/%s/releases/latest";
    public static final String LATEST_URL = "https://github.com/%s/%s/releases/latest";

    private final Plugin plugin;
    private final String user, repo;
    private final String permissionNotify;
    private LatestRelease releases;


    public UpdaterService(Plugin plugin, String user, String repo, String permissionNotify) {
        this.plugin = plugin;
        this.user = user;
        this.repo = repo;
        this.permissionNotify = permissionNotify;

        CompletableFuture.supplyAsync(this::checkUpdate)
                .thenAccept(result -> {
                    if (result) {
                        Bukkit.getScheduler().runTask(plugin, () ->
                                Bukkit.getPluginManager().registerEvents(this, plugin)
                        );
                    }
                });
    }

    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission(permissionNotify)) {
            return;
        }

        player.sendMessage(formattedMessage());
    }

    @SneakyThrows
    private boolean checkUpdate() {
        HttpClient client = HttpClient.newHttpClient();
        String url = String.format(LATEST_API_URL, user, repo);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return false;
        }

        releases = parseLatestRelease(response.body());

        int version = parseVersion(releases.tagName);
        int currentVersion = parseVersion(plugin.getDescription().getVersion());

        if (version > currentVersion) {
            System.out.println(formattedMessage());
            return true;
        }
        return false;
    }

    private int parseVersion(String tag) {
        return Arrays.stream(tag.split("-")[0]
                .replace("v", "")
                .split("\\.")
        ).mapToInt(Integer::parseInt).sum();
    }

    private LatestRelease parseLatestRelease(String response) {
        return new Gson().fromJson(response, LatestRelease.class);
    }

    private static class LatestRelease {
        @SerializedName("tag_name")
        private String tagName;
        @SerializedName("published_at")
        private String publishedAt;
    }

    private String formattedMessage() {
        PluginDescriptionFile desc = plugin.getDescription();
        return String.format(UPDATE_MESSAGE, desc.getName(), desc.getVersion(), releases.tagName,
                releases.publishedAt, String.format(LATEST_URL, user, repo));
    }

}
