package com.bmd.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmd.core.feign.ossClient;
import com.bmd.core.pojo.po.BmdPicture;
import com.bmd.core.pojo.po.BmdProduct;
import com.bmd.core.mapper.BmdProductMapper;
import com.bmd.core.service.BmdPictureService;
import com.bmd.core.service.BmdProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ZS
 * @since 2021-04-22
 */
@Service
@Transactional
public class BmdProductServiceImpl extends ServiceImpl<BmdProductMapper, BmdProduct> implements BmdProductService {
    @Autowired
    private BmdPictureService bmdPictureService;
    @Autowired
    private ossClient ossClient;
    @Autowired
    private BmdProductService bmdProductService;

    @Override
    public Boolean removePandI( String uname, String pname) {
        try {
            //  删除产品
            QueryWrapper<BmdProduct> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("uid", uname);
            wrapper1.eq("pname", pname);
            bmdProductService.remove(wrapper1);
            //  删除图片
            QueryWrapper<BmdPicture> wrapper = new QueryWrapper<>();
            wrapper.eq("mapper_id", uname);
            wrapper.eq("pid", pname);
            List<BmdPicture> list = bmdPictureService.list(wrapper);
            if (list.size() > 0) {
                list.forEach(bean -> {
                    bmdPictureService.removeById(bean.getId());
                    removeAvatar(ossClient, bean.getUrl());
                });
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void removeAvatar(ossClient ossClient, String fileName) {
        String name = fileName.substring(fileName.indexOf(".com/") + 5);
        ossClient.remove(name);
    }
}
