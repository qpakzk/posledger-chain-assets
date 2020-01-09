package com.poscoict.posledger.chain.assets.chaincode.extension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscoict.posledger.chain.assets.chaincode.SDK;
import com.poscoict.posledger.chain.assets.chaincode.util.ChaincodeCommunication;
import com.poscoict.posledger.chain.assets.chaincode.util.Manager;
import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.io.IOException;
import java.util.*;

import static com.poscoict.posledger.chain.assets.chaincode.util.Function.*;

public class XType extends SDK {
    private static final Logger logger = LogManager.getLogger(XType.class);

    private ChaincodeProxy chaincodeProxy;

    private ObjectMapper objectMapper;

    public XType() {
        super();
    }

    public XType(ChaincodeProxy chaincodeProxy) {
        super(chaincodeProxy);
        this.chaincodeProxy = super.getChaincodeProxy();
    }

    public XType(ChaincodeProxy chaincodeProxy, ObjectMapper objectMapper) {
        super(chaincodeProxy, objectMapper);
        this.chaincodeProxy = super.getChaincodeProxy();
        this.objectMapper = super.getObjectMapper();
    }

    public boolean enrollTokenType(String admin, String type, Map<String, List<String>> xattr) throws ProposalException, InvalidArgumentException, JsonProcessingException {
        logger.info("---------------- enrollTokenType SDK called ----------------");

        String caller = Manager.getCaller();
        boolean result;
        try {
            if (!caller.equals(admin)) {
                return false;
            }

            String json = objectMapper.writeValueAsString(xattr);
            String[] args = { type, json };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, ENROLL_TOKEN_TYPE_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean dropTokenType(String admin, String type) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- dropTokenType SDK called ----------------");

        String caller = Manager.getCaller();
        boolean result;
        try {
            if (!caller.equals(admin)) {
                return false;
            }

            String[] args = { type };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, DROP_TOKEN_TYPE_FUNCTION_NAME, args);
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
            tokenTypsString = ChaincodeCommunication.readFromChaincode(chaincodeProxy, TOKEN_TYPES_OF_FUNCTION_NAME, args);
            if (tokenTypsString != null) {
                tokenTypes = Arrays.asList(tokenTypsString.substring(1, tokenTypsString.length() - 1).split(", "));
            }
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return tokenTypes;
    }

    public boolean updateTokenType(String admin, String type, Map<String, List<String>> attributes) throws ProposalException, InvalidArgumentException, JsonProcessingException {
        logger.info("---------------- updateTokenType SDK called ----------------");

        String caller = Manager.getCaller();
        boolean result;
        try {
            if (!caller.equals(admin)) {
                return false;
            }

            String json = objectMapper.writeValueAsString(attributes);
            String[] args = { type, json };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, UPDATE_TOKEN_TYPE_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public Map<String, List<String>> retrieveTokenType(String type) throws ProposalException, InvalidArgumentException, IOException {
        logger.info("---------------- getTokenType SDK called ----------------");

        String json;
        Map<String, List<String>> xattr = new HashMap<String, List<String>>();
        try {
            String[] args = { type };
            json = ChaincodeCommunication.readFromChaincode(chaincodeProxy, RETRIEVE_TOKEN_TYPE_FUNCTION_NAME, args);
            if (json != null) {
                xattr = objectMapper.readValue(json, new TypeReference<Map<String, List<String>>>() {});
            }
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return xattr;
    }

    public boolean enrollAttributeOfTokenType(String admin, String type, String attribute, String dataType, String initialValue) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- enrollAttributeOfTokenType SDK called ----------------");

        String caller = Manager.getCaller();
        boolean result;
        try {
            if (!caller.equals(admin)) {
                return false;
            }

            String[] args = { type, attribute, dataType, initialValue };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, EMROLL_ATTRIBUTE_OF_TOKEN_TYPE_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean dropAttributeOfTokenType(String admin, String type, String attribute) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- dropAttributeOfTokenType SDK called ----------------");

        String caller = Manager.getCaller();
        boolean result;
        try {
            if (!caller.equals(admin)) {
                return false;
            }

            String[] args = { type, attribute };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, DROP_ATTRIBUTE_OF_TOKEN_TYPE_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean updateAttributeOfTokenType(String admin, String type, String attribute, List<String> pair) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- updateAttributeOfTokenType SDK called ----------------");

        String caller = Manager.getCaller();
        boolean result;
        try {
            if (!caller.equals(admin)) {
                return false;
            }

            String[] args = { type, attribute, pair.toString() };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, UPDATE_ATTRIBUTE_OF_TOKEN_TYPE_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public List<String> retrieveAttributeOfTokenType(String type, String attribute) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- retrieveAttributeOfTokenType SDK called ----------------");

        String string;
        List<String> pair = new ArrayList<String>();
        try {
            String[] args = { type, attribute };
            string = ChaincodeCommunication.readFromChaincode(chaincodeProxy, RETRIEVE_ATTRIBUTE_OF_TOKEN_TYPE_FUNCTION_NAME, args);
            if (string != null) {
                pair = Arrays.asList(string.substring(1, string.length() - 1).split(", "));
            }
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return pair;
    }
}
