package com.toggle.katana2d;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.toggle.katana2d.physics.PhysicsSystem;
import com.toggle.katana2d.physics.PhysicsUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Utilities {
    public static int getResourceId(Context context, String resourceType, String resourceName)
    {
        try {
            return context.getResources().getIdentifier(resourceName, resourceType,
                    context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Read text from a raw resource file
    public static String getRawFileText(Context context, int rawResId) {
        InputStream inputStream  = context.getResources().openRawResource(rawResId);
        String s = new java.util.Scanner(inputStream).useDelimiter("\\A").next();

        try {
            inputStream.close();
        } catch (IOException e) {
            Log.e("Raw File read Error", e.getMessage());
        }
        return s;
    }

    public static Vector2 getCenter(Vector2 p0, Vector2 p1) {
        return new Vector2((p0.x+p1.x)/2, (p0.y+p1.y)/2);
    }

    public static List<Vector2> getPoints(List<Vector2> path, float length, boolean meters) {
        float factor = 1;
        if (meters)
            factor = PhysicsSystem.METERS_PER_PIXEL;

        length *= factor;
        List<Vector2> result = new ArrayList<>();
        result.add(path.get(0).scl(factor));

        for (int i=0; i<path.size()-1; ++i) {
            Vector2 p0 = result.get(result.size() - 1);
            Vector2 p1 = path.get(i+1).scl(factor);

            float dx = p1.x - p0.x;
            float dy = p1.y - p0.y;

            float d = (float)Math.sqrt(dx * dx + dy * dy);
            int np = (int)Math.floor(d / length);

            float stepX = dx / np;
            float stepY = dy / np;

            float x = p0.x;
            float y = p0.y;
            for (int j=0; j<np; ++j) {
                x += stepX;
                y += stepY;
                result.add(new Vector2(x, y));
            }
        }

        return result;
    }

}
