package com.github.zshine.auth.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zshine.auth.constant.Constants;
import com.github.zshine.auth.constant.enums.StatusEnum;
import com.github.zshine.auth.dao.RouteDao;
import com.github.zshine.auth.domain.Route;
import com.github.zshine.auth.service.RouteService;
import com.github.zshine.common.valid.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RouteServiceImpl implements RouteService {

    @Resource
    private StreamBridge streamBridge;

    @Resource
    private RouteDao routeDao;


    @Override
    public void updateStatus(String id, StatusEnum statusEnum) {
        Route route = this.getAndCheckNullById(id);
        route.setStatus(statusEnum);
        routeDao.updateById(route);
        this.refresh();
    }


    @Override
    public void delete(String id) {
        Route route = this.getAndCheckNullById(id);
        routeDao.removeById(route);
        this.refresh();
    }

    @Override
    public void add(Route route) {
        routeDao.save(route);
        this.refresh();
    }

    @Override
    public void edit(Route route) {
        this.getAndCheckNullById(route.getId());
        routeDao.updateById(route);
        this.refresh();
    }


    /**
     * 刷新网关
     */
    private void refresh() {
        log.info("刷新网关数据");
        ObjectMapper objectMapper = new ObjectMapper();

        List<String> routes = new ArrayList<>();

        for (Route route : routeDao.list(Wrappers.<Route>lambdaQuery()
                .eq(Route::getStatus, StatusEnum.EFFECT))) {
            try {
                routes.add(objectMapper.writeValueAsString(route));
            } catch (JsonProcessingException e) {
                log.error("序列化网关: {}", e.getMessage());
                routes = new ArrayList<>();
            }
        }
        if (routes.isEmpty()) {
            log.error("动态网关数据为空");
        } else {
            log.info("广播网关消息：{}", routes);
            boolean response = streamBridge.send("route-out-0", routes);
            if (!response) {
                log.error("广播网关消息失败");
            }
        }
    }


    private Route getAndCheckNullById(String id) {
        Route route = routeDao.getById(id);
        AssertUtil.notNull(route, Constants.NULL_ERROR_MESSAGE);
        return route;
    }

}
