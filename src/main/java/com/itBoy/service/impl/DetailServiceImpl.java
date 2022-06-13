package com.itBoy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itBoy.entity.Detail;
import com.itBoy.mapper.DetailMapper;
import com.itBoy.service.DetailService;
import org.springframework.stereotype.Service;

@Service
public class DetailServiceImpl extends ServiceImpl<DetailMapper, Detail> implements DetailService {
}
