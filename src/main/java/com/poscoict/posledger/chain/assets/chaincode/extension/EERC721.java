package com.poscoict.posledger.chain.assets.chaincode.extension;

import com.poscoict.posledger.chain.assets.chaincode.util.ChaincodeCommunication;
import com.poscoict.posledger.chain.assets.chaincode.standard.ERC721;
import com.poscoict.posledger.chain.assets.chaincode.util.Manager;
import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;
import java.math.BigInteger;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;

import static com.poscoict.posledger.chain.assets.chaincode.util.Function.*;

public class EERC721 {
    private static final Logger logger = LogManager.getLogger(EERC721.class);

    private ChaincodeProxy chaincodeProxy;

    @Autowired
    private ERC721 erc721;

    public EERC721() {}

    public EERC721(ChaincodeProxy chaincodeProxy) {
        this.chaincodeProxy = chaincodeProxy;
    }

    private String caller = Manager.getCaller();

    public BigInteger balanceOf(String owner, String type) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- balanceOf SDK called ----------------");

        BigInteger balanceBigInt;
        try {
            String[] args = { owner, type };
            String balance = ChaincodeCommunication.readFromChaincode(chaincodeProxy, BALANCE_OF_FUNCTION_NAME, args);
            balanceBigInt = new BigInteger(balance);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return balanceBigInt;
    }

    public List<BigInteger> tokenIdsOf(String owner) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- tokenIdsOf SDK called ----------------");

        List<BigInteger> tokenIds = new ArrayList<BigInteger>();
        try {
            String[] args = { owner };
            String response = ChaincodeCommunication.readFromChaincode(chaincodeProxy, TOKEN_IDS_OF_FUNCTION_NAME, args);

            if(response != null) {
                response = response.substring(1, response.length() - 1);

                String[] strings = response.substring(1, response.length() - 1).trim().split(",");
                for (String string : strings) {
                    tokenIds.add(new BigInteger(string));
                }
            }
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return tokenIds;
    }

    public List<BigInteger> tokenIdsOf(String owner, String type) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- tokenIdsOf SDK called ----------------");

        List<BigInteger> tokenIds = new ArrayList<BigInteger>();
        try {
            String[] args = { owner, type };
            String response = ChaincodeCommunication.readFromChaincode(chaincodeProxy, TOKEN_IDS_OF_FUNCTION_NAME, args);

            if(response != null) {
                response = response.substring(1, response.length() - 1);

                String[] strings = response.substring(1, response.length() - 1).trim().split(",");
                for (String string : strings) {
                    tokenIds.add(new BigInteger(string));
                }
            }
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return tokenIds;
    }

    public boolean deactivate(BigInteger tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- deactivate SDK called ----------------");

        boolean result;
        try {
            String owner = erc721.ownerOf(tokenId);
            if(!(caller.equals(owner) || erc721.isApprovedForAll(owner, caller))) {
                return false;
            }

            String[] args = { tokenId.toString() };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, DEACTIVATE_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean divide(BigInteger tokenId, BigInteger[] newIds, String[] values, String index) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- divide SDK called ----------------");

        boolean result;
        try {
            String owner = erc721.ownerOf(tokenId);
            if(!(caller.equals(owner) || erc721.isApprovedForAll(owner, caller))) {
                return false;
            }

            String[] args = { tokenId.toString(),  Arrays.toString(newIds), Arrays.toString(values), index };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, DIVIDE_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean update(BigInteger tokenId, String index, String attr) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- update SDK called ----------------");

        boolean result;
        try {
            String owner = erc721.ownerOf(tokenId);
            if(!(caller.equals(owner) || erc721.isApprovedForAll(owner, caller))) {
                return false;
            }

            String[] args = { tokenId.toString(), index, attr };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, UPDATE_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public String query(BigInteger tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- query SDK called ----------------");

        String result;
        try {
            String[] args = { tokenId.toString() };
            result = ChaincodeCommunication.readFromChaincode(chaincodeProxy, QUERY_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public List<String> queryHistory(BigInteger tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- queryHistory SDK called ----------------");

        List<String> histories = new ArrayList<String>();
        String result;
        try {

            String[] args = { tokenId.toString() };
            result = ChaincodeCommunication.readFromChaincode(chaincodeProxy, QUERY_HISTORY_FUNCTION_NAME, args);

            if(result != null) {
                histories = Arrays.asList(result.substring(1, result.length() - 1).trim().split(","));
            }

        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return histories;
    }
}