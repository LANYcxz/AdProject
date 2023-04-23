package com.ad.service.impl;

import com.ad.dao.CreativeRepository;
import com.ad.entity.Creative;
import com.ad.service.ICreativeService;
import com.ad.vo.CreativeRequest;
import com.ad.vo.CreativeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Service
public class CreativeServiceImpl implements ICreativeService {

    private final CreativeRepository creativeRepository;

    @Autowired
    public CreativeServiceImpl(CreativeRepository creativeRepository) {
        this.creativeRepository = creativeRepository;
    }

    @Override
    public CreativeResponse createCreative(CreativeRequest request) {

        Creative creative = creativeRepository.save(
                request.convertToEntity()
        );

        return new CreativeResponse(creative.getId(), creative.getName());
    }
}
