package com.poscoict.posledger.chain.assets.chaincode.extension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscoict.posledger.chain.assets.chaincode.util.ChaincodeCommunication;
import com.poscoict.posledger.chain.assets.chaincode.standard.ERC721;
import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;
import org.apache.logging.log4j.LogManager;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.*;

import static com.poscoict.posledger.chain.assets.chaincode.util.Function.*;

public class XNFT {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(XNFT.class);

    private static String chaincodeId = "assetscc";

    private ChaincodeProxy chaincodeProxy;

    private ObjectMapper objectMapper;

    @Autowired
    private ERC721 erc721;

    @Autowired
    private EERC721 eerc721;

    public XNFT() {}

    public XNFT(ChaincodeProxy chaincodeProxy) {
        this.chaincodeProxy = chaincodeProxy;
    }

    public XNFT(ChaincodeProxy chaincodeProxy, ObjectMapper objectMapper) {
        this.chaincodeProxy = chaincodeProxy;
        this.objectMapper = objectMapper;
    }

    private String caller;

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public boolean mint(BigInteger tokenId, String type, String owner, Map<String, Object> xattr, Map<String, String> uri) throws ProposalException, InvalidArgumentException, JsonProcessingException {
        logger.info("---------------- mint SDK called ----------------");

        boolean result;
        try {
            if (!caller.equals(owner)) {
                return false;
            }

            String xattrJson = objectMapper.writeValueAsString(xattr);
            String uriJson = objectMapper.writeValueAsString(uri);
            String[] args = { tokenId.toString(), type, owner, xattrJson, uriJson };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, MINT_FUNCTION_NAME, chaincodeId, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean setURI(BigInteger tokenId, String index, String value) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- setURI SDK called ----------------");


        boolean result;
        try {
            String owner = erc721.ownerOf(tokenId);
            if(!(caller.equals(owner) || erc721.isApprovedForAll(owner, caller))) {
                return false;
            }

            String[] args = { tokenId.toString(), index, value };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, SET_URI_FUNCTION_NAME, chaincodeId, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public String getURI(BigInteger tokenId, String index) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- getURI SDK called ----------------");

        String value;
        try {
            String[] args = { tokenId.toString(), index };
            value = ChaincodeCommunication.readFromChaincode(chaincodeProxy, GET_URI_FUNCTION_NAME, chaincodeId, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return value;
    }

    public boolean setXAttr(BigInteger tokenId, String index, Object value) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- setXAttr SDK called ----------------");

        boolean result;
        try {
            String owner = erc721.ownerOf(tokenId);
            if(!(caller.equals(owner) || erc721.isApprovedForAll(owner, caller))) {
                return false;
            }

            String[] args = { tokenId.toString(), index, String.valueOf(value) };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, SET_XATTR_FUNCTION_NAME, chaincodeId, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public String getXAttr(BigInteger tokenId, String index) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- getXAttr SDK called ----------------");

        String value = null;
        try {
            String[] args = { tokenId.toString(), index };
            value = ChaincodeCommunication.readFromChaincode(chaincodeProxy, GET_XATTR_FUNCTION_NAME, chaincodeId, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return value;
    }
}
