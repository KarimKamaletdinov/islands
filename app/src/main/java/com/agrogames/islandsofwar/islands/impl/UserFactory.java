package com.agrogames.islandsofwar.islands.impl;

import com.agrogames.islandsofwar.islands.abs.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserFactory {
    public static User parse(String text){
        try {
            JSONObject json = new JSONObject(text);
            JSONObject ship = json.getJSONObject("ship");
            JSONArray shipContentJson = ship.getJSONArray("content");
            String[] shipContent = new String[shipContentJson.length()];
            for (int i = 0; i < shipContent.length; i++){
                shipContent[i] = shipContentJson.getString(i);
            }
            return new User(
                    json.getString("name"),
                    ship.getString("name"),
                    shipContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new User(
                "ERROR",
                "ERROR",
                new String[0]);
    }
}
