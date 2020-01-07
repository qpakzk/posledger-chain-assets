package com.poscoict.posledger.chain.assets.chaincode.standard;

import com.poscoict.posledger.chain.chaincode.executor.ChaincodeProxy;
import com.poscoict.posledger.chain.model.ChaincodeRequest;
import org.hyperledger.fabric.sdk.ProposalResponse;
import java.math.BigInteger;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;

import static com.poscoict.posledger.chain.assets.chaincode.constant.Function.*;

public class BaseNFT {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(BaseNFT.class);

    private static String chaincodeId = "assetscc";

    private static final String SUCCESS = "SUCCESS";

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

        String status = null;
        boolean result = false;

        try {
            if (!caller.equals(owner)) {
                return false;
            }

            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(MINT_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);

            chaincodeRequest.setArgs(new String[] { tokenId.toString(), owner });
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

    public boolean burn(BigInteger tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- burn SDK called ----------------");

        String status = null;
        boolean result = false;

        try {
            String owner = erc721.ownerOf(tokenId);
            if(!(caller.equals(owner))) {
                return false;
            }

            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(BURN_FUNCTION_NAME);
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

    public String getType(BigInteger tokenId) throws ProposalException, InvalidArgumentException {
        logger.info("---------------- getType SDK called ----------------");

        String type = null;

        try {
            ChaincodeRequest chaincodeRequest = new ChaincodeRequest();
            chaincodeRequest.setFunctionName(GET_TYPE_FUNCTION_NAME);
            chaincodeRequest.setChaincodeName(chaincodeId);
            chaincodeRequest.setArgs(new String[] { tokenId.toString() });

            Collection<ProposalResponse> responses = chaincodeProxy.queryByChainCode(chaincodeRequest);

            for (ProposalResponse response : responses) {
                if (response.getChaincodeActionResponsePayload() != null) {
                    type =  response.getMessage();
                }
            }

        } catch (ProposalException e) {
            logger.error(e);
            throw new ProposalException(e);
        }

        logger.info("getType {}", type);
        return type;
    }
}
