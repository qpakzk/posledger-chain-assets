package com.poscoict.posledger.chain.assets.chaincode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;
import com.poscoict.posledger.chain.model.ChaincodeRequest;
import org.apache.logging.log4j.LogManager;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.*;

import static sun.java2d.cmm.ColorTransform.In;

public class XNFT {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(XNFT.class);

    private static String chaincodeId = "assetscc";

    private static final String MINT_FUNCTION_NAME = "mint";

    private static final String SET_URI_FUNCTION_NAME = "setURI";

    private static final String GET_URI_FUNCTION_NAME = "getURI";

    private static final String SET_XATTR_FUNCTION_NAME = "setXAttr";

    private static final String GET_XATTR_FUNCTION_NAME = "getXAttr";

    private static final String SUCCESS = "SUCCESS";

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

        String status = null;
        boolean result = false;

        try {
            if (!caller.equals(owner)) {
                return false;
            }

            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(MINT_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);

            String xattrJson = objectMapper.writeValueAsString(xattr);
            String uriJson = objectMapper.writeValueAsString(uri);
            chaincodeRequest.setArgs(new String[] { tokenId.toString(), type, owner, xattrJson, uriJson });
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

    public boolean setURI(BigInteger tokenId, String index, String value) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- setURI SDK called ----------------");

        String status = null;
        boolean result = false;

        try {
            String owner = erc721.ownerOf(tokenId);
            if(!(caller.equals(owner) || erc721.isApprovedForAll(owner, caller))) {
                return false;
            }

            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(SET_URI_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);

            chaincodeRequest.setArgs(new String[] { tokenId.toString(), index, value });
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

    public String getURI(BigInteger tokenId, String index) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- getURI SDK called ----------------");

        String value = null;

        try {
            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(GET_URI_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);
            chaincodeRequest.setArgs(new String[] { tokenId.toString(), index });

            Collection<ProposalResponse> responses = chaincodeProxy.queryByChainCode(chaincodeRequest);

            for (ProposalResponse response : responses) {
                if (response.getChaincodeActionResponsePayload() != null) {
                    value =  response.getMessage();
                }
            }

        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }

        logger.info("getURI {}", value);
        return value;
    }

    public boolean setXAttr(BigInteger tokenId, String index, Object value) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- setXAttr SDK called ----------------");

        String status = null;
        boolean result = false;

        try {
            String owner = erc721.ownerOf(tokenId);
            if(!(caller.equals(owner) || erc721.isApprovedForAll(owner, caller))) {
                return false;
            }

            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(SET_XATTR_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);

            chaincodeRequest.setArgs(new String[] { tokenId.toString(), index, String.valueOf(value) });
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

    public String getXAttr(BigInteger tokenId, String index) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- getXAttr SDK called ----------------");

        String value = null;

        try {
            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(GET_XATTR_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);
            chaincodeRequest.setArgs(new String[] { tokenId.toString(), index });

            Collection<ProposalResponse> responses = chaincodeProxy.queryByChainCode(chaincodeRequest);

            for (ProposalResponse response : responses) {
                if (response.getChaincodeActionResponsePayload() != null) {
                    value =  response.getMessage();
                }
            }

        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }

        logger.info("getXAttr {}", value);
        return value;
    }
}
