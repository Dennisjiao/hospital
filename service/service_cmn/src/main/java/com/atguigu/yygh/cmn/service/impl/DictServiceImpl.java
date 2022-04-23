package com.atguigu.yygh.cmn.service.impl;

import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    //根据id查询子数据列表
    @Override
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

    //判断id下面是否有子节点
    private boolean isChildren(Long id){
        QueryWrapper<Dict> wrapper =new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        //count>0 证明有数据，则返回true count=0说明没有数据,0>0则会返回false
        return count>0;
    }
}
