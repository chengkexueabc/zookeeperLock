package controller;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.OrderService;

import java.util.function.IntPredicate;

@Controller
public class ProductAction {
    @Autowired
    private OrderService orderService;
    private static String connectString  = "localhost:2181,localhost:2182,localhost:2183";
    @GetMapping("/product/reduce")
    @ResponseBody
    public Object reduceStock(int id) throws Exception{
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
        client.start();
        InterProcessMutex lock = new InterProcessMutex(client, "/product_"+id);
        try {
            lock.acquire();
            orderService.reduceStock(id);
        }catch(Exception e){
            if(e instanceof RuntimeException){
                throw e;
            }
        }finally{
            lock.release();
        }
        return "ok";
    }
}
