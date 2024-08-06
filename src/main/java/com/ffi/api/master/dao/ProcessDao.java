package com.ffi.api.master.dao;

import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;

/**
 *
 * @author IT
 */
public interface ProcessDao {

    public void insertUpdateMasterItem(Map<String, String> mapping);

    /////////////// new method from Lukas 17-10-2023 ////////////////////////////
    public List<Map<String, Object>> getDataMaster(String tableName, String date, String outletId);
    public Map<String, Object> insertDataMaster(String tableName, List<Map<String, Object>> data, String outletId);
    /////////////// DONE ////////////////////////////

    // ========================== NEW Method from M Joko 22-5-2024 ======================
    public Integer execVmByOctd(String outletCode, String transDate);
    
    public List<Map<String, Object>> checkAllowTakeMaster(Map<String, Object> params);
    public Map<String, Object> updateMSyncDetail(Map<String, Object> params);
}
