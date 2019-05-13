package com.zereao.apphunter;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zereao
 * @version 2019/05/09 20:28
 */
public class TestUtils {
    @Test
    public void test01() {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            String is = String.valueOf(i);
            map.put(is, is + is);
        }
        System.out.println(map.entrySet());
        System.out.println("===================================================");
        System.out.println(map.entrySet());

    }

}
