package mapper;

import models.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface OrderMapper {
// 生成订单
    @Insert("insert into `order` (id,pid,userid) values (#{id},#{pid},#{userid})")
    int insert(Order order);
}
