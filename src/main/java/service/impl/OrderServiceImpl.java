package service.impl;

import mapper.OrderMapper;
import mapper.ProductMapper;
import models.Order;
import models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.OrderService;

import java.util.Random;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderMapper orderMapper;
    @Override
    public void reduceStock(int id) throws Exception {
// 1.获取库存
        Product product = productMapper.getProduct(id);
// 模拟网络延迟
        Thread.sleep(1000);
        if(product.getStock() <= 0)
            throw new RuntimeException("已抢光！");
// 2.减库存
        int i = productMapper.reduceStock(id);
        if(i == 1){
            Order order = new Order();
            order.setId(new Random().nextInt(100000));
            order.setPid(id);
            order.setUserid(101);
            orderMapper.insert(order);
        }else
            throw new RuntimeException("减库存失败，请重试！");
    }
}