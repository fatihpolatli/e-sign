package com.esign.signer;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.stereotype.Component;

import tr.gov.tubitak.uekae.esya.api.asn.pqixqualified.EQCStatement;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil;
import tr.gov.tubitak.uekae.esya.asn.PKIXqualified.QCStatement;

/**
 * Provides required variables and functions
 */
@Component
public class SampleBase {

    // bundle root directory of project
    public static final String ROOT_DIR = "C:/ma3api-java-bundle";

    // gets only qualified certificates in smart card
    public static final boolean IS_QUALIFIED = true;

    // the pin of the smart card
    public static final String PIN_SMARTCARD = "13275190";

    @PostConstruct
    public static void init() {

        try {
            System.out.println("init");
            LicenseUtil.setLicenseXml(new FileInputStream(ROOT_DIR + "/lisans/lisans.xml"));

            Date expirationDate = LicenseUtil.getExpirationDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("License expiration date :" + dateFormat.format(expirationDate));
            /*
             * SmartCard smartCard = new SmartCard(CardType.AKIS);
             * 
             * long[] slots = smartCard.getSlotList(); long session =
             * smartCard.openSession(slots[0]); smartCard.login(session, getPin());
             * List<byte[]> certBytes = smartCard.getSignatureCertificates(session); for
             * (byte[] bs : certBytes) { ECertificate cert = new ECertificate(bs);
             * System.out.println(cert.getSubject().getCommonNameAttribute()); }
             * smartCard.logout(session); smartCard.closeSession(session);
             */
/*
            SmartCardManager man = SmartCardManager.getInstance();
            ECertificate cert = man.getSignatureCertificate(true, false);
     
            EQCStatement[] statements = cert.getExtensions().getQCStatements().getQCStatements();
            System.out.println(statements.length);
            System.out.println(cert.getSerialNumber());

            EQCStatement statement = statements[0];

            QCStatement qc = statement.getObject();

            System.out.println(qc.statementId.getClass().getName());

            System.out.println(qc.statementInfo);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            mapper.configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, false);

            mapper.configure(SerializationFeature.EAGER_SERIALIZER_FETCH, false);
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            mapper.configure(SerializationFeature.WRAP_EXCEPTIONS, false);
            mapper.configure(SerializationFeature.WRAP_EXCEPTIONS, false);
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
            mapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);

            System.out.println(mapper.writeValueAsString(cert));
            */
            // ilk imzayı al
            // Signature signature = container.getSignatures().get(0);

            /*
             * // To set class path URL root = SampleBase.class.getResource("/"); String
             * classPath = root.getPath(); File binDir = new File(classPath); ROOT_DIR =
             * binDir.getParentFile().getParent();
             */

            /*
             * // To sign with pfx file String PFX_FILE = ROOT_DIR +
             * "/sertifika deposu/testuc@test.com.tr_313729.pfx"; String PFX_PASS =
             * "313729"; PfxSignTest pfxSigner = new PfxSignTest(SignatureAlg.RSA_SHA256,
             * PFX_FILE, PFX_PASS.toCharArray()); certificate =
             * pfxSigner.getSignersCertificate();
             */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the bundle root directory of project
     *
     * @return the root dir
     */
    protected static String getRootDir() {
        return ROOT_DIR;
    }

    /**
     * Gets the pin of the smart card
     *
     * @return the pin
     */
    protected static String getPin() throws Exception {
        throw new Exception("Set the pin of the smart card!");
        // return PIN_SMARTCARD;
    }

    /**
     * The parameter to choose the qualified certificates in smart card
     *
     * @return the
     */
    protected static boolean isQualified() {
        return IS_QUALIFIED;
    }

}
