package com.sducat.system.service;

import com.sducat.system.data.po.Donation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Created by skyyemperor on 2021-03-09
 * Description :
 */
public interface DonationService extends IService<Donation> {

    /**
     * 添加捐款公示
     */
    void addDonation(String userName, Integer money, Integer year, Integer month);

    /**
     * 按月获取捐款公示名单
     */
    List<Donation> getDonationList(Integer year, Integer month);

    /**
     * 更新打赏二维码
     */
    void updateQRCode(String pic);

    /**
     * 获取打赏二维码
     */
    String getQRCode();
}
