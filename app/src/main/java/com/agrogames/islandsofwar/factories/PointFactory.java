package com.agrogames.islandsofwar.factories;

import com.agrogames.islandsofwar.engine.abs.common.Point;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PointFactory {
    public static Point[] points(JSONArray json) throws JSONException {
        Point[] result = new Point[json.length()];
        for (int i = 0; i < json.length(); i++){
            result[i] = point(json.getJSONObject(i));
        }
        return result;
    }

    public static Point point(JSONObject json) throws JSONException{
        return new Point((float)json.getDouble("x"), (float) json.getDouble("y"));
    }
}
