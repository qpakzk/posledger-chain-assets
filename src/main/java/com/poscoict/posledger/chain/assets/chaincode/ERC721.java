package com.poscoict.posledger.chain.assets.chaincode;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.ChaincodeResponse;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.identity.X509Identity;

import com.poscoict.posledger.chain.assets.client.ChannelClient;
import com.poscoict.posledger.chain.assets.client.FabricClient;
import com.poscoict.posledger.chain.assets.config.Config;
import com.poscoict.posledger.chain.assets.config.UserConfig;
import com.poscoict.posledger.chain.assets.user.UserContext;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ERC721 {
    
    public String register(String tokenId) {

        String result = "";
        try {

            UserContext userContext = UserConfig.initUserContextForOwner();
            X509Identity identity = new X509Identity(userContext);
            String addr = AddressUtils.getMyAddress(identity);

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.getChannelName()).build();
            request.setChaincodeID(ccid);
            request.setFcn("mint");
            String[] arguments = { tokenId, addr};

            request.setArgs(arguments);
            
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO,"mint on "+Config.getChannelName());
                result = res.getMessage();
            }

        } catch (Exception e) {
            Logger.getLogger(ERC721.class.getName()).log(Level.INFO, e.getMessage());
        }

        return result;
    }

    public String balanceOf() {

        String result = "";
        try {

            UserContext userContext = UserConfig.initUserContextForOwner();
            X509Identity identity = new X509Identity(userContext);
            String addr = AddressUtils.getMyAddress(identity);

            ChannelClient channelClient = UserConfig.initChannel();

            Thread.sleep(1000);
            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.getChannelName(), "balanceOf", new String[]{addr});
            for (ProposalResponse pres : responses1Query) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            Logger.getLogger(ERC721.class.getName()).log(Level.INFO, e.getMessage());
        }

        return result;
    }

    public String ownerOf(String tokenId) {

        String result = "";
        try {

            ChannelClient channelClient = UserConfig.initChannel();

            Thread.sleep(1000);
            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.getChannelName(), "ownerOf", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            Logger.getLogger(ERC721.class.getName()).log(Level.INFO, e.getMessage());
        }

        return result;
    }

    public String approve(String tokenId) {
        String result = "";
        try {


            UserConfig.initUserContextForOwner();

            UserContext userContext = UserConfig.initUserContextForApproved();
            X509Identity identity = new X509Identity(userContext);
            String addrApproved = AddressUtils.getMyAddress(identity);

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.getChannelName()).build();
            request.setChaincodeID(ccid);
            request.setFcn("approve");
            String[] arguments = { addrApproved, tokenId };

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);
            
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO,"approve on "+Config.getChannelName());
                result = res.getMessage();
            }


        } catch (Exception e) {
            Logger.getLogger(ERC721.class.getName()).log(Level.INFO, e.getMessage());
        }
        return result;
    }

    public String getApproved(String tokenId) {

        String result="";
        try {

            UserConfig.initUserContextForOwner();

            ChannelClient channelClient = UserConfig.initChannel();

            Thread.sleep(1000);
            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.getChannelName(), "getApproved", new String[]{tokenId});
            for (ProposalResponse pres : responses1Query) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }
        } catch (Exception e) {
            Logger.getLogger(ERC721.class.getName()).log(Level.INFO, e.getMessage());
        }

        return result;
    }

    public String setApprovalForAll(String approved) {
        String result = "";
        try {

            UserConfig.initUserContextForOwner();

            UserContext userContext = UserConfig.initUserContextForOperator();
            X509Identity identity = new X509Identity(userContext);
            String addrOperator = AddressUtils.getMyAddress(identity);

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.getChannelName()).build();
            request.setChaincodeID(ccid);
            request.setFcn("setApprovalForAll");
            String[] arguments = { addrOperator , approved};

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);
            
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO,"setApprovalForAll on "+Config.getChannelName());
                result = res.getMessage();
            }


        } catch (Exception e) {
            Logger.getLogger(ERC721.class.getName()).log(Level.INFO, e.getMessage());
        }
        return result;
    }

    public String isApprovedForAll() {
        String result = "";
        try {

            UserContext userContext = UserConfig.initUserContextForOwner();
            X509Identity identity = new X509Identity(userContext);
            String addr = AddressUtils.getMyAddress(identity);

            userContext = UserConfig.initUserContextForOperator();
            identity = new X509Identity(userContext);
            String addrOperator = AddressUtils.getMyAddress(identity);

            ChannelClient channelClient = UserConfig.initChannel();

            Thread.sleep(1000);
            Collection<ProposalResponse> responses1Query = channelClient.queryByChainCode(Config.getChannelName(), "isApprovedForAll", new String[]{addr, addrOperator});
            for (ProposalResponse pres : responses1Query) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO, pres.getMessage());
                result = pres.getMessage();
            }

        } catch (Exception e) {
            Logger.getLogger(ERC721.class.getName()).log(Level.INFO, e.getMessage());
        }

        return result;
    }

    public String transfer(String tokenId) {
        String result = "";
        try {

            UserContext userContext = UserConfig.initUserContextForOwner();
            X509Identity identity = new X509Identity(userContext);
            String addr = AddressUtils.getMyAddress(identity);

            userContext = UserConfig.initUserContextForNewOwner();
            identity = new X509Identity(userContext);
            String newOwnerAddr = AddressUtils.getMyAddress(identity);

            ChannelClient channelClient = UserConfig.initChannel();
            FabricClient fabClient = UserConfig.getFabClient();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.getChannelName()).build();
            request.setChaincodeID(ccid);
            request.setFcn("transferFrom");
            String[] arguments = { addr, newOwnerAddr , tokenId};

            request.setArgs(arguments);
            request.setProposalWaitTime(1000);
            
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res: responses) {
                Logger.getLogger(ERC721.class.getName()).log(Level.INFO,"transfer on "+Config.getChannelName());
                result = res.getMessage();
            }

        } catch (Exception e) {
            Logger.getLogger(ERC721.class.getName()).log(Level.INFO, e.getMessage());
        }
        return result;
    }

    public static void main(String args[]) {

    }
}
