package com.poscoict.posledger.chain.assets.chaincode.standard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscoict.posledger.chain.assets.chaincode.SDK;
import com.poscoict.posledger.chain.assets.chaincode.util.ChaincodeCommunication;
import com.poscoict.posledger.chain.assets.chaincode.util.Manager;
import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;
import java.math.BigInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import static com.poscoict.posledger.chain.assets.chaincode.util.Function.*;

public class ERC721 extends SDK {
    private static final Logger logger = LogManager.getLogger(ERC721.class);

    private ChaincodeProxy chaincodeProxy;

    public ERC721() {
        super();
    }

    public ERC721(ChaincodeProxy chaincodeProxy) {
        super(chaincodeProxy);
        this.chaincodeProxy = super.getChaincodeProxy();
    }

    public ERC721(ChaincodeProxy chaincodeProxy, ObjectMapper objectMapper) {
        super(chaincodeProxy, objectMapper);
        this.chaincodeProxy = super.getChaincodeProxy();
    }

    public BigInteger balanceOf(String owner) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- balanceOf SDK called ----------------");

        BigInteger balanceBigInt;
        try {
            String[] args = { owner };
            String balance = ChaincodeCommunication.readFromChaincode(chaincodeProxy, BALANCE_OF_FUNCTION_NAME, args);
            balanceBigInt = new BigInteger(balance);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return balanceBigInt;
    }

    public String ownerOf(BigInteger tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- ownerOf SDK called ----------------");

        String owner;
        try {
            String[] args = { tokenId.toString() };
            owner = ChaincodeCommunication.readFromChaincode(chaincodeProxy, OWNER_OF_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return owner;
    }


    public boolean transferFrom(String from, String to, BigInteger tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- transferFrom SDK called ----------------");

        String caller = Manager.getCaller();
        boolean result;
        try {
            String owner = ownerOf(tokenId);
            String approved = getApproved(tokenId);
            if(!(caller.equals(owner) || isApprovedForAll(owner, caller) || caller.equals(approved))) {
                return false;
            }

            String[] args = { from, to, tokenId.toString() };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, TRANSFER_FROM_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean approve(String approved, BigInteger tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- approve SDK called ----------------");

        String caller = Manager.getCaller();
        boolean result;
        try {
            String owner = ownerOf(tokenId);
            if(!(caller.equals(owner) || isApprovedForAll(owner, caller))) {
                return false;
            }

            String[] args = { approved, tokenId.toString() };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, APPROVE_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean setApprovalForAll(String operator, boolean approved) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- setApprovalForAll SDK called ----------------");

        String caller = Manager.getCaller();
        boolean result;
        try {
            if (caller.equals(operator)) {
                return false;
            }

            String[] args = { caller, operator, Boolean.toString(approved) };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, SET_APPROVAL_FOR_ALL_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public String getApproved(BigInteger tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- getApproved SDK called ----------------");

        String approved;
        try {
            String[] args = { tokenId.toString() };
            approved = ChaincodeCommunication.readFromChaincode(chaincodeProxy, GET_APPROVED_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return approved;
    }

    public boolean isApprovedForAll(String owner, String operator) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- isApprovedForAll SDK called ----------------");

        boolean result;
        try {
            String[] args = { owner, operator };
            String response = ChaincodeCommunication.readFromChaincode(chaincodeProxy, IS_APPROVED_FOR_ALL_FUNCTION_NAME, args);
            result = Boolean.parseBoolean(response);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public static void main(String[] args)  {
        /*
         * main method is not used
         */
    }
}