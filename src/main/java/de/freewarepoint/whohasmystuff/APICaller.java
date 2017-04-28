package de.freewarepoint.whohasmystuff;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.freewarepoint.whohasmystuff.database.Item;

import static android.content.ContentValues.TAG;

/**
 * Created by Jake Uskoski on 2017-03-18.
 */

public class APICaller {

    private static String WEBSITE = "http://34.223.254.253:8080/";
    private Gson g = new Gson();

    private class AsyncPutRequest extends AsyncTask<String, Integer, String> {
        private String makePutRequest(String userId, String putString) {
            String str = "";

            try {
                URL url = new URL(WEBSITE + userId + "/items");

                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    try {
                        urlConnection.setDoOutput(true);
                        urlConnection.setRequestProperty("Content-Type", "application/json");
                        urlConnection.setRequestMethod("PUT");
                        urlConnection.connect();

                        OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                        writeStream(out, putString);

                        str = String.valueOf(urlConnection.getResponseCode());
                    } catch (IOException e) {

                    }
                } catch (IOException e) {

                }
            } catch (MalformedURLException e) {

            }

            return str;
        }

        protected String doInBackground(String... strings) {
            return makePutRequest(strings[0], strings[1]);
        }
    }

    private class AsyncDeleteRequest extends AsyncTask<String, Integer, String> {
        private String makeDeleteRequest(String userId, String deleteString) {
            String str = "";

            try {
                URL url = new URL(WEBSITE + userId + "/items");

                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    try {
                        urlConnection.setDoOutput(true);
                        urlConnection.setRequestMethod("DELETE");
                        urlConnection.connect();

                        OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                        writeStream(out, deleteString);

                        str = str.valueOf(urlConnection.getResponseCode());
                    } catch (IOException e) {

                    }
                } catch (IOException e) {

                }
            } catch (MalformedURLException e) {

            }

            return str;
        }

        protected String doInBackground(String... strings) {
            return makeDeleteRequest(strings[0], strings[1]);
        }
    }

    private class AsyncPostRequest extends AsyncTask<String, Integer, String> {
        private String makePostRequest(String userId, String postString) {
            String str = "";

            try {
                URL url = new URL(WEBSITE + userId + "/items");

                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    try {
                        urlConnection.setDoOutput(true);
                        urlConnection.setChunkedStreamingMode(0);
                        urlConnection.setRequestProperty("Content-Type", "application/json");
                        urlConnection.setInstanceFollowRedirects(false);

                        OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                        writeStream(out, postString);

                        InputStream in;
                        int responseCode = urlConnection.getResponseCode();
                        if (200 <= responseCode && responseCode <= 299) {
                            str = urlConnection.getHeaderField("Location");
                            if(str != null) {
                                str = str.substring(str.lastIndexOf("/") + 1);
                            } else {
                                str = "error";
                            }
                        } else {
                            in = new BufferedInputStream(urlConnection.getErrorStream());
                            str = readStream(in);
                        }


                    } catch (IOException e) {
                        Log.getStackTraceString(e);
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (IOException e) {

                }
            } catch (MalformedURLException e) {

            }

            return str;
        }

        protected String doInBackground(String... strings) {
            return makePostRequest(strings[0], strings[1]);
        }
    }

    private class AsyncGetRequest extends AsyncTask<String, Integer, String> {
        private String makeGetRequest(String userId) {
            String str = "";

            try {
                URL url = new URL(WEBSITE + userId + "/items");

                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();

                    try {
                        InputStream in;
                        int responseCode = urlConnection.getResponseCode();
                        if (200 <= responseCode && responseCode <= 299) {
                            in = new BufferedInputStream(urlConnection.getInputStream());
                            str = readStream(in);
                        } else {
                            in = new BufferedInputStream(urlConnection.getErrorStream());
                            Log.e(TAG, readStream(in));
                        }

                    } catch (IOException e) {

                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (IOException e) {

                }
            } catch (MalformedURLException e){

            }

            return str;
        }

        protected String doInBackground(String... strings) {
            return makeGetRequest(strings[0]);
        }
    }

    public String MakePutRequest(String userId, String putString) {
        String str = "";

        try {
            str = new AsyncPutRequest().execute(userId, putString).get();
        } catch (ExecutionException e) {
            Log.getStackTraceString(e);
        } catch (InterruptedException e) {
            Log.getStackTraceString(e);
        }

        return str;
    }

    public String MakeDeleteRequest(String userId, String deleteString) {
        String str = "";

        try {
            str = new AsyncDeleteRequest().execute(userId, deleteString).get();
        } catch (ExecutionException e) {
            Log.getStackTraceString(e);
        } catch (InterruptedException e) {
            Log.getStackTraceString(e);
        }

        return str;
    }

    public String MakePostRequest(String userId, String postString) {
        String str = "";

        try {
            str = new AsyncPostRequest().execute(userId, postString).get();
        } catch (ExecutionException e) {
            Log.getStackTraceString(e);
        } catch (InterruptedException e) {
            Log.getStackTraceString(e);
        }

        if(str == null) {
            str = "";
        }

        return str;
    }

    public String MakeGetRequest(String userId) {
        String str = "";

        try {
            str = new AsyncGetRequest().execute(userId).get();
        } catch (ExecutionException e) {
            Log.getStackTraceString(e);
        } catch (InterruptedException e) {
            Log.getStackTraceString(e);
        }

        return str;
    }

    public <T> String WriteJSON(T object) {
        return g.toJson(object);
    }

    public <T> T ReadJSON(String json, Class<T> clazz) {
        return g.fromJson(json, clazz);
    }

    public ArrayList<Item> ReadJSONArray(String json) {
        ArrayList<Item> list = g.fromJson(json, new TypeToken<ArrayList<Item>>(){}.getType());
        return list;
    }

    private static String readStream(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
            }
        }
        return sb.toString();
    }

    private static void writeStream(OutputStream out, String str) {
        try {
            out.write(str.getBytes());
            out.flush();
        } catch (IOException e) {

        }
    }
}