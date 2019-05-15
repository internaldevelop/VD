package com.wnt.web.operationlog.entity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class OperationLogEntity implements RowMapper<OperationLogEntity>, Serializable{
   
    private static final long serialVersionUID = -8823504831198719837L;
    private int id;
    private String createtime;
    private String username;
    private String operation;
    private String result;
    private String content;
    private String ip;
    
    
    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    
    public String getCreatetime() {
        return createtime;
    }

    
    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    
    public String getUsername() {
        return username;
    }

    
    public void setUsername(String username) {
        this.username = username;
    }

    
    public String getOperation() {
        return operation;
    }

    
    public void setOperation(String operation) {
        this.operation = operation;
    }

    
    public String getResult() {
        return result;
    }

    
    public void setResult(String result) {
        this.result = result;
    }

    
    public String getContent() {
        return content;
    }

    
    public void setContent(String content) {
        this.content = content;
    }

    
    public String getIp() {
        return ip;
    }

    
    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public OperationLogEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        // TODO Auto-generated method stub
        OperationLogEntity logEntity = new OperationLogEntity();
        logEntity.setId(rs.getInt("ID")); 
        logEntity.setCreatetime(rs.getString("CREATETIME"));
        logEntity.setUsername(rs.getString("USERNAME"));
        logEntity.setOperation(rs.getString("OPERATION"));
        logEntity.setResult(rs.getString("RESULT"));
        logEntity.setContent(rs.getString("CONTENT"));
        logEntity.setIp(rs.getString("IP"));
        return logEntity;
    }
}
