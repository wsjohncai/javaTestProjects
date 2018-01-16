package com.sss.datamodel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class TableModel extends AbstractTableModel{

    public static final int SALERCS_COL = 0;
    public static final int GOODSINFO_COL = 1;
    public static final int REPONUMSINFO_COL = 2;
    public static final int CUSINFO_COL = 3;
    public static final int SUPPLIERINFO_COL = 4;

    private Vector<String> columnNames;
    
    private void initColumns(int type) {
        String[] columnString = {};
        switch (type) {
            case SALERCS_COL:
                columnString = new String[]{"记录编号", "客户姓名", "商品名称", "数量", "总价", "日期"};
                break;
            case GOODSINFO_COL:
                columnString = new String[]{"商品编号", "商品名", "商品类型", "型号", "单价", "库存"};
                break;
            case REPONUMSINFO_COL:
                columnString = new String[]{"商品编号", "商品名", "商品类型", "库存", "仓库位置"};
                break;
            case CUSINFO_COL:
                columnString = new String[]{"客户编号", "客户名", "联系方式", "客户地址", "购买限制"};
                break;
            case SUPPLIERINFO_COL:
                columnString = new String[]{"商品编号", "商品名", "型号", "供应商编号", "供应商名称"
                        , "供应单价", "产地", "最大供应量", "联系人", "联系方式"};
                break;
        }
        columnNames.addAll(Arrays.asList(columnString));
    }


    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public String getColumnName(int column) {
        return super.getColumnName(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
