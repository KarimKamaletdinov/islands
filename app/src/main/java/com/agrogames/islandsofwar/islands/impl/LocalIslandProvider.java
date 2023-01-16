package com.agrogames.islandsofwar.islands.impl;

import android.content.Context;

import androidx.annotation.NonNull;

import com.agrogames.islandsofwar.islands.abs.Island;
import com.agrogames.islandsofwar.islands.abs.IslandProvider;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
            List<Island> islands = new ArrayList<>();
            for (String file : islandFiles) {
                if (file.startsWith(prefix)) {
                    islands.add(IslandFactory.parse(context, Integer.parseInt(file.substring(1, file.length() - 5)),
                            new Scanner(context.getAssets().open("islands/" + file)).useDelimiter("\\A").next()));
                }
            }
            return islands.toArray(new Island[0]);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new Island[0];
    }

    @Override
    public Island getMyById(int id) {
        try {
            return IslandFactory.parse(context, id,
                    new Scanner(context.getAssets().open("islands/m" + id + ".json")).useDelimiter("\\A").next());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return new Island(id, null, null, null);
    }

    @Override
    public Island getAttackableById(int id) {
        try {
            return IslandFactory.parse(context, id,
                    new Scanner(context.getAssets().open("islands/e" + id + ".json")).useDelimiter("\\A").next());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return new Island(id, null, null, null);
    }

}
