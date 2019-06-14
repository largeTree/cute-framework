package com.hzecool.frm.monitor.controller;

import com.hzecool.fdn.Constant;
import com.hzecool.frm.devOps.DevOpsParamHolder;
import com.hzecool.frm.devOps.DevOpsParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 功能描述: 设置或显示运维参数<br>
 * 新增日期: 2018/10/3<br>
 *
 * @author laisf
 * @version 1.0.0
 */
@Controller
@RequestMapping({"/devops/param"})
public class DevOpsParamController {

    @RequestMapping(value = {"/print.do"}, method = RequestMethod.GET)
    @ResponseBody
    public String print(@RequestParam String code){
        return DevOpsParamUtils.getVal(code);
    }

    @RequestMapping(value = {"/setTemp.do"}, method = RequestMethod.GET)
    @ResponseBody
    public String setTemp(@RequestParam String code, @RequestParam String val){
        DevOpsParamUtils.setTemp(code, val);
        return "success";
    }

    @RequestMapping(value = {"/setMaxThreadBusy.do"}, method = RequestMethod.GET)
    @ResponseBody
    public String setMaxThreadBusy(@RequestParam Integer val, @RequestParam(required =false) Integer httpStatus){
        DevOpsParamHolder.setMaxThreadBusy(val);
        if(httpStatus != null){
            DevOpsParamHolder.setThreadBusyHttpStatus(httpStatus);
        }
        return "success";
    }


}
