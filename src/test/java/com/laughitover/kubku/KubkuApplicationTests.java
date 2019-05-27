package com.laughitover.kubku;

import com.laughitover.kubku.commonUtils.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KubkuApplicationTests {

    @Test
    public void contextLoads() {

    }

    @Test
    public void testSplit(){
        String str="java,c,python";
        List<String> lists = StringUtil.splitString(str,",");
        System.out.println(lists);
    }

    @Test
    public void testArray(){
        String segString = StringUtil.headUpperCase("asd");
        System.out.println(segString);
    }
}
