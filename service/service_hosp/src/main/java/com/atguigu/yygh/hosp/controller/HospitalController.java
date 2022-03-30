package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalController {
    //注入Service然后调用
    @Autowired
    private HospitalSetService hospitalSetService;
    //http://localhost:8080/admin/hosp/hospitalSet/findAll

    //增删改查方法

    //查询医院设置表中的所有数据
    @GetMapping("findAll")
    public List<HospitalSet> findAllHospitalSet() {
        //调用Service中的方法
        List<HospitalSet> list = hospitalSetService.list();
        return list;
    }

    //删除医院设置(逻辑删除)
    @DeleteMapping("{id}")
    public boolean removeHospitalSet(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
        return flag;
    }


}
