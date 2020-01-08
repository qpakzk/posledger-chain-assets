package com.poscoict.posledger.chain.assets.chaincode.standard;

import com.poscoict.posledger.chain.assets.chaincode.communication.ChaincodeCommunication;
import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;
import java.math.BigInteger;

import org.apache.logging.log4j.LogManager;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;

import static com.poscoict.posledger.chain.assets.chaincode.constant.Function.*;

public class BaseNFT {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(BaseNFT.class);

    private static String chaincodeId = "assetscc";

    private ChaincodeProxy chaincodeProxy;

    @Autowired
    private ERC721 erc721;

    public BaseNFT() {}

    public BaseNFT(ChaincodeProxy chaincodeProxy) {
        this.chaincodeProxy = chaincodeProxy;
    }

    private String caller;

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public boolean mint(BigInteger tokenId, String owner) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- mint SDK called ----------------");

        boolean result;
        try {
            if (!caller.equals(owner)) {
                return false;
            }

            String[] args = { tokenId.toString(), owner };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, MINT_FUNCTION_NAME, chaincodeId, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return result;
    }

    public boolean burn(BigInteger tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- burn SDK called ----------------");

        String status = null;
        boolean result = false;

        try {
            String owner = erc721.ownerOf(tokenId);
            if(!(caller.equals(owner))) {
                return false;
            }

            String[] args = { tokenId.toString() };
            result = ChaincodeCommunication.writeToChaincode(chaincodeProxy, BURN_FUNCTION_NAME, chaincodeId, args);
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
            type = ChaincodeCommunication.readFromChaincode(chaincodeProxy, GET_TYPE_FUNCTION_NAME, chaincodeId, args);
        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }
        return type;
    }
}
