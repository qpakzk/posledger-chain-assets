package com.poscoict.posledger.chain.assets.chaincode;

import com.poscoict.posledger.chain.assets.config.Config;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.io.pem.PemReader;
import org.hyperledger.fabric.protos.msp.Identities;
import org.hyperledger.fabric.sdk.identity.X509Identity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AddressUtils {

    static boolean isValidAddress(String address) {
        if (address == null || address.length() != 42) {
            return false;
        }
        String checksum = checksum(address.replace("0x", "").toLowerCase());
        return checksum.equals(address);
    }

    static String getAddressFor(Certificate certificate) {
        return getAddressFor(certificate.getPublicKey().getEncoded());
    }

    static String getAddressFor(byte[] pubKey) {
        String pubKeyHash = sha3String(pubKey);
        String shortPubKeyHash = pubKeyHash.substring(pubKeyHash.length() - 40); // keep last 20 bytes
        return checksum(shortPubKeyHash);
    }

    private static byte[] sha3(byte[] input) {
        Keccak.Digest256 kecc = new Keccak.Digest256();
        kecc.update(input, 0, input.length);
        return kecc.digest();
    }

    private static String sha3String(String input) {
        return sha3String(input.getBytes(UTF_8));
    }

    private static String sha3String(byte[] input) {
        return toHex(sha3(input));
    }

    private static String toHex(byte[] input) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : input) {
            stringBuilder.append(String.format("%02x", b & 0xFF));
        }
        return stringBuilder.toString();
    }

    private static String checksum(String lowercaseHashWithoutPrefix) {
        String inputHash = sha3String(lowercaseHashWithoutPrefix);
        StringBuilder result = new StringBuilder();
        result.append("0x");

        for (int i = 0; i < lowercaseHashWithoutPrefix.length(); i++) {
            if (Integer.parseInt(String.valueOf(inputHash.charAt(i)), 16) >= 8) {
                result.append(String.valueOf(lowercaseHashWithoutPrefix.charAt(i)).toUpperCase());
            } else {
                result.append(lowercaseHashWithoutPrefix.charAt(i));
            }
        }

        return result.toString();
    }

    public static String getMyAddress(X509Identity identity) {
        X509Certificate cert = getMyCertificate(identity);
        if(cert != null)
            return AddressUtils.getAddressFor(cert);
        else
            return "FAILURE";
    }

    public static X509Certificate getMyCertificate(X509Identity identity) {
        try {
            Identities.SerializedIdentity serializedIdentity = identity.createSerializedIdentity();
            StringReader reader = new StringReader(serializedIdentity.getIdBytes().toStringUtf8());
            PemReader pr = new PemReader(reader);
            byte[] x509Data = pr.readPemObject().getContent();
            CertificateFactory factory = CertificateFactory.getInstance("X509");
            return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(x509Data));
        } catch (IOException | CertificateException e) {
            Logger.getLogger(AddressUtils.class.getName()).log(Level.INFO, e.getMessage());
        }

        return null;
    }
}