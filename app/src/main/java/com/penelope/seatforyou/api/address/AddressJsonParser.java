package com.penelope.seatforyou.api.address;

import com.penelope.seatforyou.data.address.Address;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddressJsonParser {

    public static List<Address> parse(JSONObject response) {

        List<Address> addressList = new ArrayList<>();

        try {

            JSONObject meta = response.getJSONObject("meta");
            if (meta.getInt("totalCount") == 0) {
                return addressList;
            }

            JSONArray documents = response.getJSONArray("addresses");

            for (int i = 0; i < documents.length(); i++) {

                JSONObject document = documents.getJSONObject(i);
                if (!document.has("roadAddress") ||
                        document.getString("roadAddress").isEmpty()) {
                    continue;
                }
                String addressName = document.getString("roadAddress");
                String strLatitude = document.getString("y");
                String strLongitude = document.getString("x");

                double latitude;
                double longitude;

                try {
                    latitude = Double.parseDouble(strLatitude);
                    longitude = Double.parseDouble(strLongitude);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    continue;
                }

                Address address = new Address(addressName, "", latitude, longitude);
                addressList.add(address);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return addressList;
    }

}
