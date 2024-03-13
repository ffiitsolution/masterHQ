/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
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

}
