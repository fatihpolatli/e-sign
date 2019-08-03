package com.esign.signer.model;

import java.util.Calendar;

/**
 * CertificateModel
 */
public class CertificateModel {

    // ECertificate cert = new ECertificate(aObject)

    private String CertificateSerialNumber;

    private String Country;

    private String Email;

    private String IssuerName;

    private Boolean IsExpired;

    private Boolean IsMaliMuhurCertificate;

    private String OrganizationName;

    private String OwnerName;

    private String Serial;

    private Integer SlotId;

    private Calendar ValidAfter;

    private Calendar ValidBefore;

	public String getCertificateSerialNumber() {
		return CertificateSerialNumber;
	}

	public void setCertificateSerialNumber(String certificateSerialNumber) {
		CertificateSerialNumber = certificateSerialNumber;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getIssuerName() {
		return IssuerName;
	}

	public void setIssuerName(String issuerName) {
		IssuerName = issuerName;
	}

	public Boolean getIsExpired() {
		return IsExpired;
	}

	public void setIsExpired(Boolean isExpired) {
		IsExpired = isExpired;
	}

	public Boolean getIsMaliMuhurCertificate() {
		return IsMaliMuhurCertificate;
	}

	public void setIsMaliMuhurCertificate(Boolean isMaliMuhurCertificate) {
		IsMaliMuhurCertificate = isMaliMuhurCertificate;
	}

	public String getOrganizationName() {
		return OrganizationName;
	}

	public void setOrganizationName(String organizationName) {
		OrganizationName = organizationName;
	}

	public String getOwnerName() {
		return OwnerName;
	}

	public void setOwnerName(String ownerName) {
		OwnerName = ownerName;
	}

	public String getSerial() {
		return Serial;
	}

	public void setSerial(String serial) {
		Serial = serial;
	}

	public Integer getSlotId() {
		return SlotId;
	}

	public void setSlotId(Integer slotId) {
		SlotId = slotId;
	}

	public Calendar getValidAfter() {
		return ValidAfter;
	}

	public void setValidAfter(Calendar validAfter) {
		ValidAfter = validAfter;
	}

	public Calendar getValidBefore() {
		return ValidBefore;
	}

	public void setValidBefore(Calendar validBefore) {
		ValidBefore = validBefore;
	}


}