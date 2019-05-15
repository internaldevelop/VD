package com.wnt.web.testsetup.entry;

import java.math.BigInteger;
import java.sql.Timestamp;

public class LDWJ_TESTSUITDETAIL {
	private BigInteger ID;
	private BigInteger TESTDEPLAYID;//'套件ID',
	private int TESTRATE;//'测试速率：0为关闭 其他为包/秒',
	private int TESTTIME;//'测试时间 0为全局 其他为秒数',
	private int STARTTESTCASE;//'起始测试用例 0为从起始位置开始 其他为从其他位置开始回溯',
	private int ENDTESTCASE;//'终止测试用例 0为从末尾位置结束 其他为从其他位置结束回溯',
	private String REMARK;//'备注',
	private int DELTATUS = 0;//default 0 comment '0：未删除1：已删除',
	private Timestamp CREATETIME;//default NULL comment '创建时间',
	private String CODE;//'编码',
	private int TRACEABILITY;//'问题溯源：0为不允许1为允许',
    private int HURRYUP;//'抓包 0为从不 1为总是',
    private int TARGET;//'目标 ：1为测试网络1 ，2为测试网络2 ，0为全部',
    private Integer POWER;//电源管理：0开启，1关闭
    private Integer SMESSAGE;//发送报文个数
    private Integer EMESSAGEP;//错误报文比例
    private Integer TESTTYPEN;   //程序业务逻辑应用
    private Integer ISSEND;
    private Integer PORTSEND; //端口号
    private Integer STOTAL;//总发包数
    private Integer TESTNUM;
    
	public BigInteger getID() {
		return ID;
	}
	public void setID(BigInteger iD) {
		ID = iD;
	}
	public BigInteger getTESTDEPLAYID() {
		return TESTDEPLAYID;
	}
	public void setTESTDEPLAYID(BigInteger tESTDEPLAYID) {
		TESTDEPLAYID = tESTDEPLAYID;
	}
	public int getTESTRATE() {
		return TESTRATE;
	}
	public void setTESTRATE(int tESTRATE) {
		TESTRATE = tESTRATE;
	}
	public int getTESTTIME() {
		return TESTTIME;
	}
	public void setTESTTIME(int tESTTIME) {
		TESTTIME = tESTTIME;
	}
	public int getSTARTTESTCASE() {
		return STARTTESTCASE;
	}
	public void setSTARTTESTCASE(int sTARTTESTCASE) {
		STARTTESTCASE = sTARTTESTCASE;
	}
	public int getENDTESTCASE() {
		return ENDTESTCASE;
	}
	public void setENDTESTCASE(int eNDTESTCASE) {
		ENDTESTCASE = eNDTESTCASE;
	}
	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
	public int getDELTATUS() {
		return DELTATUS;
	}
	public void setDELTATUS(int dELTATUS) {
		DELTATUS = dELTATUS;
	}
	public Timestamp getCREATETIME() {
		return CREATETIME;
	}
	public void setCREATETIME(Timestamp cREATETIME) {
		CREATETIME = cREATETIME;
	}
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	public int getTRACEABILITY() {
		return TRACEABILITY;
	}
	public void setTRACEABILITY(int tRACEABILITY) {
		TRACEABILITY = tRACEABILITY;
	}
	public int getHURRYUP() {
		return HURRYUP;
	}
	public void setHURRYUP(int hURRYUP) {
		HURRYUP = hURRYUP;
	}
	public int getTARGET() {
		return TARGET;
	}
	public void setTARGET(int tARGET) {
		TARGET = tARGET;
	}
	public Integer getPOWER() {
		return POWER;
	}
	public void setPOWER(Integer pOWER) {
		POWER = pOWER;
	}
	public Integer getSMESSAGE() {
		return SMESSAGE;
	}
	public void setSMESSAGE(Integer sMESSAGE) {
		SMESSAGE = sMESSAGE;
	}
	public Integer getEMESSAGEP() {
		return EMESSAGEP;
	}
	public void setEMESSAGEP(Integer eMESSAGEP) {
		EMESSAGEP = eMESSAGEP;
	}
	public Integer getTESTTYPEN() {
		return TESTTYPEN;
	}
	public void setTESTTYPEN(Integer tESTTYPEN) {
		TESTTYPEN = tESTTYPEN;
	}
	public Integer getISSEND() {
		return ISSEND;
	}
	public void setISSEND(Integer iSSEND) {
		ISSEND = iSSEND;
	}
	public Integer getPORTSEND() {
		return PORTSEND;
	}
	public void setPORTSEND(Integer pORTSEND) {
		PORTSEND = pORTSEND;
	}
	public Integer getSTOTAL() {
		return STOTAL;
	}
	public void setSTOTAL(Integer sTOTAL) {
		STOTAL = sTOTAL;
	}
	public Integer getTESTNUM() {
		return TESTNUM;
	}
	public void setTESTNUM(Integer tESTNUM) {
		TESTNUM = tESTNUM;
	}    	
	
	
}
