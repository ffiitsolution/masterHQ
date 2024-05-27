package com.ffi.api.master.dao.impl;

import com.ffi.api.master.dao.ProcessDao;
import com.ffi.api.master.model.TableAlias;
import com.ffi.api.master.utils.DynamicRowMapper;
import com.ffi.api.master.utils.TableAliasUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author IT
 */
@Repository
public class ProcessDaoImpl implements ProcessDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    String timeStamp = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
    String dateNow = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());

    DateFormat dfDate = new SimpleDateFormat("dd-MMM-yyyy");
    DateFormat dfYear = new SimpleDateFormat("yyyy");

    @Autowired
    private TableAliasUtil tableAliasUtil;

    @Autowired
    public ProcessDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Value("${endpoint.boffi}")
    private String urlBoffi;

    @Override
    public void insertUpdateMasterItem(Map<String, String> balance) {

        String json = "";
        Gson gson = new Gson();
        Map<String, Object> map1 = new HashMap<String, Object>();
        try {
            String qry1 = "SELECT  count(*) as DUMMY  FROM M_ITEM WHERE status = 'A' AND FLAG_MATERIAL = 'Y' AND FLAG_STOCK = 'Y' ";
            Map prm = new HashMap();
            // prm.put("cdTemplate", balance.get("cdTemplate"));
            System.err.println("q1 :" + qry1);
            List<Map<String, Object>> list = jdbcTemplate.query(qry1, prm, new RowMapper<Map<String, Object>>() {
                public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                    Map<String, Object> rh = new HashMap<String, Object>();
                    String qry2 = "select nvl(ITEM_CODE,' ') as ITEM_CODE, "
                            + "nvl(CD_BRAND,' ') as CD_BRAND, "
                            + "nvl(ITEM_DESCRIPTION,' ') as ITEM_DESCRIPTION, "
                            + "nvl(CD_LEVEL_1,' ') as CD_LEVEL_1, "
                            + "nvl(CD_LEVEL_2,' ') as CD_LEVEL_2, "
                            + "nvl(CD_LEVEL_3,' ') as CD_LEVEL_3, "
                            + "nvl(CD_LEVEL_4,' ') as CD_LEVEL_4, "
                            + "nvl(AMT_COST,0) as AMT_COST, "
                            + "nvl(UOM_WAREHOUSE,' ') as UOM_WAREHOUSE, "
                            + "nvl(CONV_WAREHOUSE,0) as CONV_WAREHOUSE, "
                            + "nvl(UOM_PURCHASE,' ') as UOM_PURCHASE, "
                            + "nvl(CONV_STOCK,0) as CONV_STOCK, "
                            + "nvl(UOM_STOCK,' ') as UOM_STOCK, "
                            + "nvl(CD_WAREHOUSE,' ') as CD_WAREHOUSE, "
                            + "nvl(FLAG_OTHERS,' ') as FLAG_OTHERS, "
                            + "nvl(FLAG_MATERIAL,' ') as FLAG_MATERIAL, "
                            + "nvl(FLAG_HALF_FINISH,' ') as FLAG_HALF_FINISH, "
                            + "nvl(FLAG_FINISHED_GOOD,' ') as FLAG_FINISHED_GOOD, "
                            + "nvl(FLAG_OPEN_MARKET,' ') as FLAG_OPEN_MARKET, "
                            + "nvl(FLAG_TRANSFER_LOC,' ') as FLAG_TRANSFER_LOC, "
                            + "nvl(FLAG_CANVASING,' ') as FLAG_CANVASING, "
                            + "nvl(FLAG_STOCK,' ') as FLAG_STOCK, "
                            + "nvl(PLU,' ') as PLU, "
                            + "nvl(CD_SUPPLIER_DEFAULT,' ') as CD_SUPPLIER_DEFAULT, "
                            + "nvl(MIN_STOCK,0) as MIN_STOCK, "
                            + "nvl(MAX_STOCK,0) as MAX_STOCK, "
                            + "nvl(QTY_STOCK,0) as QTY_STOCK, "
                            + "nvl(CD_MENU_ITEM,' ') as CD_MENU_ITEM, "
                            + "nvl(CD_ITEM_LEFTOVER,' ') as CD_ITEM_LEFTOVER, "
                            + "nvl(STATUS,' ') as STATUS, "
                            + "nvl(USER_UPD,' ') as USER_UPD, "
                            + "nvl(DATE_UPD,'1 jan 1950 ') as DATE_UPD, "
                            + "nvl(TIME_UPD,'000000') as TIME_UPD, "
                            + "nvl(FLAG_PAKET,' ') as FLAG_PAKET "
                            + "from m_item where item_code in('04-6523','02-1007')";
                    System.out.println(qry2);
                    List<Map<String, Object>> list2 = jdbcTemplate.query(qry2, prm, new RowMapper<Map<String, Object>>() {
                        @Override
                        public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
                            Map<String, Object> rt = new HashMap<String, Object>();
                            rt.put("itemCode", rs.getString("ITEM_CODE"));
                            rt.put("cdBrand", rs.getString("CD_BRAND"));
                            rt.put("itemDescription", rs.getString("ITEM_DESCRIPTION"));
                            rt.put("cdLevel1", rs.getString("CD_LEVEL_1"));
                            rt.put("cdLevel2", rs.getString("CD_LEVEL_2"));
                            rt.put("cdLevel3", rs.getString("CD_LEVEL_3"));
                            rt.put("cdLevel4", rs.getString("CD_LEVEL_4"));
                            rt.put("amtCost", rs.getString("AMT_COST"));
                            rt.put("uomWarehouse", rs.getString("UOM_WAREHOUSE"));
                            rt.put("convWarehouse", rs.getString("CONV_WAREHOUSE"));
                            rt.put("uomPurchase", rs.getString("UOM_PURCHASE"));
                            rt.put("convStock", rs.getString("CONV_STOCK"));
                            rt.put("uomStock", rs.getString("UOM_STOCK"));
                            rt.put("cdWarehouse", rs.getString("CD_WAREHOUSE"));
                            rt.put("flagOthers", rs.getString("FLAG_OTHERS"));
                            rt.put("flagMaterial", rs.getString("FLAG_MATERIAL"));
                            rt.put("flagHalffinish", rs.getString("FLAG_HALF_FINISH"));
                            rt.put("flagFinishedgood", rs.getString("FLAG_FINISHED_GOOD"));
                            rt.put("flagOpenmarket", rs.getString("FLAG_OPEN_MARKET"));
                            rt.put("flagTransferloc", rs.getString("FLAG_TRANSFER_LOC"));
                            rt.put("flagCanvasing", rs.getString("FLAG_CANVASING"));
                            rt.put("flagStock", rs.getString("FLAG_STOCK"));
                            rt.put("plu", rs.getString("PLU"));
                            rt.put("cdSupplierDefault", rs.getString("CD_SUPPLIER_DEFAULT"));
                            rt.put("minStock", rs.getString("MIN_STOCK"));
                            rt.put("maxStock", rs.getString("MAX_STOCK"));
                            rt.put("qtyStock", rs.getString("QTY_STOCK"));
                            rt.put("cdMenuItem", rs.getString("CD_MENU_ITEM"));
                            rt.put("cdLtemLeftover", rs.getString("CD_ITEM_LEFTOVER"));
                            rt.put("status", rs.getString("STATUS"));
                            rt.put("flagPaket", rs.getString("FLAG_PAKET"));
                            return rt;
                        }
                    });
                    rh.put("itemList", list2);
                    rh.put("dummyHeader", rs.getString("DUMMY"));
                    return rh;
                }
            });

            for (Map<String, Object> dtl : list) {
                CloseableHttpClient client = HttpClients.createDefault();
                String url = urlBoffi + "/insert-master-item";
                HttpPost post = new HttpPost(url);

                post.setHeader("Accept", "*/*");
                post.setHeader("Content-Type", "application/json");

                Map<String, Object> param = new HashMap<String, Object>();
                //param.put("COBA", dtl.get("COBA"));
                param.put("userUpd", balance.get("userUpd"));
                param.put("itemList", dtl.get("itemList"));
                param.put("dateUpd", dateNow);
                param.put("timeUpd", timeStamp);

                json = new Gson().toJson(param);
                StringEntity entity = new StringEntity(json);
                post.setEntity(entity);
                CloseableHttpResponse response = client.execute(post);
                System.out.println("json" + json);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                (response.getEntity().getContent())
                        )
                );
                StringBuilder content = new StringBuilder();
                String line;
                while (null != (line = br.readLine())) {
                    content.append(line);
                }
                String result = content.toString();
                System.out.println("trans =" + result);
                map1 = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
                }.getType());
            }

        } catch (JsonSyntaxException | IOException | UnsupportedOperationException | DataAccessException e) {
            System.out.println("insertUpdateMasterItem :" + e.getMessage());
        }
    }

    // ========================== NEW Method from Lukas 17-10-2023 ======================
    @Override
    public List<Map<String, Object>> getDataMaster(String tableName, String date, String outletId) {
        String query = customQuery(tableName, date, outletId);
        Map prm = new HashMap();
        System.out.println("Outlet " + outletId + " Query to Database Server : " + query);
        List<Map<String, Object>> list = jdbcTemplate.query(query, prm, (ResultSet rs, int index) -> convertObject(rs));
        System.out.println("Outlet " + outletId + " Query Done: " + list.size());
        return list;
    }

    @Override
    public Map<String, Object> insertDataMaster(String tableName, List<Map<String, Object>> data, String outletId) {
        Map<String, Object> result = compareData(data, tableName, outletId);
        return result;
    }

    // ========================= Function Stand Alone ======================
    // ===================== Compare Data to Master =================
    public Map<String, Object> compareData(List<Map<String, Object>> itemReceive, String tableName, String outletId) {
        List<String> primaryKey = getPrimaryKey(tableName);
        int totalUpdateRow = 0;
        int totalInsertRow = 0;
        List<String> errors = new ArrayList();

        for (Map<String, Object> item : itemReceive) {
            String conditionText = "";
            int indexKey = 0;

            if (primaryKey.isEmpty()) {
                for (String key : item.keySet()) {
                    conditionText += key + "='" + item.get(key) + "'";
                    indexKey++;
                    if (indexKey < item.keySet().size()) {
                        conditionText += " AND ";
                    }
                }
            } else {
                for (String key : primaryKey) {
                    conditionText += key + "='" + item.get(key) + "'";
                    indexKey++;
                    if (indexKey < primaryKey.size()) {
                        conditionText += " AND ";
                    }
                }
            }

            String query = "SELECT * FROM " + tableName; // Need Update Condition HERE

            if (!"".equals(conditionText)) {
                query += " WHERE " + conditionText;
            }
            Map prm = new HashMap();
            List<Map<String, Object>> list = jdbcTemplate.query(query, prm, (ResultSet rs, int index) -> convertObject(rs));
            Map<String, Object> exist = list.isEmpty() ? null : list.get(0);
            if (exist == null || "".equals(conditionText)) {
                String resp = insertData(tableName, item);
                switch (resp) {
                    case "1" ->
                        totalInsertRow++;
                    case "0" -> {
                    }
                    default ->
                        errors.add(resp);
                }
            } else if (checkAllColumn(item, exist) == false) {
                String resp = updateData(tableName, item, primaryKey);
                switch (resp) {
                    case "1" ->
                        totalUpdateRow++;
                    case "0" -> {
                    }
                    default ->
                        errors.add(resp);
                }
            }
        }

        Map<String, Object> result = new HashMap();
        result.put("total", totalInsertRow + totalUpdateRow);
        result.put("insert", totalInsertRow);
        result.put("update", totalUpdateRow);
        result.put("errors", errors);

        System.out.println("Total Update Data Outlet " + outletId + " " + tableName + " : " + totalUpdateRow + " Row ");
        System.out.println("Total Insert Data Outlet " + outletId + " " + tableName + " : " + totalInsertRow + " Row ");

        return result;
    }

    public String customQuery(String tableName, String date, String outletId) {
        String conditionText = "";
        if (tableName.charAt(0) == 'M') {
            conditionText = " WHERE DATE_UPD = '" + date + "'";
        } else if (tableName.charAt(0) == 'T') {
            conditionText = " WHERE TRANS_DATE = '" + date + "'";
        }
        String checkOutletCode = "SELECT COUNT(column_name) FROM all_tab_columns WHERE table_name = '" + tableName + "' AND column_name = 'OUTLET_CODE'";
        int count = jdbcTemplate.queryForObject(checkOutletCode, new HashMap(), Integer.class);
        if (count > 0) {
            conditionText += " AND OUTLET_CODE = '" + outletId + "' ";
        }
        String query = switch (tableName) {
            case "M_PRICE" ->
                "SELECT * FROM M_PRICE WHERE DATE_UPD = '" + date + "' AND PRICE_TYPE_CODE IN (SELECT DISTINCT PRICE_TYPE_CODE FROM M_OUTLET_PRICE mop WHERE PRICE_TYPE_CODE <> '_' AND OUTLET_CODE = '" + outletId + "')";
            default ->
                "Select * From " + tableName + conditionText;
        };
        String checkTimeUpd = "SELECT COUNT(column_name) FROM all_tab_columns WHERE table_name = '" + tableName + "' AND column_name = 'TIME_UPD'";
        int countList = jdbcTemplate.queryForObject(checkTimeUpd, new HashMap(), Integer.class);
        if (countList > 0) {
            query += " ORDER BY TIME_UPD DESC";
        }
        return query;
    }

    public boolean checkAllColumn(Map<String, Object> itemServer, Map<String, Object> itemExist) {
        boolean result = true;

        for (String key : itemServer.keySet()) {
            switch (key) {
                case "QTY_STOCK_E", "QTY_STOCK_T", "QTY_EI", "QTY_TA" -> {
                    var temp = Double.valueOf(itemServer.get(key).toString());
                    if (!itemServer.get(key).equals(temp)) {
                        result = false;
                    }
                }
                default -> {
                    if (itemServer.get(key) != itemExist.get(key) && !itemServer.get(key).equals(itemExist.get(key))) {
                        result = false;
                    }
                }
            }
        }
        return result;
    }

    public Map<String, Object> convertObject(ResultSet result) throws SQLException {
        Map<String, Object> resultReturn = new HashMap<>();
        ResultSetMetaData rsmd = result.getMetaData();
        int cols = rsmd.getColumnCount();
        for (int i = 0; i < cols; i++) {
            String columnName = rsmd.getColumnName(i + 1);
            Object columnValue = result.getObject(i + 1);
            switch (rsmd.getColumnName(i + 1)) {
                case "ASSEMBLY_END_TIME", "ASSEMBLY_START_TIME", "BILL_DATE", "BOOK_DATE", "BUCKET_DATE", "CC_DATE", "DATE_1", "DATE_2", "DATE_3", "DATE_4", "DATE_5", "DATE_6", "DATE_ABSEN", "DATE_CREATE", "DATE_DEL", "DATE_END", "DATE_EOD", "DATE_MPCS", "DATE_OF_BIRTH", "DATE_SEND", "DATE_START", "DATE_TRANS", "DATE_UPD", "DELIVERY_DATE", "DISPATCH_END_TIME", "DISPATCH_START_TIME", "DT_DUE", "DT_EXPIRED", "EFFECTIVE_DATE", "EMPLOY_DATE", "END_DATE", "END_OF_DAY", "EVENT_DATE", "FINISH_DATE", "FINISH_TIME", "HOLIDAY_DATE", "KEY_3", "LAST_ORDER", "LAST_RECEIVE", "LAST_RETURN", "LAST_SALES", "LAST_TRANSFER_IN", "LAST_TRANSFER_OUT", "LOG_DATE", "MPCS_DATE", "OPNAME_DATE", "ORDER_DATE", "PAYMENT_DATE", "PERIODE", "PICKUP_END_TIME", "PICKUP_START_TIME", "RECV_DATE", "RESIGN_DATE", "RETURN_DATE", "START_DATE", "START_OF_DAY", "START_TIME", "SUGGEST_DATE", "SUPPLY_END_TIME", "SUPPLY_QUEUE_START_TIME", "SUPPLY_START_TIME", "TANGGAL", "TANGGAL_CREATE", "TANGGAL_MODAL", "TANGGAL_PESAN_TERAKHIR", "TANGGAL_SETOR", "TANGGAL_TARIK", "TANGGAL_TRANS", "TANGGAL_TRANSAKSI", "TD", "TGL_CREATE", "TGL_KIRIM", "TGL_MODAL", "TGL_PESAN_TERAKHIR", "TGL_RETURN", "TGL_SETOR", "TGL_TERIMA", "TGL_TRANSAKSI", "TMPFLD2", "TMPFLD9", "TMP_DATE_UPD", "TMP_TRANS_DATE", "TRANSFER_DATE", "TRANS_DATE", "VIEW_DATE_UPD", "VIEW_TRANS_DATE", "WASTAGE_DATE" -> {
                    String dateData = new SimpleDateFormat("dd-MMM-yyyy").format(columnValue);
                    resultReturn.put(columnName, dateData);
                }
                case "SEQ_MPCS", "QTY_ACC_REJECT", "QTY_ACC_VARIANCE", "ENDING_QTY", "PURCHASE_QTY", "RECEIVE_QTY", "TOT_RECORD", "TOT_ML", "TOT_MP", "ITEM_DETAIL_SEQ", "TOTAL_CHARGE", "LOG_SEQ", "AMT_TAX", "PAYMENT_USED", "CASH_BANK", "PENGAJUAN2", "NO_SEQ", "AMT_TRANS", "EI_AMOUNT", "TA_QTY", "TA_AMOUNT", "TTD", "DELTIMEM", "BEGINING_K", "ENDING_QTY_K", "BEGINING", "QTYTRANS", "TOTAL_CLAIM", "TOTAL_PENJUALAN_FNB", "COUNT_TRANS_AMOUNT", "MIN_SALES", "RND_LIMIT", "MAX_SHIFT", "ID_NO", "TAKE_AWAY", "DISC_PERCENT", "CNT", "POSTED", "QTY_BEGINING", "FML", "DLV", "JUMLAH_DETAILS", "BIAYA_ANTAR", "SETOR_TRANSAKSI", "DISC_25_NILAI", "DISC_50_TRANSAKSI", "DISC_100_TRANSAKSI", "REFUND_NILAI", "JUMLAH_CUSTOMER", "QUANTITY_EAT_EI", "AMT_ENDSHIFT", "QTY_PROD", "QTY_ACC_PROD", "PRD_QTY", "AMT_COST", "TARGET", "PERIOD_YEAR", "TOTAL_CUSTOMER", "TOTAL_PAYMENT", "TRANS_AMOUNT", "TOTAL_TAX_CHARGE", "TOTAL_DP_PAID", "TOTAL_IN", "AMT_OUT", "DEBET_AMT", "TOTAL_QTY_STOCK", "QTY1", "AMT_SALES_HDR", "QTY_SALES", "QTY_USED", "QTY_PERC", "AMT_TA", "INTIMESTOREM", "PEMBAGI", "QTY_K_IN", "RETURN_SUPP", "TOT_AMT_PAID", "PPN_CD", "AMT_CD", "TAXABLE", "DEL_LIMIT", "RND_FACT", "MAX_BILLS", "MIN_ITEMS", "FRYER_TYPE_SEQ_CNT", "MODIFIER_GROUP6_MAX_QTY", "KODE_TERMINAL", "FREE_MAGNETIC", "SALDOAWAL", "STOCKIN", "SLS2", "DEL", "PRD", "RCV", "STO", "DISCOUNT_PERSEN", "TAX_BRUTTO_AMOUNT", "KUPON_NILAI_DIGUNAKAN", "OVERALL_HARGA_SATUAN", "Total CASH", "AMT_SETOR", "WASTAGE_ID", "ITEM_COST", "RETURN_ID", "TOTAL_PRICE", "CREDIT_AMT", "ORDER_ID", "TRANSFER_ID", "AMT_SALES_DTL", "PLU_QTY", "QTY_EI", "AMT_EI", "NILAI", "SORT", "RCV_SUPPLIER", "TRANSFER_IN_OUTLET", "LEFT_OFER_OUT", "ENDING", "MULTIPLY", "QTYTA", "FLD10", "FLD6", "FLD7", "TOT_CD", "TICKET_AVG", "PPN", "TOTPAYMENTAMOUNT", "TOTPAYMENTUSE", "DISC_AMT", "DEL_CHARGE", "DP_MIN", "MODIFIER_GROUP1_MIN_QTY", "MODIFIER_GROUP2_MIN_QTY", "MODIFIER_GROUP4_MAX_QTY", "MODIFIER_GROUP5_MAX_QTY", "MODIFIER_GROUP6_MIN_QTY", "ACCESSW", "CURRENT_STOCK", "TRANSFER_OUT", "KEY_4", "MONTH", "COUNTER_NO", "G_VALUE", "NILAI_TAX", "JML_DETAIL", "NILAI_SETOR", "NILAI_REFUND", "DISKON_NILAI", "TRX", "LOV3", "PRD4", "WAS", "DIFF", "PEMBAYARAN", "DISCOUNT_PERCENT", "TAX_NETT_AMOUNT", "DISC_25_TRANSAKSI", "DISC_50_NILAI", "ERROR_NILAI", "JUMLAH_TRANSAKSI", "OVERALL_TOTAL_HARGA", "AMT_MODAL", "DINE_NILAI", "QTY_ACC_SOLD", "QTY_ACC_WASTAGE", "QTY_IN", "IN_QTY", "ADJUSTMENT_QTY", "NO_OF_PRINT", "QTY_1", "ITEM_QTY", "TOTAL_DISCOUNT", "TOTAL_TAX", "PAY_SEQ", "QTY_STOCK", "QTY_FREEZE", "TOTAL", "BEGINING_B", "RCV_GUDANG", "QTYEI", "TMPFLD6", "SUM_AMOUNT", "KUPON_DIGUNAKAN_Q", "DONASI", "TAX_CHARGE_BILL", "GROSS_SALES", "AMOUNT_BY_STATUS", "PROCESS", "VALUE", "COST", "CAT_ITEMS", "REF_TIME", "MAX_DISC_PERCENT", "TAX_CHARGE", "MODIFIER_GROUP1_MAX_QTY", "MODIFIER_GROUP3_MIN_QTY", "RECEIVE", "QTY_STOCK_T", "DRAWER", "MODAL", "TOTAL_HEADER", "TOTAL_DETAIL", "STA", "FML5", "SLS", "EI_TA", "MODAL_NILAI", "SETOR_NILAI", "DISC_75_TRANSAKSI", "DISC_100_NILAI", "ERROR_TRANSAKSI", "KUPON_QUANTITY", "TOTAL_HARGA_EI", "DINE_PERSEN", "TOTAL_PERSEN", "DS", "AMT_REFUND", "QTY_PROJ_CONV", "QTY_PROJ", "QTY_VARIANCE", "ITEM_SEQ", "QTY_PURCHASE", "QTY_2", "DAY_SEQ", "TOTAL_SALES", "TOTAL_CANCEL", "DONATE_AMOUNT", "PENGAJUAN1", "QTY", "AMT_CUSTOMER", "DELTIMES", "JUMLAH", "QTY_K_OUT", "REFUND", "PRODUKSI", "ADJUSTMENT", "FLD11", "FLD3", "SUM_COUNT", "TRANSAKSI", "DISCOUNT", "TOTAL_PENDAPATAN", "ROUNDING_BILL", "DAY_OF_WEEK", "ENABLED_MENU", "FRYER_TYPE_RESET", "MODIFIER_GROUP5_MIN_QTY", "MODIFIER_GROUP7_MAX_QTY", "ACCESSR", "CONV_WAREHOUSE", "ON_ORDER", "RETURN", "DISC_NILAI", "HARGA_SATUAN", "BIAYA_DELIVERY", "RET6", "NOMOR_POS", "JML_TRANS", "SALDO_CASH_DRAWER", "DISC_200_TRANSAKSI", "DISC_200_NILAI", "BCA_QUANTITY", "OVERALL_QUANTITY", "AWAY_PERSEN", "COUNT_BILL", "QUANTITY", "QTY_SOLD", "OUT_QTY", "BEGINNING_QTY", "QUANTITY_IN", "QTY_BONUS", "TOTAL_AMOUNT", "TOTAL_ROUNDING", "TOTAL_OUT", "QTY_PURCH", "ENDING_AMT", "UNIT_PRICE", "PENGAJUAN3", "HIST_SEQ", "PRICE", "LEVELING", "INTIMESTORES", "DOORTIMES", "GROUPES", "QTY_B_IN", "ENDING_QTY_B", "PETTY_C", "LEFT_OVER_IN", "RETURN_GUDANG", "TMPFLD4", "CUST_AVERAGE", "CHARGE_BILL", "COUNT_DISC", "MAX_CHANGE", "CANCEL_FEE", "TIME_OUT", "MIN_PULL_TRX", "FRYER_TYPE_CNT_PREV", "MODIFIER_GROUP2_MAX_QTY", "MODIFIER_GROUP3_MAX_QTY", "MODIFIER_GROUP4_MIN_QTY", "CONV_STOCK", "ORDER_FREQ", "R_VALUE", "B_VALUE", "NILAI_VOUCHER", "NILAI_BCA_DEBIT", "CUSTOMER_COUNT", "KODE_PLU", "DISKON_PERSEN", "TOTAL_KEMBALIAN", "KODE_MAP", "NOMOR", "DEL1", "WAS9", "STO11", "LOV", "DISCOUNT_NILAI", "NILAI_BCA_DEBIT_CARD", "NO_TRANS", "KUPON_NILAI_TERPAKAI", "MODAL_TRANSAKSI", "VOID_TRANSAKSI", "HARGA_SATUAN_EI", "DINE_QTY", "AMT_SALES", "TOTALSTOCK", "NOMINAL", "QTY_REJECT", "QTY_WASTAGE", "QTY_ACC_ONHAND", "QTY_OUT", "TRANSFER_OUT_QTY", "FILE_NO", "PERIOD_MONTH", "TOTAL_EXCESS", "TOTAL_REPRINT", "TOTAL_REFUND", "TOTAL_DONATION", "BEGINNING_AMT", "CD_TEMPLATE", "SEQ", "AMT_DISC", "AMT_PERC", "QTY_B_OUT", "SALES_OUT", "TRANSFER_OUT_OUTLET", "FLD2", "TOT_TRN_PAID", "TOTALBILL", "CUSTOMER", "TAX", "TOTAL_BY_STATUS", "DONE", "FLAG_CHOICE", "MAX_PULL_VALUE", "REFUND_TIME_LIMIT", "QTY_CONV", "LEVEL_MENU", "MAX_STOCK", "TRANSFER_IN", "SALES", "KEMBALI", "NILAI_PENJUALAN", "NILAI_MODAL", "SETOR_END_SHIFT", "SALES_QUERY", "REPRINT", "RCV7", "DLV8", "RET", "WFP", "CASH_I_NILAI", "TAX_AMOUNT", "TOTAL_NILAI_TRANSAKSI", "AWAY_QTY", "AMT_DP", "TOTAL_NILAI", "QTY_ACC_PROJ", "QTY_ONHAND", "TRANSFER_IN_QTY", "SEQ_NO", "QTY_BEGINNING", "FILE_SIZE", "MP", "QTY_WAREHOUSE", "TOTAL_QTY", "AMOUNT", "TOTAL_ITEM", "PERCENTAGE", "TOTAL_CHANGE", "TRANS_SEQ", "PAYMENT_AMOUNT", "DONATE_SEQ", "DP_SEQ", "TOTAL_ESTIMATE_PAYMENT", "AMT_IN", "PENGAJUAN4", "COST_FREEZE", "QTY2", "COST_OPNAME", "SERVICE_CHARGE", "EI_QTY", "QTY_TA", "TTM", "DOORTIMEM", "PRODUKSI_IN", "WASTE_OUT", "TMPFLD5", "FLD4", "FLD5", "KUPON_DIGUNAKAN_A", "KUPON_TERPAKAI", "TOTAL_PENJUALAN", "TTL_BILL_JOINT", "SUM_TRANS_AMOUNT", "CASH_BALANCE", "MAX_DISC_AMOUNT", "FRYER_TYPE_CNT", "MODIFIER_GROUP7_MIN_QTY", "MIN_STOCK", "QTY_STOCK_E", "YEAR", "SUB_TOTAL_HARGA", "TOTAL_HARGA", "PERCENT_PPN", "SETOR", "CML", "JUMLAH_PESAN", "TOTAL_NILAI_PESAN", "STOCKOUT", "SALDOAKHIR", "LOC10", "LOC", "DISC_75_NILAI", "VOID_NILAI", "REFUND_TRANSAKSI", "BCA_NILAI", "QUANTITY_EAT_TA", "TOTAL_HARGA_TA", "HARGA_SATUAN_TA", "AMT_CATERING" -> {
                    Object temp = result.getObject(i + 1);
                    if (temp == null) {
                        resultReturn.put(rsmd.getColumnName(i + 1), null);
                    } else if (temp instanceof Number number) {
                        resultReturn.put(rsmd.getColumnName(i + 1), number);
                    } else if (temp instanceof String string) {
                        String regexNumber = "^[0-9.]*$";
                        if (string.matches(regexNumber)) {
                            try {
                                Number numberValue = Double.valueOf(string);
                                resultReturn.put(rsmd.getColumnName(i + 1), numberValue);
                            } catch (NumberFormatException e) {
                                resultReturn.put(rsmd.getColumnName(i + 1), string);
                            }
                        } else {
                            resultReturn.put(rsmd.getColumnName(i + 1), string);
                        }
                    } else if (temp instanceof Date date) {
                        String dateData = new SimpleDateFormat("dd-MMM-yyyy").format(date);
                        resultReturn.put(columnName, dateData);
                    } else {
                        resultReturn.put(rsmd.getColumnName(i + 1), 0);
                    }
                }
                default -> {
                    Object temp = result.getObject(i + 1);
                    if (temp == null) {
                        resultReturn.put(rsmd.getColumnName(i + 1), null);
                    } else {
                        resultReturn.put(rsmd.getColumnName(i + 1), result.getObject(i + 1).toString());
                    }
                }
            }
        }
        return resultReturn;
    }

    public String insertData(String tableName, Map<String, Object> data) {
        String columnName = "";
        String value = "";
        int indexKey = 0;
        Map<String, Object> params = new HashMap<>();

        for (String key : data.keySet()) {
            columnName += key;
            value += ":" + key;

            Object temp = data.get(key);
            if (temp == null) {
                params.put(key, null);
            } else {
                params.put(key, data.get(key));
            }
            indexKey++;
            if (indexKey < data.keySet().size()) {
                columnName += " ,";
                value += " ,";
            }
        }

        String query = "INSERT INTO " + tableName + " (" + columnName + ") VALUES (" + value + ")";
        try {
            Integer i = jdbcTemplate.update(query, params);
            return i.toString();
        } catch (DataAccessException e) {
            return e.getMessage();
        }
    }

    public String updateData(String tableName, Map<String, Object> data, List<String> primaryKey) {
        String columnValue = "";
        String conditionQuery = "";
        int indexKey = 0;

        Map<String, Object> params = new HashMap<>();
        int counterKey = 0;
        for (String key : data.keySet()) {
            columnValue += key + "= :" + key + "";
            if (primaryKey.contains(key)) {
                conditionQuery += key + "= :" + key;
                counterKey++;
                if (counterKey < primaryKey.size()) {
                    conditionQuery += " AND ";
                }
            }

            Object temp = data.get(key);
            if (temp == null) {
                params.put(key, null);
            } else {
                params.put(key, data.get(key));
            }
            indexKey++;
            if (indexKey < data.keySet().size()) {
                columnValue += " ,";
            }
        }
        String query = "UPDATE " + tableName + " SET " + columnValue + " WHERE " + conditionQuery;
        try {
            Integer i = jdbcTemplate.update(query, params);
            return i.toString();
        } catch (DataAccessException e) {
            return e.getMessage();
        }
    }

    public List<String> getPrimaryKey(String tableName) {
        Optional<TableAlias> ta = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_T, "table", tableName);
        if (ta.isEmpty()) {
            ta = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_T, "alias", tableName);
        }
        if (ta.isEmpty()) {
            ta = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_M, "table", tableName);
        }
        if (ta.isEmpty()) {
            ta = tableAliasUtil.firstByColumn(TableAliasUtil.TABLE_ALIAS_M, "alias", tableName);
        }
        TableAlias tableAlias = ta.get();
        List<String> primaryKey = tableAlias.getPrimaryKeyList();
        return primaryKey;
    }
    // ========================== End Method from Lukas 17-10-2023 ======================

    // ========================== NEW Method from M Joko 22-5-2024 ======================
    @Override
    public Integer execVmByOctd(String outletCode, String transDate) {
        String query = "BEGIN EXEC_VM_BY_OCTD('" + outletCode + "', '" + transDate + "'); END;";
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        System.out.println("execVmByOctd: " + query + " at " + formattedDateTime);
        int rowsAffected = jdbcTemplate.update(query, new HashMap());
        System.out.println("execVmByOctd result: " + rowsAffected + " at " + formattedDateTime);
        return rowsAffected;
    }
}
