package com.esign.signer.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.esign.signer.SampleBase;
import com.esign.signer.SmartCardManager;
import com.esign.signer.model.CertificateModel;
import com.esign.signer.model.TerminalModel;
import com.esign.signer.service.CertificateService;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Component;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableFile;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.pades.PAdESContext;
import tr.gov.tubitak.uekae.esya.api.signature.Signature;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureContainer;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureFactory;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureFormat;
import tr.gov.tubitak.uekae.esya.api.signature.config.Config;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

/**
 * CertificateServiceImpl
 */
@Component
public class CertificateServiceImpl implements CertificateService {

    DozerBeanMapper mapper = new DozerBeanMapper();

    private static String policyFile;
    private static ValidationPolicy validationPolicy;

    @Override
    public CertificateModel getCertificates(String[] terminals, int index) throws Exception {

        SmartCardManager cardManager = SmartCardManager.getInstance(terminals, index);

        ECertificate cert = cardManager.getSignatureCertificate(false, false);

        CertificateModel model = new CertificateModel();

        model.setCertificateSerialNumber(cert.getSerialNumber().toString());
        model.setEmail(cert.getEmail());

        model.setValidAfter(cert.getNotBefore());
        model.setValidBefore(cert.getNotAfter());

        Calendar dateNow = Calendar.getInstance();

        Boolean isExpired = (dateNow.after(model.getValidAfter()) || dateNow.equals(model.getValidAfter()))
                && (dateNow.before(model.getValidBefore()) || dateNow.equals(model.getValidBefore()));

        model.setIsExpired(isExpired);

        model.setIssuerName(cert.getIssuer().getCommonNameAttribute());

        model.setCountry(cert.getSubject().getCountryNameAttribute());

        model.setIsMaliMuhurCertificate(cert.isMaliMuhurCertificate());

        model.setOrganizationName(cert.getIssuer().getOrganizationNameAttribute());

        model.setOwnerName(cert.getSubject().getCommonNameAttribute());

        model.setSerial(cert.getSubject().getSerialNumberAttribute());

        return model;
    }

    private String[] getCardTerminals() throws Exception {

        return SmartOp.getCardTerminals();
    }

    @Override
    public List<TerminalModel> getTerminals() throws Exception {

        String[] terminals = getCardTerminals();

        List<TerminalModel> terminalList = new ArrayList<TerminalModel>();

        int index = 0;

        for (String terminal : terminals) {

            Pair<Long, CardType> info = SmartOp.getSlotAndCardType(terminal);

            TerminalModel terminalModel = new TerminalModel(info.getObject1(), info.getObject2());

            List<CertificateModel> certificateList = new ArrayList<CertificateModel>();

            certificateList.add(getCertificates(terminals, index));

            terminalModel.setSignatureCertificates(certificateList);

            terminalList.add(terminalModel);

            index++;

        }
        return terminalList;
    }

    @Override
    public void signPAdES(String pin, int index, String filePath) throws Exception {

        SmartCardManager cardManager = getSmartCardManager(index);

        try {

            ECertificate eCertificate = cardManager.getSignatureCertificate(true);
            BaseSigner signer = cardManager.getSigner(pin, eCertificate);

            SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES,
                    new FileInputStream(new File(filePath)), createContext());

            Signature signature = signatureContainer.createSignature(eCertificate);
            signature.setSigningTime(Calendar.getInstance());
            signature.sign(signer);
            signatureContainer.write(new FileOutputStream(filePath + "_copy"));
        } finally {

            cardManager.logout();

        }

    }

    private SmartCardManager getSmartCardManager(int index) throws Exception {

        String[] terminals = getCardTerminals();

        return SmartCardManager.getInstance(terminals, index);
    }

    @Override
    public void sign(String pin, int index, String filePath, ESignatureType signatureType) throws Exception {

        SmartCardManager cardManager = getSmartCardManager(index);

        try {

            BaseSignedData baseSignedData = new BaseSignedData();
            ISignable content = new SignableFile(new File(filePath));
            baseSignedData.addContent(content);

            ECertificate eCertificate = cardManager.getSignatureCertificate(true);
            BaseSigner signer = cardManager.getSigner(pin, eCertificate);

            HashMap<String, Object> params = new HashMap<String, Object>();

            params.put(EParameters.P_EXTERNAL_CONTENT, content);

            params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);
            params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

            baseSignedData.addSigner(signatureType, eCertificate, signer, null, params);

            byte[] signature = baseSignedData.getEncoded();
            cardManager.logout();

            AsnIO.dosyayaz(signature, filePath + ".p7s");

        } finally {

            cardManager.logout();
        }

    }

    @Override
    public void serialSign(String pin, int index, String filePath, ESignatureType signatureType) throws Exception {

        SmartCardManager cardManager = getSmartCardManager(index);

        try {
            byte[] signature = AsnIO.dosyadanOKU(filePath);

            BaseSignedData baseSignedData = new BaseSignedData();
            baseSignedData = new BaseSignedData(signature);

            List<Signer> signerList = baseSignedData.getSignerList();

            Signer lastSigner = signerList.get(0);

            HashMap<String, Object> params = new HashMap<String, Object>();

            params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);
            params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

            ECertificate eCertificate = cardManager.getSignatureCertificate(true);
            BaseSigner signer = cardManager.getSigner(pin, eCertificate);

            lastSigner.addCounterSigner(ESignatureType.TYPE_BES, eCertificate, signer, null, params);

            cardManager.logout();

            AsnIO.dosyayaz(baseSignedData.getEncoded(), filePath + ".p7s");
        } finally {
            cardManager.logout();
        }

    }

    protected PAdESContext createContext() {
        PAdESContext c = new PAdESContext(new File("C://Users//Lenovo").toURI());
        c.setConfig(new Config(SampleBase.ROOT_DIR + "/config/esya-signature-config.xml"));
        return c;
    }

    public synchronized ValidationPolicy getPolicy() throws ESYAException {

        policyFile = SampleBase.ROOT_DIR + "/config/certval-policy-test.xml";
        if (validationPolicy == null) {
            try {
                validationPolicy = PolicyReader.readValidationPolicy(new FileInputStream(policyFile));
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Policy file could not be found", e);
            }
        }
        return validationPolicy;
    }

}