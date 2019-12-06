package com.poscoict.posledger.chain.assets.config;

public class Config {

	private Config() {

	}
	
	private static String ORG1_MSP;

	private static String ORG1;

	private static String ADMIN;

	private static String ADMIN_PASSWORD;

	private static String CA_ORG1_URL;

	private static String ORDERER_URL;

	private static String ORDERER_NAME;

	private static String CHANNEL_NAME;

	private static String ORG1_PEER_0;

	private static String ORG1_PEER_0_URL;

	private static String EVENT_HUB;

	private static String CHAINCODE_1_NAME;

	public static String getORG1() {
		return ORG1;
	}

	public static void setORG1(String ORG1) {
		Config.ORG1 = ORG1;
	}

	public static String getOrg1Msp() {
		return ORG1_MSP;
	}

	public static void setOrg1Msp(String org1Msp) {
		ORG1_MSP = org1Msp;
	}

	public static String getADMIN() {
		return ADMIN;
	}

	public static void setADMIN(String ADMIN) {
		Config.ADMIN = ADMIN;
	}

	public static String getAdminPassword() {
		return ADMIN_PASSWORD;
	}

	public static void setAdminPassword(String adminPassword) {
		ADMIN_PASSWORD = adminPassword;
	}

	public static String getCaOrg1Url() {
		return CA_ORG1_URL;
	}

	public static void setCaOrg1Url(String caOrg1Url) {
		CA_ORG1_URL = caOrg1Url;
	}

	public static String getOrdererUrl() {
		return ORDERER_URL;
	}

	public static void setOrdererUrl(String ordererUrl) {
		ORDERER_URL = ordererUrl;
	}

	public static String getOrdererName() {
		return ORDERER_NAME;
	}

	public static void setOrdererName(String ordererName) {
		ORDERER_NAME = ordererName;
	}

	public static String getChannelName() {
		return CHANNEL_NAME;
	}

	public static void setChannelName(String channelName) {
		CHANNEL_NAME = channelName;
	}

	public static String getOrg1Peer0() {
		return ORG1_PEER_0;
	}

	public static void setOrg1Peer0(String org1Peer0) {
		ORG1_PEER_0 = org1Peer0;
	}

	public static String getOrg1Peer0Url() {
		return ORG1_PEER_0_URL;
	}

	public static void setOrg1Peer0Url(String org1Peer0Url) {
		ORG1_PEER_0_URL = org1Peer0Url;
	}

	public static String getEventHub() {
		return EVENT_HUB;
	}

	public static void setEventHub(String eventHub) {
		EVENT_HUB = eventHub;
	}

	public static String getChaincode1Name() {
		return CHAINCODE_1_NAME;
	}

	public static void setChaincode1Name(String chaincode1Name) {
		CHAINCODE_1_NAME = chaincode1Name;
	}



}
