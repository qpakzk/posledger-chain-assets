package com.poscoict.posledger.chain.assets.chaincode;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.identity.X509Identity;

import com.poscoict.posledger.chain.assets.client.ChannelClient;
import com.poscoict.posledger.chain.assets.client.FabricClient;
import com.poscoict.posledger.chain.assets.config.Config;
import com.poscoict.posledger.chain.assets.config.UserConfig;
import com.poscoict.posledger.chain.assets.user.UserContext;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.poscoict.posledger.chain.assets.chaincode.AddressUtils.getOwnerAddress;

public class EERC721 {


    public String register(String tokenId, String type, int pages, String hash, String signers, String path, String pathHash) {

        String result = "";
        try {

            String addr = getOwnerAddress();

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.getChannelName()).build();
            request.setChaincodeID(ccid);
            request.setFcn("mint");
            String[] arguments = { tokenId, type, addr, Integer.toString(pages), hash, signers, path, pathHash};

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO,"mint on "+Config.getChannelName());
                result = res.getMessage();
            }

        } catch (Exception e) {
            Logger.getLogger(EERC721.class.getName()).log(Level.INFO, e.getMessage());        
        }

        return result;
    }

    public String balanceOf(String type) {

        String result = "";
        try {

            String addr = getOwnerAddress();
            ChannelClient channelClient = UserConfig.initChannel();

            Thread.sleep(1000);
            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.getChannelName(), "balanceOf", new String[]{addr, type});
            for (ProposalResponse pres : responses1Query) {
                Logger.getLogger(EERC721.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            Logger.getLogger(EERC721.class.getName()).log(Level.INFO, e.getMessage());
        }

        return result;
    }

    public String deactivate(String tokenId) {

        String result = "";
        try {

            UserConfig.initUserContextForOwner();

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.getChannelName()).build();
            request.setChaincodeID(ccid);
            
            request.setFcn("deactivate");
            String[] arguments = { tokenId };
            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO,"deactivated on "+Config.getChannelName());
                result = res.getMessage();
            }

        } catch (Exception e) {
            Logger.getLogger(EERC721.class.getName()).log(Level.INFO, e.getMessage());
        }

        return result;
    }

    public String divide(String tokenId, String[] newIds, String[] values, int index) {

        String result = "";
        try {

            UserConfig.initUserContextForOwner();

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.getChannelName()).build();
            request.setChaincodeID(ccid);
            request.setFcn("divide");
            String[] arguments = { tokenId,  Arrays.toString(newIds), Arrays.toString(values), Integer.toString(index) };
            
            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO,"divided on "+Config.getChannelName());
                result = res.getMessage();
            }

        } catch (Exception e) {
            Logger.getLogger(EERC721.class.getName()).log(Level.INFO, e.getMessage());
        }

        return result;
    }

    public String update(String tokenId, String index, String attr) {

        String result = "";
        try {

            UserConfig.initUserContextForOwner();

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.getChannelName()).build();
            request.setChaincodeID(ccid);
            request.setFcn("setXAttr");
            String[] arguments = { tokenId, index, attr };
            
            request.setArgs(arguments);
            request.setProposalWaitTime(1000);

            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO,"update on "+Config.getChannelName());
                result = res.getMessage();
            }

        } catch (Exception e) {
            Logger.getLogger(EERC721.class.getName()).log(Level.INFO, e.getMessage());
        }
        
        return result;
    }

    public String query(String tokenId) {

        String result = "";
        try {

            UserConfig.initUserContextForOwner();

            ChannelClient channelClient = UserConfig.initChannel();

            Thread.sleep(1000);
            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.getChannelName(), "query", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                Logger.getLogger(EERC721.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            Logger.getLogger(EERC721.class.getName()).log(Level.INFO, e.getMessage());
        }

        return result;
    }

    public String queryHistory(String tokenId) {

        String result = "";
        try {
            ChannelClient channelClient = UserConfig.initChannel();

            Thread.sleep(1000);
            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.getChannelName(), "queryHistory", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                Logger.getLogger(EERC721.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            Logger.getLogger(EERC721.class.getName()).log(Level.INFO, e.getMessage());
        }

        return result;
    }
    
}
