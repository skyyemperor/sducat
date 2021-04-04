package com.sducat.system.service.impl;

import com.sducat.system.mapper.StaticValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sducat.system.data.po.Donation;
import com.sducat.system.mapper.DonationMapper;
import com.sducat.system.service.DonationService;

/**
 * Created by skyyemperor on 2021-03-09
 * Description :
 */
@Service
public class DonationServiceImpl extends ServiceImpl<DonationMapper, Donation> implements DonationService {

    @Autowired
    private DonationMapper donationMapper;

    @Autowired
    private StaticValueMapper staticValueMapper;

    private static final String DONATION_QRCODE_URL_KEY = "DONATION_QRCODE_URL";

    @Override
    public void addDonation(String userName, Integer money, Integer year, Integer month) {
        LocalDate date = LocalDate.of(year, month, 1);
        donationMapper.insert(new Donation(userName, money, date));
    }

    @Override
    public List<Donation> getDonationList(Integer year, Integer month) {
        return donationMapper.selectDonationList(year, month);
    }

    @Override
    public void updateQRCode(String pic) {
        staticValueMapper.setValue(DONATION_QRCODE_URL_KEY, pic);
    }

    @Override
    public String getQRCode() {
        return staticValueMapper.getValue(DONATION_QRCODE_URL_KEY);
    }
}
