package com.atguigu.yygh.hosp.controller.api;


import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hosp/hospital")
public class HospApiController {

    @Autowired
    private HospitalService hospitalService;

    @ApiOperation(value = "查询医院列表")
    @GetMapping("findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable Integer page, @PathVariable Integer limit, HospitalQueryVo hospitalQueryVo){
        Page<Hospital> hospitals = hospitalService.selectHospPage(page,limit,hospitalQueryVo);
        //List<Hospital> content = hospitals.getContent();
        //int totalPages = hospitals.getTotalPages();
        return Result.ok(hospitals);
    }

    @ApiOperation(value="根据医院名称进行模糊查询")
    @GetMapping("findByHosName/{hosname}")
    public Result findByHosName(@PathVariable String hosname){
        List<Hospital> list= hospitalService.findByHosname(hosname);
        return Result.ok(list);
    }

}
