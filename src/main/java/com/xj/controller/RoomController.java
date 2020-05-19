package com.xj.controller;

import com.xj.entity.Room;
import com.xj.service.RoomService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value="房间",tags={"房间"})
@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    RoomService roomService;

    @GetMapping("/getRooms")
    private List<Room> getUser(){
        List<Room> rooms = roomService.selectList(null);
        return rooms;
    }
}
