package com.parttime.enterprise.uicomman.appsearch.custommodels;

public class SearchFilterModels {
    private String dataType;
    private String idOrSelectedValue;
    private String filterType;
    private String displayName;
    private boolean isCheckedData;

    public SearchFilterModels(String dataType, String idOrSelectedValue, String filterType, String displayName, boolean isCheckedData) {
        this.dataType = dataType;
        this.idOrSelectedValue = idOrSelectedValue;
        this.filterType = filterType;
        this.displayName = displayName;
        this.isCheckedData = isCheckedData;
    }

    public boolean isCheckedData() {
        return isCheckedData;
    }

    public void setCheckedData(boolean checkedData) {
        isCheckedData = checkedData;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getIdOrSelectedValue() {
        return idOrSelectedValue;
    }

    public void setIdOrSelectedValue(String idOrSelectedValue) {
        this.idOrSelectedValue = idOrSelectedValue;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "SearchFilterModels{" +
                "dataType='" + dataType + '\'' +
                ", idOrSelectedValue='" + idOrSelectedValue + '\'' +
                ", filterType='" + filterType + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
