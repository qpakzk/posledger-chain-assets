package com.poscoict.posledger.chain.assets.chaincode.util;

import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;
import com.poscoict.posledger.chain.model.ChaincodeRequest;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import java.util.Collection;

public class ChaincodeCommunication {
    private static final String SUCCESS_STATUS = "SUCCESS";

    private static String chaincodeId = Manager.getChaincodeId();

    private ChaincodeCommunication() {}

    public static boolean writeToChaincode(ChaincodeProxy chaincodeProxy, String function, String[] args) throws InvalidArgumentException, ProposalException {
        boolean result = false;

        ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
        chaincodeRequest.setFunctionName(function);
        chaincodeRequest.setChaincodeName(chaincodeId);

        chaincodeRequest.setArgs(args);
        Collection<ProposalResponse> responses = chaincodeProxy.sendTransaction(chaincodeRequest);

        for (ProposalResponse response : responses) {
            if (response.getChaincodeActionResponsePayload() != null) {
                result =  Boolean.parseBoolean(response.getMessage());
            }
        }

        return result;
    }

    public static String readFromChaincode(ChaincodeProxy chaincodeProxy, String function, String[] args) throws InvalidArgumentException, ProposalException {
        ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
        chaincodeRequest.setFunctionName(function);
        chaincodeRequest.setChaincodeName(chaincodeId);
        chaincodeRequest.setArgs(args);

        Collection<ProposalResponse> responses = chaincodeProxy.queryByChainCode(chaincodeRequest);

        String result = null;
        for (ProposalResponse response : responses) {
            if (response.getChaincodeActionResponsePayload() != null) {
                result =  response.getMessage();
            }
        }

        return result;
    }
}
