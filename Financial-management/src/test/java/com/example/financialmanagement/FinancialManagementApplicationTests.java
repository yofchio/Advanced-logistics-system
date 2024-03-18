package com.example.financialmanagement;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class FinancialManagementApplicationTests {

  @Autowired
  private RedisTemplate redisTemplate;


}
