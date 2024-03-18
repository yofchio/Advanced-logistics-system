package com.example.customerservicecentre;

import com.example.customerservicecentre.entity.Customer;
import com.example.customerservicecentre.entity.Orders;
import com.example.customerservicecentre.mapper.CustomerMapper;
import com.example.customerservicecentre.service.OrderService;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerServiceCentreApplicationTests {
  @Autowired
  private OrderService orderService;
  @Autowired
  private CustomerMapper customerMapper;
  @Test
  void contextLoads() {

  }
  @Test
  public void testlgs()
  {
//        根据id查询
    Customer user = customerMapper.selectById("1");
    System.out.println("乐观锁："+user);
//    Scanner sc  = new Scanner(System.in);
//    sc.nextInt();
    user.setName("乐观锁");
    Integer row = customerMapper.updateById(user);
  }
}
