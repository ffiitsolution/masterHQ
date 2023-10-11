/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.master.services;

import com.ffi.api.master.dao.ProcessDao;
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
}
