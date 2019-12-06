package com.poscoict.posledger.chain.assets.config;

import com.poscoict.posledger.chain.assets.chaincode.ERC721;
import org.hyperledger.fabric.sdk.*;

import com.poscoict.posledger.chain.assets.client.ChannelClient;
import com.poscoict.posledger.chain.assets.client.FabricClient;
import com.poscoict.posledger.chain.assets.user.UserContext;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UserConfig {

    static String owner;
    static String newOwner;
    static String approved;
    static String operator;

    static Enrollment enrollment;
    static Enrollment enrollmentForNewOwner;
    static Enrollment enrollmentForApproved;
    static Enrollment enrollmentForOperator;

    static UserContext userContextForOwner;
    static UserContext userContextForNewOwner;
    static UserContext userContextForApproved;
    static UserContext userContextForOperator;

    static FabricClient fabClient;

    public static void setEnrollment(String _owner, Enrollment _enrollment) {
        owner = _owner;
        enrollment = _enrollment;
    }

    public static void setEnrollmentForNewOwner(String _newOwner, Enrollment _enrollmentForNewOwner) {
        newOwner = _newOwner;
        enrollmentForNewOwner = _enrollmentForNewOwner;
    }

    public static void setEnrollmentForApproved(String _approved, Enrollment _enrollmentForApproved) {
        approved = _approved;
        enrollmentForApproved = _enrollmentForApproved;
    }

    public static void setEnrollmentForOperator(String _oprator, Enrollment _enrollmentForOperator) {
        operator = _oprator;
        enrollmentForOperator = _enrollmentForOperator;
    }

    public static UserContext initUserContextForOwner() {

        userContextForOwner = new UserContext();
        userContextForOwner.setName(owner);
        userContextForOwner.setAffiliation(Config.getORG1());
        userContextForOwner.setMspId(Config.getOrg1Msp());
        userContextForOwner.setEnrollment(enrollment);

        return userContextForOwner;
    }

    public static UserContext initUserContextForNewOwner() {

        userContextForNewOwner = new UserContext();
        userContextForNewOwner.setName(newOwner);
        userContextForNewOwner.setAffiliation(Config.getORG1());
        userContextForNewOwner.setMspId(Config.getOrg1Msp());
        userContextForNewOwner.setEnrollment(enrollmentForNewOwner);

        return userContextForNewOwner;
    }

    public static UserContext initUserContextForApproved() {

        userContextForApproved = new UserContext();
        userContextForApproved.setName(approved);
        userContextForApproved.setAffiliation(Config.getORG1());
        userContextForApproved.setMspId(Config.getOrg1Msp());
        userContextForApproved.setEnrollment(enrollmentForApproved);

        return userContextForApproved;
    }

    public static UserContext initUserContextForOperator() {

        userContextForOperator = new UserContext();
        userContextForOperator.setName(operator);
        userContextForOperator.setAffiliation(Config.getORG1());
        userContextForOperator.setMspId(Config.getOrg1Msp());
        userContextForOperator.setEnrollment(enrollmentForOperator);

        return userContextForOperator;
    }
    public static FabricClient getFabClient() {
        return fabClient;
    }

    public static ChannelClient initChannel() throws InvalidArgumentException, TransactionException {

        try {
            fabClient = new FabricClient(userContextForOwner);
        } catch (Exception e) {
            Logger.getLogger(UserConfig.class.getName()).log(Level.INFO, e.getMessage());
        }

        ChannelClient channelClient = fabClient.createChannelClient(Config.getChannelName());
        Channel channel = channelClient.getChannel();
        Peer peer = fabClient.getInstance().newPeer(Config.getOrg1Peer0(), Config.getOrg1Peer0Url());
        EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", Config.getEventHub());
        Orderer orderer = fabClient.getInstance().newOrderer(Config.getOrdererName(), Config.getOrdererUrl());
        channel.addPeer(peer);
        channel.addEventHub(eventHub);
        channel.addOrderer(orderer);
        channel.initialize();

        return channelClient;
    }


}
