package com.poscoict.posledger.chain.assets.chaincode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;
import com.poscoict.posledger.chain.model.ChaincodeRequest;
import org.hyperledger.fabric.sdk.ProposalResponse;
import java.math.BigInteger;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;

public class EERC721 {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(EERC721.class);

    private static String chaincodeId = "assetscc";

    private static final String BALANCE_OF_FUNCTION_NAME = "balanceOf";

    private static final String TOKEN_IDS_OF_FUNCTION_NAME = "tokenIdsOf";

    private static final String DIVIDE_FUNCTION_NAME = "divide";

    private static final String UPDATE_FUNCTION_NAME = "setXAttr";

    private static final String DEACTIVATE_FUNCTION_NAME = "deactivate";

    private static final String QUERY_FUNCTION_NAME = "query";

    private static final String QUERY_HISTORY_FUNCTION_NAME = "queryHistory";

    private static final String SUCCESS = "SUCCESS";

    private ChaincodeProxy chaincodeProxy;

    private ObjectMapper objectMapper;

    @Autowired
    private ERC721 erc721;

    public EERC721() {}

    public EERC721(ChaincodeProxy chaincodeProxy) {
        this.chaincodeProxy = chaincodeProxy;
    }

    public EERC721(ChaincodeProxy chaincodeProxy, ObjectMapper objectMapper) {
        this.chaincodeProxy = chaincodeProxy;
        this.objectMapper = objectMapper;
    }

    private String caller;

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public BigInteger balanceOf(String owner, String type) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- balanceOf SDK called ----------------");

        BigInteger balanceBigInt = null;

        try {
            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(BALANCE_OF_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);
            chaincodeRequest.setArgs(new String[] { owner, type });

            Collection<ProposalResponse> responses = chaincodeProxy.queryByChainCode(chaincodeRequest);

            for (ProposalResponse response : responses) {
                if (response.getChaincodeActionResponsePayload() != null) {
                    String balance =  response.getMessage();
                    int balanceInt = Integer.parseInt(balance);
                    balanceBigInt = BigInteger.valueOf(balanceInt);
                }
            }

        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }

        logger.info("balance {}", balanceBigInt);
        return balanceBigInt;
    }

    public List<BigInteger> tokenIdsOf(String owner) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- tokenIdsOf SDK called ----------------");

        List<BigInteger> tokenIds = new ArrayList<BigInteger>();
        String result = null;

        try {
            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(TOKEN_IDS_OF_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);
            chaincodeRequest.setArgs(new String[] { owner });

            Collection<ProposalResponse> responses = chaincodeProxy.queryByChainCode(chaincodeRequest);

            for (ProposalResponse response : responses) {
                if (response.getChaincodeActionResponsePayload() != null) {
                    result =  response.getMessage();
                }
            }

            if(result != null) {
                result = result.substring(1);
                result = result.substring(0, result.length() - 1);

                String[] string = result.split(", ");
                BigInteger[] bigInt = new BigInteger[string.length];
                for (int i = 0; i < string.length; i++) {
                    int n = Integer.parseInt(string[i]);
                    bigInt[i] = BigInteger.valueOf(n);
                }
                tokenIds = Arrays.asList(bigInt);
            }

        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }

        logger.info("tokenIds {}", result);
        return tokenIds;
    }

    public List<BigInteger> tokenIdsOf(String owner, String type) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- tokenIdsOf SDK called ----------------");

        List<BigInteger> tokenIds = new ArrayList<BigInteger>();
        String result = null;

        try {
            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(TOKEN_IDS_OF_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);
            chaincodeRequest.setArgs(new String[] { owner, type });

            Collection<ProposalResponse> responses = chaincodeProxy.queryByChainCode(chaincodeRequest);

            for (ProposalResponse response : responses) {
                if (response.getChaincodeActionResponsePayload() != null) {
                    result =  response.getMessage();
                }
            }

            if(result != null) {
                result = result.substring(1);
                result = result.substring(0, result.length() - 1);

                String[] string = result.split(", ");
                BigInteger[] bigInt = new BigInteger[string.length];
                for (int i = 0; i < string.length; i++) {
                    int n = Integer.parseInt(string[i]);
                    bigInt[i] = BigInteger.valueOf(n);
                }
                tokenIds = Arrays.asList(bigInt);
            }

        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }

        logger.info("tokenIds {}", result);
        return tokenIds;
    }

    public boolean deactivate(BigInteger tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- deactivate SDK called ----------------");

        String status = null;
        boolean result = false;

        try {
            String owner = erc721.ownerOf(tokenId);
            if(!(caller.equals(owner) || erc721.isApprovedForAll(owner, caller))) {
                return false;
            }

            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(DEACTIVATE_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);

            chaincodeRequest.setArgs(new String[] { tokenId.toString() });
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

    public boolean divide(BigInteger tokenId, BigInteger[] newIds, String[] values, String index) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- divide SDK called ----------------");

        String status = null;
        boolean result = false;

        try {
            String owner = erc721.ownerOf(tokenId);
            if(!(caller.equals(owner) || erc721.isApprovedForAll(owner, caller))) {
                return false;
            }

            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(DIVIDE_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);

            chaincodeRequest.setArgs(new String[] { tokenId.toString(),  Arrays.toString(newIds), Arrays.toString(values), index });
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

    public boolean update(BigInteger tokenId, String index, String attr) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- update SDK called ----------------");

        String status = null;
        boolean result = false;

        try {
            String owner = erc721.ownerOf(tokenId);
            if(!(caller.equals(owner) || erc721.isApprovedForAll(owner, caller))) {
                return false;
            }

            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(UPDATE_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);

            chaincodeRequest.setArgs(new String[] { tokenId.toString(), index, attr });
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

    public String query(BigInteger tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- query SDK called ----------------");

        String result = null;

        try {
            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(QUERY_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);
            chaincodeRequest.setArgs(new String[] { tokenId.toString() });

            Collection<ProposalResponse> responses = chaincodeProxy.queryByChainCode(chaincodeRequest);

            for (ProposalResponse response : responses) {
                if (response.getChaincodeActionResponsePayload() != null) {
                    result =  response.getMessage();
                }
            }

        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }

        logger.info("query {}", result);
        return result;
    }

    public List<String> queryHistory(BigInteger tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- queryHistory SDK called ----------------");

        List<String> histories = new ArrayList<String>();
        String result = null;

        try {
            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(QUERY_HISTORY_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);
            chaincodeRequest.setArgs(new String[] { tokenId.toString() });

            Collection<ProposalResponse> responses = chaincodeProxy.queryByChainCode(chaincodeRequest);

            for (ProposalResponse response : responses) {
                if (response.getChaincodeActionResponsePayload() != null) {
                    result =  response.getMessage();
                }
            }

            if(result != null) {
                result = result.substring(1);
                result = result.substring(0, result.length() - 1);

                histories = Arrays.asList(result.split(", "));
            }

        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }

        logger.info("query history {}", result);
        return histories;
    }
}