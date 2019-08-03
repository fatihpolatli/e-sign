package com.esign.signer.model;

import java.util.List;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;

/**
 * TerminalModel
 */
public class TerminalModel {

    private String Index;

    private String Name;

    private String LibName;

    private List<CertificateModel> SignatureCertificates;

    public TerminalModel(Long index, CardType cardType) {

        this.Index = index.toString();

        this.Name = cardType.getName();
        
        this.LibName = cardType.getLibName();

    }

    public String getIndex() {
        return Index;
    }

    public void setIndex(String index) {
        Index = index;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLibName() {
        return LibName;
    }

    public void setLibName(String libName) {
        LibName = libName;
    }

    public List<CertificateModel> getSignatureCertificates() {
        return SignatureCertificates;
    }

    public void setSignatureCertificates(List<CertificateModel> signatureCertificates) {
        SignatureCertificates = signatureCertificates;
    }

}