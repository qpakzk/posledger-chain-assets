package com.poscoict.posledger.chain.assets.chaincode.standard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscoict.posledger.chain.assets.chaincode.util.ChaincodeCommunication;
import com.poscoict.posledger.chain.assets.chaincode.util.Manager;
import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;
import java.math.BigInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import static com.poscoict.posledger.chain.assets.chaincode.util.Function.*;

public class BaseNFT extends ERC721 {

    private static final Logger logger = LogManager.getLogger(BaseNFT.class);

    private ChaincodeProxy chaincodeProxy;

    public BaseNFT() {
        super();
    }

    public BaseNFT(ChaincodeProxy chaincodeProxy) {
        super(chaincodeProxy);
        this.chaincodeProxy = super.getChaincodeProxy();
    }

    public BaseNFT(ChaincodeProxy chaincodeProxy, ObjectMapper objectMapper) {
        super(chaincodeProxy, objectMapper);
        this.chaincodeProxy = super.getChaincodeProxy();
    }

    public boolean mint(BigInteger tokenId, String owner) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- mint SDK called ----------------");

        String caller = Manager.getCaller();
        boolean result;
        try {
            if (!caller.equals(owner)) {
                return false;
            }

            String[] args = { tokenId.toString(), owner };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, MINT_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean burn(BigInteger tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- burn SDK called ----------------");

        String caller = Manager.getCaller();
        boolean result;
        try {
            String owner = super.ownerOf(tokenId);
            if(!(caller.equals(owner))) {
                return false;
            }

            String[] args = { tokenId.toString() };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, BURN_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public String getType(BigInteger tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- getType SDK called ----------------");

        String type;
        try {
            String[] args = { tokenId.toString() };
            type = ChaincodeCommunication.readFromChaincode(chaincodeProxy, GET_TYPE_FUNCTION_NAME, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return type;
    }
}
