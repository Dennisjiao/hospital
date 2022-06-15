package com.atguigu.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import com.atguigu.yygh.cmn.listener.DictListener;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

//    @Autowired
//    private DictMapper dictMapper;

    //根据id查询子数据列表
    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    //因为再ServiceImpl中已经有了@Autowired所以不需要再注入Mapper了,直接使用baseMapper.方法就可以使用
    public List<Dict> findChildData(Long id) {

        QueryWrapper<Dict> wrapper =new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(wrapper);

        //向list集合每个dict对象中设置haschildren
        for(Dict dict:dictList){                      //遍历list集合(dictList),得到其中的每个对象(dict)
            Long dictId = dict.getId();               //得到对象中的id值
            boolean isChild = this.isChildren(dictId);//根据得到的id值，去查下面有没有子节点(二级数据).有true赋值给isChild，没有false赋值给isChild
            dict.setHasChildren(isChild);             //把得到的true或者false，set到Hash表中
        }
        return dictList;                              //最后返回集合，返回的集合中的Dict中就会有true和false的值
    }

    //导出数据字典接口
    @Override
    public void exportDictData(HttpServletResponse response) {
        //设置下载信息
        response.setContentType("application/vnd.ms-excel");           //content类型是excel类型
        response.setCharacterEncoding("utf-8");                        //
        String fileName = "dict";                                      //设置文件名称
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

        //查询数据库
        List<Dict> dictList = baseMapper.selectList(null);
        //将dict转换为dictEeVo操作
        List<DictEeVo> dictVoList = new ArrayList<>();
        for(Dict dict:dictList){
            DictEeVo dictEeVo = new DictEeVo();
            //dictEeVo.setId(dict.getId());  和下面的BeanUtils作用一样  先get值，后set进去值
            BeanUtils.copyProperties(dict,dictEeVo);
            dictVoList.add(dictEeVo);
        }

        //调用方法进行写操作
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict").doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //导入数据字典 需要一个监听器
    @Override
    @CacheEvict(value = "dict", allEntries=true)
    public void importDictData(MultipartFile file) {
        //第一个是这个文件的流  第二个文件是实体类， 第三个是监听器
        try {
            //EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(dictMapper)).sheet().doRead();    //或者不在上面写Autowired 直接传BaseMapper也可以
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();    //这样也可以 加sheet和doRead就可以将excel表提出来并加到数据库中
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //根据dictcode和value查询
    @Override
    public String getDictName(String dictCode, String value) {
        //如果dictCode为空，直接根据value查询
        if(StringUtils.isEmpty(dictCode)){
            //直接根据value查询
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value", value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }else{
            //如果dictCode为不空，直接根据dictCode和value查询
            //根据dictCode查询dict对象
            Dict codeDict = this.getDictByDictCode(dictCode);
            Long parent_id = codeDict.getId();
            //根据parent_id和value进行查询
            Dict findDict = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", parent_id)
                    .eq("value", value));

            return findDict.getName();
        }
    }

    private Dict getDictByDictCode(String dictCode){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", dictCode);
        Dict codeDict = baseMapper.selectOne(wrapper);
        return codeDict;
    }

    //判断id下面是否有子节点
    private boolean isChildren(Long id){
        QueryWrapper<Dict> wrapper =new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        //count>0 证明有数据，则返回true count=0说明没有数据,0>0则会返回false
        return count>0;
    }
}
