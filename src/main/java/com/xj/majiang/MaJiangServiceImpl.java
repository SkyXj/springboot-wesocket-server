package com.xj.majiang;

import com.xj.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MaJiangServiceImpl implements MaJiangService{

    private Map<Integer, User> userMap=new HashMap<>();

    @Override
    public List<Integer> fapai(int userid) {

        return null;
    }
}
