package com.atguigu.yygh.cmn.controller;


import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/smn/dict")
@Api(value ="数据字典")
@CrossOrigin
public class DictController {


    @Autowired
    private DictService dictService;

    //根据数据id查询id下的子数据(由id查parentId)
    @GetMapping("findChildData/{id}")
    @ApiOperation(value ="根据数据id查询子数据列表")
    public Result findChildData(@PathVariable Long id){
        List<Dict> list = dictService.findChildData(id);
        return  Result.ok(list);
    }
}
