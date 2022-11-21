package com.agrogames.islandsofwar.islands.impl;

import android.content.Context;

import androidx.annotation.NonNull;

import com.agrogames.islandsofwar.islands.abs.Island;
import com.agrogames.islandsofwar.islands.abs.IslandProvider;
import com.agrogames.islandsofwar.map.impl.Map;

import org.json.JSONException;

import java.io.IOException;
import java.util.Scanner;

public class LocalIslandProvider implements IslandProvider {
    private final Context context;

    public LocalIslandProvider(Context context) {
        this.context = context;
    }

    @Override
    public Island[] getMy() {
        return getIslands("m");
    }

    @Override
    public Island[] getAttackable() {
        return getIslands("e");
    }
    @NonNull
    private Island[] getIslands(String prefix) {
        try {
            String[] islandFiles = context.getAssets().list("islands");
            Island[] islands = new Island[islandFiles.length];
            for (int i = 0, islandFilesLength = islandFiles.length; i < islandFilesLength; i++) {
                String file = islandFiles[i];
                if (file.startsWith(prefix)) {
                    islands[i] = IslandFactory.parse(context,
                            new Scanner(context.getAssets().open("islands/" + file)).useDelimiter("\\A").next());
                }
            }
            return islands;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new Island[0];
    }

    @Override
    public Island getMyById(int id) {
        try {
            return IslandFactory.parse(context,
                    new Scanner(context.getAssets().open("islands/m" + id + ".json")).useDelimiter("\\A").next());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return new Island(null, null);
    }

    @Override
    public Island getAttackableById(int id) {
        try {
            return IslandFactory.parse(context,
                    new Scanner(context.getAssets().open("islands/e" + id + ".json")).useDelimiter("\\A").next());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return new Island(null, null);
    }

}
