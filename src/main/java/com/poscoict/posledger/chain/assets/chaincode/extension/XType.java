package com.poscoict.posledger.chain.assets.chaincode.extension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;
import com.poscoict.posledger.chain.model.ChaincodeRequest;
import org.apache.logging.log4j.LogManager;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.io.IOException;
import java.util.*;

import static com.poscoict.posledger.chain.assets.chaincode.constant.Function.*;

public class XType {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(XType.class);

    private static String chaincodeId = "assetscc";

    private static final String SUCCESS = "SUCCESS";

    private ChaincodeProxy chaincodeProxy;

    private ObjectMapper objectMapper;

    public XType() {}

    public XType(ChaincodeProxy chaincodeProxy) {
        this.chaincodeProxy = chaincodeProxy;
    }

    public XType(ChaincodeProxy chaincodeProxy, ObjectMapper objectMapper) {
        this.chaincodeProxy = chaincodeProxy;
        this.objectMapper = objectMapper;
    }

    private String caller;

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public boolean registerTokenType(String admin, String type, Map<String, List<String>> xattr) throws ProposalException, InvalidArgumentException, JsonProcessingException {
        logger.info("---------------- registerTokenType SDK called ----------------");

        String status = null;
        boolean result = false;

        try {
            if (!caller.equals(admin)) {
                return false;
            }
            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(REGISTER_TOKEN_TYPE_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);

            String json = objectMapper.writeValueAsString(xattr);
            chaincodeRequest.setArgs(new String[] { type, json });
            Collection<ProposalResponse> responses = chaincodeProxy.sendTransaction(chaincodeRequest);

            for (ProposalResponse response : responses) {
                if (response.getChaincodeActionResponsePayload() != null) {
                    status = response.getStatus().toString();
                }
            }

            if (status != null && status.equals(SUCCESS)) {
                result = true;
            }

        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }

        return result;
    }


    public List<String> tokenTypesOf() throws ProposalException, InvalidArgumentException {
        logger.info("---------------- tokenTypesOf SDK called ----------------");

        String tokenTypsString = null;

        try {
            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(TOKEN_TYPES_OF_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);
            chaincodeRequest.setArgs(new String[] { });

            Collection<ProposalResponse> responses = chaincodeProxy.queryByChainCode(chaincodeRequest);

            for (ProposalResponse response : responses) {
                if (response.getChaincodeActionResponsePayload() != null) {
                    tokenTypsString =  response.getMessage();
                }
            }

        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }

        if (tokenTypsString == null) {
            return new ArrayList<String>();
        }

        List<String> tokenTypes = Arrays.asList(tokenTypsString.substring(1, tokenTypsString.length() - 1).trim().split(","));
        logger.info("tokenTypesOf {}", tokenTypes);
        return tokenTypes;
    }

    public Map<String, List<String>> getTokenType(String type) throws ProposalException, InvalidArgumentException, IOException {
        logger.info("---------------- getTokenType SDK called ----------------");

        String json = null;

        try {
            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(GET_TOKEN_TYPE_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);
            chaincodeRequest.setArgs(new String[] { type });

            Collection<ProposalResponse> responses = chaincodeProxy.queryByChainCode(chaincodeRequest);

            for (ProposalResponse response : responses) {
                if (response.getChaincodeActionResponsePayload() != null) {
                    json =  response.getMessage();
                }
            }

        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }

        Map<String, List<String>> xattr = objectMapper.readValue(json, new TypeReference<Map<String, List<String>>>() {});
        logger.info("getTokenType {}", json);
        return xattr;
    }
}
