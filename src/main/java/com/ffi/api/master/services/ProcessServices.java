package com.ffi.api.master.services;

import com.ffi.api.master.dao.ProcessDao;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author IT
 */
@Service
public class ProcessServices {

    @Autowired
    ProcessDao dao;

    ///////////////new method from dona 27-02-2023////////////////////////////
    public void insertUpdateMasterItem(Map<String, String> balance) {
        dao.insertUpdateMasterItem(balance);

    }
    ////////////Done//////////////////////

    /////////////// new method from Lukas 17-10-2023 ////////////////////////////
    public List<Map<String, Object>> getDataMaster(String tableName, String date, String outletId) {
        return dao.getDataMaster(tableName,date, outletId);
    }
    
    public Map<String, Object> insertDataMaster(String tableName, List<Map<String,Object>> data, String outletId){
        return dao.insertDataMaster(tableName,data, outletId);
    }
    ////////////Done//////////////////////

    // ========================== NEW Method from M Joko 22-5-2024 ======================
    public Integer execVmByOctd(String outletCode, String transDate) {
        return dao.execVmByOctd(outletCode, transDate);
    }

    // ========================== NEW Method from M Joko 9-7-2024 ======================
    public List<Map<String, Object>> checkAllowTakeMaster(Map<String, Object> params) {
        return dao.checkAllowTakeMaster(params);
    }
    public Map<String, Object> updateMSyncDetail(Map<String, Object> params) {
        return dao.updateMSyncDetail(params);
    }
}
