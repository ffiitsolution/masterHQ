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
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    @Value("${spring.datasource.username}")
    private String schemaOwner;

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
        List<Map<String, Object>> list = jdbcTemplate.query(query, prm, (ResultSet rs, int index) -> convertObject(rs, tableName));
        System.out.println(getDateTimeForLog() + "getDataMaster " + outletId + list.size() + " rows - q: " + query);
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
            List<Map<String, Object>> list = jdbcTemplate.query(query, prm, (ResultSet rs, int index) -> convertObject(rs, tableName));
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

//        System.out.println("Total Update Data Outlet " + outletId + " " + tableName + " : " + totalUpdateRow + " Row ");
//        System.out.println("Total Insert Data Outlet " + outletId + " " + tableName + " : " + totalInsertRow + " Row ");

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

    public Map<String, Object> convertObject(ResultSet result, String tableName) throws SQLException {
        Map<String, Object> resultReturn = new HashMap<>();
        ResultSetMetaData rsmd = result.getMetaData();
        int cols = rsmd.getColumnCount();
        for (int i = 0; i < cols; i++) {
            String columnName = rsmd.getColumnName(i + 1);
            Object columnValue = result.getObject(i + 1);
            if (columnValue == null) {
                String qryCheckDefault = "SELECT data_type, data_default FROM all_tab_columns WHERE owner = :owner AND table_name = :tableName AND column_name = :columnName AND rownum = 1";
                Map prm = new HashMap();
                prm.put("tableName", tableName);
                prm.put("columnName", columnName);
                prm.put("owner", schemaOwner);
                List<Map<String, Object>> listDataType = jdbcTemplate.query(qryCheckDefault, prm, new DynamicRowMapper());
                if (listDataType.isEmpty()) {
                    if (columnValue instanceof Date) {
                        resultReturn.put(columnName, "01 JAN 1950");
                    } else if (columnValue instanceof Number) {
                        resultReturn.put(columnName, 0);
                    } else {
                        resultReturn.put(columnName, " ");
                    }
                } else {
                    Map dataType = listDataType.get(0);
                    String dType = dataType.getOrDefault("dataType", "").toString();
                    String dDefault = dataType.getOrDefault("dataDefault", " ").toString();
                    resultReturn.put(columnName, dDefault.replaceAll("'", ""));
                }
            } else {
//                resultReturn.put(columnName, columnValue);
                if (columnValue instanceof Date) {
                    String dateData = new SimpleDateFormat("dd-MMM-yyyy").format(columnValue);
                    if (columnName.equalsIgnoreCase("DATE_UPD") && (tableName.equalsIgnoreCase("T_STOCK_CARD") || tableName.equalsIgnoreCase("T_STOCK_CARD_DETAIL"))) {
                        dateData = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(columnValue);
                    }
                    resultReturn.put(columnName, dateData);
                } else {
                    resultReturn.put(columnName, columnValue);
                }
                // old code
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
            if (key.equalsIgnoreCase("DATE_UPD") && (tableName.equalsIgnoreCase("T_STOCK_CARD") || tableName.equalsIgnoreCase("T_STOCK_CARD_DETAIL"))) {
                value += "TO_DATE( :" + key + ", 'DD-MON-YYYY HH24:MI:SS')";
            } else {
                value += ":" + key;
            }

            Object temp = data.get(key);
            if (temp == null) {
                String qryCheckDefault = "SELECT data_type, data_default FROM all_tab_columns WHERE owner = :owner AND table_name = :tableName AND column_name = :columnName AND rownum = 1";
                Map prm = new HashMap();
                prm.put("tableName", tableName);
                prm.put("columnName", key);
                prm.put("owner", schemaOwner);
                List<Map<String, Object>> listDataType = jdbcTemplate.query(qryCheckDefault, prm, new DynamicRowMapper());
                if (listDataType.isEmpty()) {
                    if (temp instanceof Date) {
                        params.put(key, "01 JAN 1950");
                    } else if (temp instanceof Number) {
                        params.put(key, 0);
                    } else {
                        params.put(key, " ");
                    }
                } else {
                    Map dataType = listDataType.get(0);
                    String dType = dataType.getOrDefault("dataType", "").toString();
                    String dDefault = dataType.getOrDefault("dataDefault", " ").toString();
                    params.put(key, dDefault.replaceAll("'", ""));
                }
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
            System.out.println(getDateTimeForLog() + "insertData: " + query + " error: " + e.getMessage());
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
            if (key.equalsIgnoreCase("DATE_UPD") && (tableName.equalsIgnoreCase("T_STOCK_CARD") || tableName.equalsIgnoreCase("T_STOCK_CARD_DETAIL"))) {
                columnValue += key + "= TO_DATE( :" + key + ", 'DD-MON-YYYY HH24:MI:SS')";
            } else {
                columnValue += key + "= :" + key + "";
            }
            if (primaryKey.contains(key)) {
                conditionQuery += key + "= :" + key;
                counterKey++;
                if (counterKey < primaryKey.size()) {
                    conditionQuery += " AND ";
                }
            }

            Object temp = data.get(key);
            if (temp == null) {
                String qryCheckDefault = "SELECT data_type, data_default FROM all_tab_columns WHERE owner = :owner AND table_name = :tableName AND column_name = :columnName AND rownum = 1";
                Map prm = new HashMap();
                prm.put("tableName", tableName);
                prm.put("columnName", key);
                prm.put("owner", schemaOwner);
                List<Map<String, Object>> listDataType = jdbcTemplate.query(qryCheckDefault, prm, new DynamicRowMapper());
                if (listDataType.isEmpty()) {
                    if (temp instanceof Date) {
                        params.put(key, "01 JAN 1950");
                    } else if (temp instanceof Number) {
                        params.put(key, 0);
                    } else {
                        params.put(key, " ");
                    }
                } else {
                    Map dataType = listDataType.get(0);
                    String dType = dataType.getOrDefault("dataType", "").toString();
                    String dDefault = dataType.getOrDefault("dataDefault", " ").toString();
                    params.put(key, dDefault.replaceAll("'", ""));
                }
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
            System.out.println(getDateTimeForLog() + "updateData: " + query + " error: " + e.getMessage());
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
        System.out.println(getDateTimeForLog() + "execVmByOctd: " + query + " at " + formattedDateTime);
        int rowsAffected = jdbcTemplate.update(query, new HashMap());
        System.out.println(getDateTimeForLog() + "execVmByOctd result: " + rowsAffected + " at " + formattedDateTime);
        return rowsAffected;
    }

    public String getDateTimeForLog() {
        return LocalDateTime.now().format(dateTimeFormatter) + " - ";
    }
}
