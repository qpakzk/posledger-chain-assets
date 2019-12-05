package com.poscoict.posledger.chain.assets.config;

public class Config {
	
	private static String ORG1_MSP;

	private static String ORG1;
	
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

	public static String ADMIN;

	public static String ADMIN_PASSWORD;

	public static String CA_ORG1_URL;
	
	public static String ORDERER_URL;
	
	public static String ORDERER_NAME;
	
	public static String CHANNEL_NAME;
	
	public static String ORG1_PEER_0;
	
	public static String ORG1_PEER_0_URL;

	public static String EVENT_HUB;

	public static String CHAINCODE_1_NAME;

}
