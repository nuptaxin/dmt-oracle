package org.renix.dmt.oracle.bean;

import org.springframework.stereotype.Component;

/**
 * @author renzx
 *
 */
@Component
public class TmpExpData {
    private String type;
    private String tableName;
    private Long bytes;

    public TmpExpData() {}

    public TmpExpData(String type, String tableName, Long bytes) {
        this.type = type;
        this.tableName = tableName;
        this.bytes = bytes;
    }

    public final String getType() {
        return type;
    }

    public final void setType(String type) {
        this.type = type;
    }

    public final String getTableName() {
        return tableName;
    }

    public final void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public final Long getBytes() {
        return bytes;
    }

    public final void setBytes(Long bytes) {
        this.bytes = bytes;
    }
}
