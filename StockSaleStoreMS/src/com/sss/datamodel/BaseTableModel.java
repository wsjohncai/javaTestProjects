package com.sss.datamodel;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

public class BaseTableModel extends AbstractTableModel {

    public static final int SALERCS_COL = 0;
    public static final int GOODSINFO_COL = 1;
    public static final int REPONUMSINFO_COL = 2;
    public static final int CUSINFO_COL = 3;
    public static final int SUPPLYINFO_COL = 4;
    public static final int LOGININFO_COL = 5;
    public static final int REPOINFO_COL = 6;
    public static final int REPORCD_COL = 7;
    public static final int STOCKRCD_COL = 8;
    public static final int DEPTINFO_COL = 9;
    public static final int STUFFINFO_COL = 10;
    public static final int SUPPLIERINFO_COL = 11;
    public static final String[] NAMES = {"销售记录", "商品信息", "库存信息", "客户信息", "供应信息", "账户管理"
            , "仓库信息", "出入库记录", "采购记录", "部门信息", "职工表", "供应商表"};

    private Vector<String> columnNames;
    private Vector<Vector<Object>> rowData;
    private String table, uId, rawTable;
    private String[] columnString = {}, columnIds = {};

    public BaseTableModel(int table_type) {
        initColumns(table_type);
        updateData();
    }

    private void initColumns(int type) {
        columnNames = new Vector<>();
        switch (type) {
            case SALERCS_COL:
                columnString = new String[]{"记录编号", "客户姓名", "商品名称", "数量", "总价"
                        , "日期", "经手人编号"};
                columnIds = new String[]{"sale_id", "c_id", "g_id"
                        , "sale_num", "sale_total_p", "sale_date", "s_id"};
                table = "view_sale_rcd";
                rawTable = "sale";
                uId = "sale_id";
                break;
            case GOODSINFO_COL:
                columnString = new String[]{"商品编号", "商品名", "商品类型", "型号", "单价", "库存"};
                columnIds = new String[]{"g_id", "g_name", "t_id", "g_spec", "g_unit_p", "g_num"};
                table = "view_goods_info";
                rawTable = "goods";
                uId = "g_id";
                break;
            case REPONUMSINFO_COL:
                columnString = new String[]{"商品编号", "商品名", "商品类型", "库存", "仓库位置"};
                columnIds = new String[]{"g_id", "g_name", "t_name", "g_num", "r_id"};
                table = "view_mgr_reponum";
                uId = "g_id";
                break;
            case CUSINFO_COL:
                columnString = new String[]{"客户编号", "客户名", "联系方式", "客户地址", "购买限制"};
                columnIds = new String[]{"c_id", "c_name", "c_con", "c_addr", "cre_lv"};
                table = "view_customer_info";
                rawTable = "customer";
                uId = "c_id";
                break;
            case SUPPLYINFO_COL:
                columnString = new String[]{"商品编号", "商品名", "型号", "供应商编号", "供应单价"
                        , "产地", "最大供应量"};
                columnIds = new String[]{"g_id", "g_name", "g_spec", "sup_id", "sup_unit_p", "sup_ori", "sup_maxp"};
                table = "view_stock_supplier";
                uId = "g_id";
                break;
            case LOGININFO_COL:
                columnString = new String[]{"登陆ID", "密码", "在线状态"};
                columnIds = new String[]{"userId", "passwd", "ustatus"};
                table = "user";
                uId = "userId";
                break;
            case REPOINFO_COL:
                columnString = new String[]{"仓库号", "仓库面积", "仓库电话"};
                columnIds = new String[]{"r_id", "r_area", "r_phone"};
                table = "repo";
                uId = "r_id";
                break;
            case REPORCD_COL:
                columnString = new String[]{"记录编号", "仓库编号", "经手人", "出入类型", "日期", "商品", "数量"};
                columnIds = new String[]{"rcd_id", "r_id", "s_id", "type", "rcd_date", "g_id", "num"};
                table = "repo_rcd";
                uId = "rcd_id";
                break;
            case STOCKRCD_COL:
                columnString = new String[]{"采购编号", "采购员", "商品编号", "采购单价", "数量"
                        , "供应商", "总金额", "日期"};
                columnIds = new String[]{"b_id", "s_id", "g_id", "b_unit_p", "b_num", "b_total_p"
                        , "sup_id", "b_date"};
                table = "stock";
                uId = "b_id";
                break;
            case DEPTINFO_COL:
                columnString = new String[]{"部门编号", "部门", "部门电话"};
                columnIds = new String[]{"d_id", "d_name", "d_con"};
                table = "department";
                uId = "d_id";
                break;
            case STUFFINFO_COL:
                columnString = new String[]{"员工号", "姓名", "性别", "年龄", "部门"
                        , "职位", "联系方式"};
                columnIds = new String[]{"s_id", "s_name", "s_sex", "s_age", "dept", "job", "s_con"};
                table = "stuff";
                uId = "s_id";
                break;
            case SUPPLIERINFO_COL:
                columnString = new String[]{"供应商编号", "供应商名称", "联系人", "联系方式", "地址"};
                columnIds = new String[]{"sup_id", "sup_name", "sup_conp", "sup_con", "sup_addr"};
                table = "supplier";
                uId = "sup_id";
                break;
        }
//        System.out.println("Initialized: " + ((table == null) ? type + "" : table));
        columnNames.addAll(Arrays.asList(columnString));
    }

    public String getTableName() {
        String returnVal = table;
        if (table.contains("view")) {
            if (rawTable != null)
                returnVal = rawTable;
        }
        return returnVal;
    }

    public String[] getColumnIds() {
        return columnIds;
    }

    public String[] getColumnString() {
        return columnString;
    }

    public boolean deleteData(int idx) {
        String id = (String) getValueAt(idx, 0);
        String sql = "delete from " + table + " where " + uId + "=?";
        return alterData(sql, id);
    }

    /**
     * 更新表格数据
     */
    public void updateData() {
        if (rowData == null)
            rowData = new Vector<>();
        rowData.removeAllElements();
        String sql = "select * from " + table;
        DataBaseTool db = new DataBaseTool();
        ResultSet rs = db.query(sql);
        try {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 0; i < columnString.length; i++) {
                    row.add(rs.getString((i + 1)));
                }
                rowData.add(row);
            }
            db.closeAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fireTableDataChanged();
    }

    /**
     * 更新、插入、删除操作
     *
     * @return 成功返回true
     */
    public boolean alterData(String sql, String... params) {
        DataBaseTool db = new DataBaseTool();
        boolean v = db.update(sql, params);
        db.closeAll();
        return v;
    }

    @Override
    public int getRowCount() {
        return rowData.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex >= 0 && columnIndex < getColumnCount()) {
            return getValueAt(0, columnIndex).getClass();
        } else
            return Object.class;
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rowData.get(rowIndex).get(columnIndex);
    }
}
