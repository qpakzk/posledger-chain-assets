package com.poscoict.posledger.chain.assets.chaincode.util;

public class Manager {
    private static String caller;

    private static String chaincodeId;

    private Manager() {}

    public static void setCaller(String caller) {
        Manager.caller = caller;
    }
    public static String getCaller() {
        return caller;
    }

    public static void setChaincodeId(String chaincodeId) {
        Manager.chaincodeId = chaincodeId;
    }

    public static String getChaincodeId() {
        return chaincodeId;
    }
}
