package com.poscoict.posledger.chain.assets.chaincode.extension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscoict.posledger.chain.assets.chaincode.util.ChaincodeCommunication;
import com.poscoict.posledger.chain.assets.chaincode.standard.ERC721;
import com.poscoict.posledger.chain.assets.chaincode.util.Manager;
import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import java.util.*;

import static com.poscoict.posledger.chain.assets.chaincode.util.Function.*;

public class XNFT extends ERC721 {

    private static final Logger logger = LogManager.getLogger(XNFT.class);

    private ChaincodeProxy chaincodeProxy;

    private ObjectMapper objectMapper;

    public XNFT() {
        super();
    }

    public XNFT(ChaincodeProxy chaincodeProxy) {
        super(chaincodeProxy);
        this.chaincodeProxy = super.getChaincodeProxy();
    }

    public XNFT(ChaincodeProxy chaincodeProxy, ObjectMapper objectMapper) {
        super(chaincodeProxy, objectMapper);
        this.chaincodeProxy = super.getChaincodeProxy();
        this.objectMapper = super.getObjectMapper();
    }

    public boolean mint(String tokenId, String type, String owner, Map<String, Object> xattr, Map<String, String> uri) throws ProposalException, InvalidArgumentException, JsonProcessingException {
        logger.info("---------------- mint SDK called ----------------");

        String caller = Manager.getCaller();
        boolean result;
        try {
            if (!caller.equals(owner)) {
                return false;
            }

            String xattrJson = objectMapper.writeValueAsString(xattr);
            String uriJson = objectMapper.writeValueAsString(uri);
            String[] args = { tokenId, type, owner, xattrJson, uriJson };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, MINT_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean setURI(String tokenId, String index, String value) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- setURI SDK called ----------------");

        boolean result;
        try {
            String[] args = { tokenId, index, value };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, SET_URI_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public String getURI(String tokenId, String index) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- getURI SDK called ----------------");

        String value;
        try {
            String[] args = { tokenId, index };
            value = ChaincodeCommunication.readFromChaincode(chaincodeProxy, GET_URI_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return value;
    }

    public boolean setXAttr(String tokenId, String index, Object value) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- setXAttr SDK called ----------------");

        boolean result;
        try {
            String[] args = { tokenId, index, String.valueOf(value) };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, SET_XATTR_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public String getXAttr(String tokenId, String index) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- getXAttr SDK called ----------------");

        String value;
        try {
            String[] args = { tokenId, index };
            value = ChaincodeCommunication.readFromChaincode(chaincodeProxy, GET_XATTR_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return value;
    }
}
