package com.penelope.seatforyou.api.address;

import com.penelope.seatforyou.data.address.Address;

import junit.framework.TestCase;

import java.util.List;

public class AddressApiTest extends TestCase {

    public void testGet() {

        List<Address> addressList = AddressApi.get("완월동 8길");

        System.out.println(addressList != null ? addressList.toString() : null);

    }
}