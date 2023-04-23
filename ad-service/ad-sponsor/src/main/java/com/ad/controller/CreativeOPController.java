package com.ad.controller;

import com.ad.service.ICreativeService;
import com.ad.vo.CreativeRequest;
import com.ad.vo.CreativeResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Slf4j
@RestController
public class CreativeOPController {
    private final ICreativeService creativeService;

    public CreativeOPController(ICreativeService creativeService) {
        this.creativeService = creativeService;
    }
    @PostMapping("/create/creative")
    public CreativeResponse createCreative(
            @RequestBody CreativeRequest request
    ) {
        log.info("ad-sponsor: createCreative -> {}",
                JSON.toJSONString(request));
        return creativeService.createCreative(request);
    }

}
