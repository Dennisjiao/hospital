package com.atguigu.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;

public class DictListener extends AnalysisEventListener<DictEeVo> {
    //在DictListener要么调取数据库要么调取Mapper,所以还要写一个Mapper
    //可以加注解交给Spring管理，也可以写一个有参构造

    //这个是加入构造函数
    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }


    //一行一行读取
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        //调用方法，添加方法，添加到数据库
        //insert中传的是Dict对象，所以需要将dictEeVo new出Dict对象
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo, dict);//将dictEeVo的内容复制到dict中去
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
