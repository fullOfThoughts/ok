package com.bmd.core.service;

import com.bmd.core.pojo.po.BmdProduct;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZS
 * @since 2021-04-22
 */
public interface BmdProductService extends IService<BmdProduct> {
    Boolean removePandI(   String uname, String pname);
}
