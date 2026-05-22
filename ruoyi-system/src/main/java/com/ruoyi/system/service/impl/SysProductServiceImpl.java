package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysProductMapper;
import com.ruoyi.system.domain.SysProduct;
import com.ruoyi.system.service.ISysProductService;
import com.ruoyi.common.core.text.Convert;

/**
 * 商品Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-12
 */
@Service
public class SysProductServiceImpl implements ISysProductService
{
    @Autowired
    private SysProductMapper sysProductMapper;

    /**
     * 查询商品
     *
     * @param id 商品主键
     * @return 商品
     */
    @Override
    public SysProduct selectSysProductById(Long id)
    {
        return sysProductMapper.selectSysProductById(id);
    }

    /**
     * 查询商品列表
     *
     * @param sysProduct 商品
     * @return 商品
     */
    @Override
    public List<SysProduct> selectSysProductList(SysProduct sysProduct)
    {
        return sysProductMapper.selectSysProductList(sysProduct);
    }

    /**
     * 新增商品
     *
     * @param sysProduct 商品
     * @return 结果
     */
    @Override
    public int insertSysProduct(SysProduct sysProduct)
    {
        sysProduct.setCreateTime(DateUtils.getNowDate());
        return sysProductMapper.insertSysProduct(sysProduct);
    }

    /**
     * 修改商品
     *
     * @param sysProduct 商品
     * @return 结果
     */
    @Override
    public int updateSysProduct(SysProduct sysProduct)
    {
        sysProduct.setUpdateTime(DateUtils.getNowDate());
        return sysProductMapper.updateSysProduct(sysProduct);
    }

    /**
     * 批量删除商品
     *
     * @param ids 需要删除的商品主键
     * @return 结果
     */
    @Override
    public int deleteSysProductByIds(String ids)
    {
        return sysProductMapper.deleteSysProductByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除商品信息
     *
     * @param id 商品主键
     * @return 结果
     */
    @Override
    public int deleteSysProductById(Long id)
    {
        return sysProductMapper.deleteSysProductById(id);
    }

    @Override
    public SysProduct selectSysProductRand() {
        return sysProductMapper.selectSysProductRand();
    }
}
