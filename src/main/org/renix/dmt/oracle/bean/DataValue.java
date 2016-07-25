package org.renix.dmt.oracle.bean;

import org.springframework.stereotype.Component;

@Component
public class DataValue {
    
    private String tableName;
    private String parName;
    private Long tableBytes;
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public final String getParName() {
        return parName;
    }
    public final void setParName(String parName) {
        this.parName = parName;
    }
    public Long getTableBytes() {
        return tableBytes;
    }
    public void setTableBytes(Long tableBytes) {
        this.tableBytes = tableBytes;
    }

}
