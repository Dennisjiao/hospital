package com.atguigu.yygh.cmn.controller;


import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/admin/cmn/dict")
@Api(value ="数据字典")
@CrossOrigin
public class DictController {


    @Autowired
    private DictService dictService;


    //导入数据
    @PostMapping("importData")  //MultipartFile是Spring中得到上传的文件
    public Result importData(MultipartFile file){
        dictService.importDictData(file);       //实现导入操作
        return Result.ok();

    }

    //根据数据id查询id下的子数据(由id查parentId)
    @GetMapping("findChildData/{id}")
    @ApiOperation(value ="根据数据id查询子数据列表")
    public Result findChildData(@PathVariable Long id){
        List<Dict> list = dictService.findChildData(id);
        return  Result.ok(list);
    }
    //导出数据字典接口
    @GetMapping("exportData")
    //加入response是为了优化用户体验，可以让用户自己选择下载位置
    //因为不用返回数据，所以不用result类型了s
    public void exportDict(HttpServletResponse response){
        dictService.exportDictData(response);
    }



}
