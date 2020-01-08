package com.poscoict.posledger.chain.assets.chaincode.standard;

import com.poscoict.posledger.chain.assets.chaincode.util.ChaincodeCommunication;
import com.poscoict.posledger.chain.assets.chaincode.util.Manager;
import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;
import java.math.BigInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;

import static com.poscoict.posledger.chain.assets.chaincode.util.Function.*;

public class BaseNFT {

    private static final Logger logger = LogManager.getLogger(BaseNFT.class);

    private ChaincodeProxy chaincodeProxy;

    @Autowired
    private ERC721 erc721;

    public BaseNFT() {}

    public BaseNFT(ChaincodeProxy chaincodeProxy) {
        this.chaincodeProxy = chaincodeProxy;
    }

    private String caller = Manager.getCaller();

    public boolean mint(BigInteger tokenId, String owner) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- mint SDK called ----------------");

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

        boolean result;

        try {
            String owner = erc721.ownerOf(tokenId);
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
