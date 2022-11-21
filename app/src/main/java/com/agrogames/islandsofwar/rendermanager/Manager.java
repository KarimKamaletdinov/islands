package com.agrogames.islandsofwar.rendermanager;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.Engine;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.impl.EngineFactory;
import com.agrogames.islandsofwar.factories.Factory;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;
import com.agrogames.islandsofwar.graphics.abs.RenderManager;
import com.agrogames.islandsofwar.islands.impl.LocalIslandProvider;
import com.agrogames.islandsofwar.islands.impl.LocalUserProvider;
import com.agrogames.islandsofwar.render.abs.Renderer;
import com.agrogames.islandsofwar.sounds.impl.SoundPlayerImpl;
import com.agrogames.islandsofwar.ui.impl.UI;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class Manager implements RenderManager {
    private final Engine engine;
    private final Renderer renderer;
    private final SoundPlayerImpl soundPlayer;
    private static Timer timer;
    private LocalTime previous;
    private boolean start = false;
    private Point touch;
    private Point move;
    private Point previousMove;
    private Point zoom1;
    private Point previousZoom1;
    private Point zoom2;
    private Point previousZoom2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Manager(Context context, int islandId, boolean runEngine, Callable<Void> right,
                   Callable<Void> left, Callable<Void> attack, Callable<Void> back) {
        Factory.load(context);

        soundPlayer = new SoundPlayerImpl(context);
        engine = new EngineFactory(new LocalIslandProvider(context), new LocalUserProvider(context))
                .create(islandId, soundPlayer);
        if(runEngine){
            new Thread(() -> {
                while (true){
                    if(start){
                        LocalTime now = LocalTime.now();
                        if (previous == null) {
                            previous = now;
                        }
                        float deltaTime = ((float)previous.until(now, ChronoUnit.MICROS)) / 1000000f;
                        //if(deltaTime > 0.1f) deltaTime = 0.1f;
                        previous = now;
                        engine.update(deltaTime);
                    }
                }
            }).start();
        }
        if(timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                soundPlayer.playSound("music");
            }
        }, 3000, 10000);
        this.renderer = new com.agrogames.islandsofwar.render.impl.Renderer(new Presenter(engine),
                new UI(), runEngine, right, left, attack, back);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void render(TextureDrawer drawer) {
        start = true;
        renderer.render(drawer, soundPlayer, touch, move, previousMove, zoom1, zoom2, previousZoom1, previousZoom2);
        touch = null;
        previousMove = move;
        previousZoom1 = zoom1;
        previousZoom2 = zoom2;
        move = null;
        zoom1 = null;
        zoom2 = null;
    }

    @Override
    public void onTouch(float x, float y) {
        touch = new Point(x, y);
    }

    @Override
    public void onMove(float x, float y) {
        move = new Point(x, y);
    }

    @Override
    public void onZoom(float x1, float y1, float x2, float y2) {
        zoom1 = new Point(x1, y1);
        zoom2 = new Point(x2, y2);
    }

    @Override
    public void stop() {
        soundPlayer.disable();
    }
}
