package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalController {
    //注入Service然后调用
    @Autowired
    private HospitalSetService hospitalSetService;
    //http://localhost:8080/admin/hosp/hospitalSet/findAll

    //增删改查方法

    //查询医院设置表中的所有数据
    @ApiOperation(value = "获取医院所有设置信息")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        //调用Service中的方法
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
        //return list;
    }

    /*//查询医院设置表中的所有数据
    @ApiOperation(value = "获取医院所有设置信息")
    @GetMapping("findAll")
    public List<HospitalSet> findAllHospitalSet() {
        //调用Service中的方法
        List<HospitalSet> list = hospitalSetService.list();
        return list;
    }*/

    //删除医院设置(逻辑删除)
    @ApiOperation(value = "逻辑删除医院设置信息")
    @DeleteMapping("{id}")
    public Result removeHospitalSet(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
        return Result.ok(flag);
        //return flag;
    }


}
