package com.wnt.web.testresult.entry;

import java.sql.Timestamp;

import javax.persistence.Entity;

@Entity
public class LDWJ_TESTRESULT {
	private String ID;
	private Integer TESTDEPLAYID;
	private Timestamp CREATETIME;
	private String REMARK;
	private Integer DELSTATUS;
	private String CODE;
	private Integer EXECUTIONSTATUS;
	private Integer EXECUTIONRUSTLE;
	private String EQUIPMENTNAME;
	private String FILEURL;
	private String FILENAME;
	private String PARENTID;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public Integer getTESTDEPLAYID() {
		return TESTDEPLAYID;
	}
	public void setTESTDEPLAYID(Integer tESTDEPLAYID) {
		TESTDEPLAYID = tESTDEPLAYID;
	}
	public Timestamp getCREATETIME() {
		return CREATETIME;
	}
	public void setCREATETIME(Timestamp cREATETIME) {
		CREATETIME = cREATETIME;
	}
	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
	public Integer getDELSTATUS() {
		return DELSTATUS;
	}
	public void setDELSTATUS(Integer dELSTATUS) {
		DELSTATUS = dELSTATUS;
	}
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	public Integer getEXECUTIONSTATUS() {
		return EXECUTIONSTATUS;
	}
	public void setEXECUTIONSTATUS(Integer eXECUTIONSTATUS) {
		EXECUTIONSTATUS = eXECUTIONSTATUS;
	}
	public Integer getEXECUTIONRUSTLE() {
		return EXECUTIONRUSTLE;
	}
	public void setEXECUTIONRUSTLE(Integer eXECUTIONRUSTLE) {
		EXECUTIONRUSTLE = eXECUTIONRUSTLE;
	}
	public String getEQUIPMENTNAME() {
		return EQUIPMENTNAME;
	}
	public void setEQUIPMENTNAME(String eQUIPMENTNAME) {
		EQUIPMENTNAME = eQUIPMENTNAME;
	}
	public String getFILEURL() {
		return FILEURL;
	}
	public void setFILEURL(String fILEURL) {
		FILEURL = fILEURL;
	}
	public String getFILENAME() {
		return FILENAME;
	}
	public void setFILENAME(String fILENAME) {
		FILENAME = fILENAME;
	}
	public String getPARENTID() {
		return PARENTID;
	}
	public void setPARENTID(String pARENTID) {
		PARENTID = pARENTID;
	}
	
	
}
