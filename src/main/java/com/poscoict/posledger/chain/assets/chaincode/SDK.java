package com.poscoict.posledger.chain.assets.chaincode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;

public class SDK {
    private ChaincodeProxy chaincodeProxy;
    private ObjectMapper objectMapper;

    public SDK() {}

    public SDK(ChaincodeProxy chaincodeProxy) {
        this.chaincodeProxy = chaincodeProxy;
    }

    public SDK(ChaincodeProxy chaincodeProxy, ObjectMapper objectMapper) {
        this.chaincodeProxy = chaincodeProxy;
        this.objectMapper = objectMapper;
    }

    protected ChaincodeProxy getChaincodeProxy() {
        return chaincodeProxy;
    }

    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
