/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.master;

import com.ffi.api.master.services.ProcessServices;
import com.ffi.paging.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author IT
 */
@RestController
public class IndexController {

    public String versionBe = "24.06.03a";

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    ProcessServices processServices;

    @Value("${spring.datasource.url}")
    private String urlDb;

    @RequestMapping(value = "/halo")
    public @ResponseBody
    Map<String, Object> tes() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("output", "welcome to master");
        map.put("urlDb", urlDb);
        map.put("versionBe", versionBe);
        return map;
    }

    @RequestMapping(value = "/send-data-to-store", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Digunakan untuk generate template", response = Object.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "The resource not found"),}
    )
    public @ResponseBody
    ResponseMessage insertUpdateMasterItem(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String status = "";
        ResponseMessage rm = new ResponseMessage();
        try {
            processServices.insertUpdateMasterItem(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Successfuly");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed: " + e.getMessage());
        }
        rm.setItem(list);
        return rm;
    }

    //============== new method from LUKAS 17-10-2023 ===============
    @RequestMapping(value = "/get-data")
    public @ResponseBody
    ResponseMessage copyPaste(@RequestParam String param, @RequestParam(required = true) String date, @RequestParam(required = true) String outletId) throws IOException, Exception {
        String dateCopy = date;
        if (date == null) {
            dateCopy = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        }

        ResponseMessage rm = new ResponseMessage();
        try {
            rm.setItem(processServices.getDataMaster(param, dateCopy, outletId));
            rm.setSuccess(true);
            rm.setMessage("Get Data Table Successfuly " + param + " For " + dateCopy);
            System.err.println(getDateTimeForLog() + "get-data: " + outletId + ": " + param + ": " + rm.getItem().size() + "rows");
        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Get Data Table " + param + " For " + dateCopy + " Error : " + e.getMessage());
            System.err.println(getDateTimeForLog() + "get-data error: " + outletId + ": " + param + ": " + e.getMessage());
        }
        return rm;
    }

    @RequestMapping(value = "/receive-data")
    public @ResponseBody
    ResponseMessage insertDataMaster(@RequestBody Map<String, Object> param) throws IOException, Exception {
        String outletId = param.get("outletId") != null ? param.get("outletId").toString() : null;
        String tableName = param.get("tableName").toString();
        List<Map<String, Object>> bodyData = (List<Map<String, Object>>) param.get("data");
        List errors = new ArrayList();
        List response = new ArrayList();
        ResponseMessage rm = new ResponseMessage();
        rm.setItem(new ArrayList());
        try {
            if (!bodyData.isEmpty()) {
                Map<String, Object> resp = processServices.insertDataMaster(tableName, bodyData, outletId);
                System.err.println(getDateTimeForLog() + "receive-data: " + outletId + ": " + tableName + ": " + resp);
                if (resp.containsKey("errors") && resp.get("errors") instanceof List) {
                    List err = ((List) resp.get("errors"));
                    if (!err.isEmpty()) {
                        errors = err;
                    }
                }
                Integer d = 0;
                if (resp.containsKey("total") && resp.get("total") instanceof Number) {
                    int total = ((Number) resp.get("total")).intValue();
                    if (total > 0) {
                        d = total;
                    }
                }
                if (d > 0 && errors.isEmpty()) {
                    rm.setSuccess(true);
                    rm.setMessage("Insert Data for table " + tableName + " Success");
                } else if (d > 0 && !errors.isEmpty()) {
                    rm.setSuccess(true);
                    rm.setMessage("Insert Data for table " + tableName + " Success Partially");
                } else {
                    rm.setSuccess(false);
                    rm.setMessage("Insert Data for table " + tableName + " Failed");
                }
                response.add(resp);
                rm.setItem(response);
            } else {
                rm.setSuccess(true);
                rm.setMessage("No Data to Insert for table " + tableName);
            }

        } catch (Exception e) {
            System.err.println(getDateTimeForLog() + "receive-data error: " + outletId + ": " + tableName + ": " + e.getMessage());
            rm.setSuccess(false);
            rm.setMessage("Insert Data Table " + tableName + " Error : " + e.getMessage());
        }
        return rm;
    }
    //============== End method from LUKAS 17-10-2023 ===============

    @RequestMapping(value = "/execVmByOctd")
    public @ResponseBody
    ResponseMessage execVmByOctd(@RequestParam String param, @RequestParam(required = true) String date, @RequestParam(required = true) String outletId) throws IOException, Exception {
        String transDate = date;
        if (date == null) {
            transDate = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        }

        ResponseMessage rm = new ResponseMessage();
        try {
            int result = processServices.execVmByOctd(outletId, transDate);
            rm.setItem(new ArrayList());
            rm.setSuccess(result > 0);
            rm.setMessage("execVmByOctd " + (result > 0 ? "Success" : "Error") + ": " + result + " rows affected.");
            System.err.println(getDateTimeForLog() + "execVmByOctd: " + outletId + ": " + (result > 0));
        } catch (Exception e) {
            System.err.println(getDateTimeForLog() + "execVmByOctd error: " + outletId + ": " + e.getMessage());
            rm.setSuccess(false);
            rm.setMessage("execVmByOctd Error: " + e.getMessage());
        }
        return rm;
    }

    public String getDateTimeForLog() {
        return LocalDateTime.now().format(dateTimeFormatter) + " - ";
    }
}
