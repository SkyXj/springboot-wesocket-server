package com.xj.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xj.entity.Room;
import com.xj.mapper.RoomMapper;
import com.xj.service.RoomService;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

}
