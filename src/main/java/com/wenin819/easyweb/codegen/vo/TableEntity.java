package com.wenin819.easyweb.codegen.vo;


import com.wenin819.easyweb.core.utils.ConfigUtils;
import com.wenin819.easyweb.core.utils.StringUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.*;

/**
 * 表对应的代码生成辅助类.
 * Created by wenin819@gmail.com on 2014/4/7.
 */
public class TableEntity {

    private String schema;
    private String name;
    private String remarks;
    private String className;
    private List<TableField> fieldList;
    private final List<String> primaryKeyList = new ArrayList<String>();

    public List<TableField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<TableField> fieldList) {
        this.fieldList = fieldList;
    }

    public String getTableId() {
        return schema + '.' + name;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSchemaPropName() {
        if(null == schema) {
            return "";
        }
        Set<String> keys = ConfigUtils.get().keySetStartWith("schema.");
        for (String key : keys) {
            if(schema.equalsIgnoreCase(ConfigUtils.get().getValue(key))) {
                return key;
            }
        }
        return "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.className = StringUtils.capitalizeAll(name);
    }

    public String getClassName() {
        return className;
    }

    public String getRemarks() {
        return ObjectUtils.toString(remarks, "");
    }

    public void setRemarks(String remarks) {
        if(null != remarks) {
            remarks = remarks.replaceAll("\\n|\\r", "  ");
        }
        this.remarks = remarks;
    }

    public void addPrimaryKeyColumn(String collumnName) {
        if (null == collumnName) {
            return;
        }
        collumnName = collumnName.toUpperCase();
        if (!primaryKeyList.contains(collumnName)) {
            primaryKeyList.add(collumnName);
        }
    }

    public TableField getPrimaryField() {
        if (primaryKeyList.isEmpty() || primaryKeyList.size() > 1) {
            // 自动识别常用的ID
            primaryKeyList.add("ID");
        }
        for (TableField tableField : fieldList) {
            if (primaryKeyList.contains(tableField.getCollumnName().toUpperCase())) {
                return tableField;
            }
        }
        // 否则默认取第一个键
        return fieldList.get(0);
    }

    public List<TableField> getFieldListWithoutKey() {
        List<TableField> list = new ArrayList<TableField>(fieldList.size());
        TableField primaryField = getPrimaryField();
        for (TableField tableField : fieldList) {
            if (tableField != primaryField) {
                list.add(tableField);
            }
        }
        return list;
    }

    public Set<String> getImportTypeList() {
        if (null != this.fieldList) {
            Set<String> importTypeSet = new HashSet<String>();
            for (TableField tableField : fieldList) {
                Class javaType = tableField.getJavaType();
                if (null != javaType && !javaType.getName().startsWith("java.lang")) {
                    importTypeSet.add(javaType.getName());
                }
            }
            return importTypeSet;
        }
        return null;
    }

    @Override
    public int hashCode() {
        return getTableId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TableEntity && null != getTableId() &&
                getTableId().equals(obj);
    }

    @Override
    public String toString() {
        return "TableEntity{" +
               "schema='" + schema + '\'' +
               ", name='" + name + '\'' +
               ", remarks='" + remarks + '\'' +
               ", className='" + className + '\'' +
               ", fieldList=" + fieldList +
               ", primaryKeyList=" + primaryKeyList +
               '}';
    }
}
