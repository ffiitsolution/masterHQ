package com.ffi.api.master.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author USER
 */
public class TableAlias {

    @JsonProperty("data")
    private String data;

    @JsonProperty("table")
    private String table;

    @JsonProperty("alias")
    private String alias;

    @JsonProperty("dateColumn")
    private String dateColumn;

    @JsonProperty("hasOutletCode")
    private boolean hasOutletCode;

    @JsonProperty("emptyFirst")
    private boolean emptyFirst;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("process")
    private boolean process;

    @JsonProperty("dateUpd")
    private String dateUpd;

    @JsonProperty("partitionBy")
    private String partitionBy;

    @JsonProperty("timeUpd")
    private String timeUpd;

    @JsonProperty("primaryKeys")
    private String primaryKeys;

    @JsonProperty("priority")
    private String priority;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDateColumn() {
        return dateColumn;
    }

    public void setDateColumn(String dateColumn) {
        this.dateColumn = dateColumn;
    }

    public String getPartitionBy() {
        return partitionBy;
    }

    public void setPartitionBy(String partitionBy) {
        this.partitionBy = partitionBy;
    }

    public boolean isHasOutletCode() {
        return hasOutletCode;
    }

    public void setHasOutletCode(boolean hasOutletCode) {
        this.hasOutletCode = hasOutletCode;
    }

    public boolean isEmptyFirst() {
        return emptyFirst;
    }

    public void setEmptyFirst(boolean emptyFirst) {
        this.emptyFirst = emptyFirst;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getProcess() {
        return process;
    }

    public void setProcess(boolean process) {
        this.process = process;
    }

    public String getDateUpd() {
        return dateUpd;
    }

    public void setDateUpd(String dateUpd) {
        this.dateUpd = dateUpd;
    }

    public String getTimeUpd() {
        return timeUpd;
    }

    public void setTimeUpd(String timeUpd) {
        this.timeUpd = timeUpd;
    }

    public List getPrimaryKeyList() {
        return Arrays.asList(primaryKeys.split(","));
    }

    public String getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(String primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "TableAlias [data=" + data + ", table=" + table + ", alias=" + alias + ", dateColumn=" + dateColumn
                + ", hasOutletCode=" + hasOutletCode + ", emptyFirst=" + emptyFirst + ", active=" + active
                + ", process=" + process + ", dateUpd=" + dateUpd + ", timeUpd=" + timeUpd + ", primaryKeys=" + primaryKeys + "]\n";
    }
}
