package com.penelope.seatforyou.api.address;

import androidx.annotation.WorkerThread;

import com.penelope.seatforyou.data.address.Address;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class AddressApi {

    private static final String URL_FORMAT = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?X-NCP-APIGW-API-KEY-ID={API_KEY_ID}&X-NCP-APIGW-API-KEY={API_KEY}&output=json&query={QUERY}";
    private static final String ARG_API_KEY_ID = "{API_KEY_ID}";
    private static final String ARG_API_KEY = "{API_KEY}";
    private static final String ARG_QUERY = "{QUERY}";
    private static final String API_KEY_ID = "zswi02dkah";
    private static final String API_KEY = "fPUGgAzkLygXDfwxZKKkNaFz2umWxwM9xvnC4IDR";


    @WorkerThread
    public static List<Address> get(String query) {

        if (query.isEmpty()) {
            return null;
        }

        try {
            String strUrl = URL_FORMAT
                    .replace(ARG_API_KEY_ID, API_KEY_ID)
                    .replace(ARG_API_KEY, API_KEY)
                    .replace(ARG_QUERY, query);

            URL url = new URL(strUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            JSONObject response = new JSONObject(sb.toString());
            return AddressJsonParser.parse(response);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
