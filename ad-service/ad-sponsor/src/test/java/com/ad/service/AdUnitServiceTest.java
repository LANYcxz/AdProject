package com.ad.service;

import com.ad.Application;
import com.ad.entity.AdUnit;
import com.ad.exception.AdException;
import com.ad.vo.AdUnitDistrictRequest;
import com.ad.vo.AdUnitRequest;
import com.ad.vo.CreativeUnitRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AdUnitServiceTest {
    @Autowired
    private IAdUnitService adUnitService;
    @Test
    public void testAdUnit() throws AdException {
//      测试新建广告推广单元  新增的数据在表ad_unit中
        System.out.println(adUnitService.createUnit(new AdUnitRequest(11L, "推广单元测试", 2, 200000L)));
//      测试新建  广告推广单元和创意 的关联表；即每个推广单元和创意是多对多的关系  新增的数据在表creative_unit中
//      测试过程，先new获取新对象，因为传入的参数的类中含有静态内部类，因此要记得初始化！否则会报空指针异常！
        CreativeUnitRequest creativeUnitRequest = new CreativeUnitRequest();
        List<CreativeUnitRequest.CreativeUnitItem> unitItems = creativeUnitRequest.getUnitItems();
        unitItems=new ArrayList<>();//要初始化，不然报空指针异常
//      设置好 “广告单元-创意”的id关联关系
        CreativeUnitRequest.CreativeUnitItem creativeUnitItem = new CreativeUnitRequest.CreativeUnitItem();
        creativeUnitItem.setCreativeId(10L);
        creativeUnitItem.setUnitId(12L);
//      加入要传入的参数
        unitItems.add(creativeUnitItem);
        System.out.println(adUnitService.createCreativeUnit(new CreativeUnitRequest(unitItems))
        );
    }
}
