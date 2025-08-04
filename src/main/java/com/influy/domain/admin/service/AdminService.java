package com.influy.domain.admin.service;

import com.influy.domain.admin.dto.AdminRequestDTO;
import com.influy.domain.item.entity.Item;
import com.influy.domain.managerProfile.entity.ManagerProfile;
import com.influy.domain.member.converter.MemberConverter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.sellerProfile.converter.SellerProfileConverter;
import com.influy.domain.sellerProfile.entity.SellerProfile;

import java.util.List;

public interface AdminService {

    Member joinAdmin(MemberRequestDTO.SellerJoin request);

    List<Item> copyItemToSeller(List<Long> itemIds, Long sellerIds);
}
