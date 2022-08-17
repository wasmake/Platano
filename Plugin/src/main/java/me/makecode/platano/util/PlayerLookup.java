package me.makecode.platano.util;

import net.minecraft.util.com.google.gson.JsonObject;
import net.minecraft.util.com.google.gson.JsonParser;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerLookup {
    private static final String MINECRAFT_API = "https://api.minetools.eu/uuid/";
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    public static boolean checkPlayerIsPremium(String playerName) {
        try {
            StringBuilder builder = new StringBuilder();
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(MINECRAFT_API + playerName).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            Scanner scanner = new Scanner(httpURLConnection.getInputStream());
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }

            scanner.close();
            httpURLConnection.disconnect();

            JsonObject jsonObject = (JsonObject) new JsonParser().parse(builder.toString());
            JsonObject data = jsonObject.get("data").getAsJsonObject();
            String id = data.get("id").getAsString();
            boolean nulledId = id == null || id.equalsIgnoreCase("null");

            return !nulledId;
        } catch (IOException exception) {
            Bukkit.getLogger().severe("Could not fetch player! (Playername: " + playerName + "). Message: " + exception.getMessage());
            exception.printStackTrace();
        }
        return false;
    }
}
