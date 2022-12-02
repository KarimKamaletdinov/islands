package com.agrogames.islandsofwar.activity;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javax.xml.transform.stream.StreamResult;

public class IslandsOfWar extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            ex.printStackTrace();
            Log.e("IOW-ERROR", ex.getMessage());/*
            SendMessage sendMessage = new SendMessage();
            sendMessage.execute(ex);
            try {
                sendMessage.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
*/
            Send send = new Send();
            send.doInBackground(ex);
        });
    }

    public static class Send extends AsyncTask<Throwable, Void, Void>{

        public Send(){
            super();
        }
        @Override
        protected Void doInBackground(Throwable... exceptions) {
            TelegramBot bot = new TelegramBot("5833067017:AAEn3HzoeeZsALPv0PLn8t94PujdHxgoDrk");
            long chatId = 933854525;
            SendResponse response = bot.execute(new SendMessage(chatId, "Hello!"));
            response.message();
            return null;
        }
    }
}
