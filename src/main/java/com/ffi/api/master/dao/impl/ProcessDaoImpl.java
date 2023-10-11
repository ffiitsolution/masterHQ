/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.master.dao.impl;

import com.ffi.api.master.dao.ProcessDao;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
