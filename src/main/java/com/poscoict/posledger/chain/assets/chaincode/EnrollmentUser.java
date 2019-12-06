package com.poscoict.posledger.chain.assets.chaincode;

import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallet.Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.identity.X509Identity;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

import com.poscoict.posledger.chain.assets.config.Config;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.RegistrationException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnrollmentUser {

    String userID;
    static final String orgMSP = "Org1MSP";

    AddressUtils addressUtils;

    public void enrollAdmin() throws MalformedURLException, ClassNotFoundException, InstantiationException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, CryptoException, InvalidArgumentException, IOException, EnrollmentException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException {

        boolean adminExists = false;
        // if url starts as https.., need to set SSL
        HFCAClient caClient = HFCAClient.createNewInstance(Config.getCaOrg1Url(), null/*props*/);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallet.createFileSystemWallet(Paths.get("wallet"));

        if(wallet == null)
            Logger.getLogger(EnrollmentUser.class.getName()).log(Level.INFO, "wallet fail");
        else {
            Logger.getLogger(EnrollmentUser.class.getName()).log(Level.INFO, "------------------------------");
            // Check to see if we've already enrolled the admin user.
            adminExists = wallet.exists(Config.getADMIN());

            if (adminExists) {
                Logger.getLogger(EnrollmentUser.class.getName()).log(Level.INFO, "already exists in the wallet " + Config.getADMIN());
                return;
            }

            // Enroll the admin user, and import the new identity into the wallet.
            final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
            enrollmentRequestTLS.addHost("localhost");
            enrollmentRequestTLS.setProfile("tls");
            Enrollment enrollment = caClient.enroll(Config.getADMIN(), Config.getAdminPassword(), enrollmentRequestTLS);
            Identity user = Identity.createIdentity(orgMSP, enrollment.getCert(), enrollment.getKey());
            wallet.put(Config.getADMIN(), user);
            Logger.getLogger(EnrollmentUser.class.getName()).log(Level.INFO, "Successfully enrolled user " + Config.getADMIN());
        }
    }

    public Enrollment registerUser(String _userID) throws MalformedURLException, ClassNotFoundException, InstantiationException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, CryptoException, InvalidArgumentException, IOException, EnrollmentException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException, RegistrationException {

        this.userID = _userID;

        // if url starts as https.., need to set SSL
        HFCAClient caClient = HFCAClient.createNewInstance(Config.getCaOrg1Url(), null/*props*/);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallet.createFileSystemWallet(Paths.get("wallet"));

        // Check to see if we've already enrolled the user.
        boolean userExists = wallet.exists(this.userID);
        if (userExists) {
            Logger.getLogger(EnrollmentUser.class.getName()).log(Level.INFO, this.userID + " already exists in the wallet");
            return null;
        }

        userExists = wallet.exists(Config.getADMIN());
        if (!userExists) {
            Logger.getLogger(EnrollmentUser.class.getName()).log(Level.INFO, Config.getADMIN() + " needed to be enroll ");
            return null;
        }

        Identity adminIdentity = wallet.get(Config.getADMIN());
        User admin = new User() {

            @Override
            public String getName() {
                return Config.getADMIN();
            }

            @Override
            public Set<String> getRoles() {
                return Collections.emptySet();
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return "org1.department1";
            }

            @Override
            public Enrollment getEnrollment() {
                return new Enrollment() {

                    @Override
                    public PrivateKey getKey() {
                        return adminIdentity.getPrivateKey();
                    }

                    @Override
                    public String getCert() {
                        return adminIdentity.getCertificate();
                    }
                };
            }

            @Override
            public String getMspId() {
                return orgMSP;
            }
        };

        // Register the user, enroll the user, and import the new identity into the wallet.
        Enrollment enrollment = null;
        try {
            RegistrationRequest registrationRequest = new RegistrationRequest(this.userID);
            registrationRequest.setAffiliation("org1.department1");
            registrationRequest.setEnrollmentID(this.userID);
            String enrollmentSecret = caClient.register(registrationRequest, admin);
            enrollment = caClient.enroll(this.userID, enrollmentSecret);
            Identity user = Identity.createIdentity(orgMSP, enrollment.getCert(), enrollment.getKey());
            wallet.put(this.userID, user);
            Logger.getLogger(EnrollmentUser.class.getName()).log(Level.INFO, "Successfully enrolled user " + this.userID);
        } catch(Exception e) {

        }

        return enrollment;
    }

}
