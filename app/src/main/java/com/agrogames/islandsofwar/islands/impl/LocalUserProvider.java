package com.agrogames.islandsofwar.islands.impl;

import android.content.Context;

import com.agrogames.islandsofwar.islands.abs.User;
import com.agrogames.islandsofwar.islands.abs.UserProvider;

import java.io.IOException;
import java.util.Scanner;

public class LocalUserProvider implements UserProvider {
    private final Context context;

    public LocalUserProvider(Context context) {
        this.context = context;
    }

    @Override
    public User get() {
        try {
            return UserFactory.parse(new Scanner(context.getAssets().open("islands/user.json")).useDelimiter("\\A").next());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new User(
                "ERROR",
                "ERROR",
                new String[0]);
    }
}
