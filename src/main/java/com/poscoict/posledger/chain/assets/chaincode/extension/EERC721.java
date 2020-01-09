package com.poscoict.posledger.chain.assets.chaincode.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscoict.posledger.chain.assets.chaincode.util.ChaincodeCommunication;
import com.poscoict.posledger.chain.assets.chaincode.standard.ERC721;
import com.poscoict.posledger.chain.assets.chaincode.util.Manager;
import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import static com.poscoict.posledger.chain.assets.chaincode.util.Function.*;

public class EERC721 extends ERC721 {
    private static final Logger logger = LogManager.getLogger(EERC721.class);

    private ChaincodeProxy chaincodeProxy;

    public EERC721() {
        super();
    }

    public EERC721(ChaincodeProxy chaincodeProxy) {
        super(chaincodeProxy);
        this.chaincodeProxy = super.getChaincodeProxy();
    }

    public EERC721(ChaincodeProxy chaincodeProxy, ObjectMapper objectMapper) {
        super(chaincodeProxy, objectMapper);
        this.chaincodeProxy = super.getChaincodeProxy();
    }

    public long balanceOf(String owner, String type) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- balanceOf SDK called ----------------");

        long balance;
        try {
            String[] args = { owner, type };
            String balanceStr = ChaincodeCommunication.readFromChaincode(chaincodeProxy, BALANCE_OF_FUNCTION_NAME, args);
            balance = Long.parseLong(balanceStr);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return balance;
    }

    public List<String> tokenIdsOf(String owner) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- tokenIdsOf SDK called ----------------");

        List<String> tokenIds = new ArrayList<String>();
        try {
            String[] args = { owner };
            String tokenIdsStr = ChaincodeCommunication.readFromChaincode(chaincodeProxy, TOKEN_IDS_OF_FUNCTION_NAME, args);

            if(tokenIdsStr != null) {
                tokenIds = Arrays.asList(tokenIdsStr.substring(1, tokenIdsStr.length() - 1).split(", "));
            }
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return tokenIds;
    }

    public List<String> tokenIdsOf(String owner, String type) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- tokenIdsOf SDK called ----------------");

        List<String> tokenIds = new ArrayList<String>();
        try {
            String[] args = { owner, type };
            String tokenIdsStr = ChaincodeCommunication.readFromChaincode(chaincodeProxy, TOKEN_IDS_OF_FUNCTION_NAME, args);

            if(tokenIdsStr != null) {
                tokenIds = Arrays.asList(tokenIdsStr.substring(1, tokenIdsStr.length() - 1).split(", "));
            }
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return tokenIds;
    }

    public boolean deactivate(String tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- deactivate SDK called ----------------");

        String caller = Manager.getCaller();
        boolean result;
        try {
            String owner = super.ownerOf(tokenId);
            if(!(caller.equals(owner) || super.isApprovedForAll(owner, caller))) {
                return false;
            }

            String[] args = { tokenId };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, DEACTIVATE_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean divide(String tokenId, String[] newIds, String[] values, String index) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- divide SDK called ----------------");

        String caller = Manager.getCaller();
        boolean result;
        try {
            String owner = super.ownerOf(tokenId);
            if(!(caller.equals(owner) || super.isApprovedForAll(owner, caller))) {
                return false;
            }

            String[] args = { tokenId,  Arrays.toString(newIds), Arrays.toString(values), index };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, DIVIDE_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean update(String tokenId, String index, String attr) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- update SDK called ----------------");

        String caller = Manager.getCaller();
        boolean result;
        try {
            String owner = super.ownerOf(tokenId);
            if(!(caller.equals(owner) || super.isApprovedForAll(owner, caller))) {
                return false;
            }

            String[] args = { tokenId, index, attr };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, SET_XATTR_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public String query(String tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- query SDK called ----------------");

        String result;
        try {
            String[] args = { tokenId };
            result = ChaincodeCommunication.readFromChaincode(chaincodeProxy, QUERY_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public List<String> queryHistory(String tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- queryHistory SDK called ----------------");

        List<String> histories = new ArrayList<String>();
        String result;
        try {

            String[] args = { tokenId };
            result = ChaincodeCommunication.readFromChaincode(chaincodeProxy, QUERY_HISTORY_FUNCTION_NAME, args);

            if(result != null) {
                histories = Arrays.asList(result.substring(1, result.length() - 1).split(", "));
            }

        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return histories;
    }
}