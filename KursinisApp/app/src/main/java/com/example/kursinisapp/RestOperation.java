package com.example.kursinisapp;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestOperation {

    public static String sendPost(String urlPost, String postDataParams) throws IOException {
        URL url = new  URL(urlPost);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setConnectTimeout(15000);
        httpURLConnection.setReadTimeout(15000);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);

        OutputStream outputStream = httpURLConnection.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        bufferedWriter.write(postDataParams);

        outputStream.close();

        int code = httpURLConnection.getResponseCode();
        System.out.println("Response Code : " + code);

        if(code == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
            String line;
            StringBuffer response = new StringBuffer();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            in.close();
            return response.toString();
        } else{
            return "HTTP ERROR";
        }
    }
}
