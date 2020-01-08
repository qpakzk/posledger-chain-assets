package com.poscoict.posledger.chain.assets.chaincode.extension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscoict.posledger.chain.assets.chaincode.util.ChaincodeCommunication;
import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;
import org.apache.logging.log4j.LogManager;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.io.IOException;
import java.util.*;

import static com.poscoict.posledger.chain.assets.chaincode.util.Function.*;

public class XType {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(XType.class);

    private static String chaincodeId = "assetscc";

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

        boolean result;
        try {
            if (!caller.equals(admin)) {
                return false;
            }

            String json = objectMapper.writeValueAsString(xattr);
            String[] args = { type, json };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, REGISTER_TOKEN_TYPE_FUNCTION_NAME, chaincodeId, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }


    public List<String> tokenTypesOf() throws ProposalException, InvalidArgumentException {
        logger.info("---------------- tokenTypesOf SDK called ----------------");

        String tokenTypsString;
        List<String> tokenTypes = new ArrayList<String>();
        try {
            String[] args = {};
            tokenTypsString = ChaincodeCommunication.readFromChaincode(chaincodeProxy, TOKEN_TYPES_OF_FUNCTION_NAME, chaincodeId, args);
            if (tokenTypsString != null) {
                tokenTypes = Arrays.asList(tokenTypsString.substring(1, tokenTypsString.length() - 1).trim().split(","));
            }
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return tokenTypes;
    }

    public Map<String, List<String>> getTokenType(String type) throws ProposalException, InvalidArgumentException, IOException {
        logger.info("---------------- getTokenType SDK called ----------------");

        String json;
        Map<String, List<String>> xattr = new HashMap<String, List<String>>();
        try {
            String[] args = { type };
            json = ChaincodeCommunication.readFromChaincode(chaincodeProxy, GET_TOKEN_TYPE_FUNCTION_NAME, chaincodeId, args);
            if (json != null) {
                xattr = objectMapper.readValue(json, new TypeReference<Map<String, List<String>>>() {});
            }
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return xattr;
    }
}
