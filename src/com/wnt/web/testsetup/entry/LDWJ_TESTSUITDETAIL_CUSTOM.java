package com.wnt.web.testsetup.entry;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Entity;
@Entity
public class LDWJ_TESTSUITDETAIL_CUSTOM {

	private String ID;
	private BigInteger TESTDEPLAYID;
	private String TYPENAME;
	private String FIELDNAME;
	private String FIELDVALUE;
	private Integer FIELDTYPE;
	private Integer FIELDLEN;
	private Integer DELTATUS;
	private Integer TYPE;
	private Timestamp CREATETIME;
	private Integer SORTS;
	private String REMARK;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public BigInteger getTESTDEPLAYID() {
		return TESTDEPLAYID;
	}
	public void setTESTDEPLAYID(BigInteger tESTDEPLAYID) {
		TESTDEPLAYID = tESTDEPLAYID;
	}
	public String getTYPENAME() {
		return TYPENAME;
	}
	public void setTYPENAME(String tYPENAME) {
		TYPENAME = tYPENAME;
	}
	public String getFIELDNAME() {
		return FIELDNAME;
	}
	public void setFIELDNAME(String fIELDNAME) {
		FIELDNAME = fIELDNAME;
	}
	public String getFIELDVALUE() {
		return FIELDVALUE;
	}
	public void setFIELDVALUE(String fIELDVALUE) {
		FIELDVALUE = fIELDVALUE;
	}
	public Integer getFIELDTYPE() {
		return FIELDTYPE;
	}
	public void setFIELDTYPE(Integer fIELDTYPE) {
		FIELDTYPE = fIELDTYPE;
	}
	public Integer getFIELDLEN() {
		return FIELDLEN;
	}
	public void setFIELDLEN(Integer fIELDLEN) {
		FIELDLEN = fIELDLEN;
	}
	public Integer getDELTATUS() {
		return DELTATUS;
	}
	public void setDELTATUS(Integer dELTATUS) {
		DELTATUS = dELTATUS;
	}
	public Integer getTYPE() {
		return TYPE;
	}
	public void setTYPE(Integer tYPE) {
		TYPE = tYPE;
	}
	public Timestamp getCREATETIME() {
		return CREATETIME;
	}
	public void setCREATETIME(Timestamp cREATETIME) {
		CREATETIME = cREATETIME;
	}
	public Integer getSORTS() {
		return SORTS;
	}
	public void setSORTS(Integer sORTS) {
		SORTS = sORTS;
	}
	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}	
	
	
	
}
