package com.esign.signer.service;

import java.util.List;

import com.esign.signer.model.CertificateModel;
import com.esign.signer.model.TerminalModel;

import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;

/**
 * CertificateService
 */
public interface CertificateService {

    public CertificateModel getCertificates(String[] terminals, int index) throws Exception;

    public List<TerminalModel> getTerminals() throws Exception;

    public void signPAdES(String pin, int index, String filePath) throws Exception;

    public void sign(String pin, int index, String filePath, ESignatureType signatureType) throws Exception;

    public void serialSign(String pin, int index, String filePath, ESignatureType signatureType) throws Exception;
}