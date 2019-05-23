package com.zereao.apphunter;

import com.zereao.apphunter.pojo.entity.AppInfo;
import com.zereao.apphunter.service.AppInfoService;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zereao
 * @version 2019/05/09 20:28
 */
public class TestUtils {
    @Test
    public void test02() {
        String url = "https://itunes.apple.com/cn/app/%E5%8F%8C%E5%AD%90-gemini/id1159266744?mt=8";
//        String result = OkHttp3Utils.doGet(url);
//        System.out.println(result);
        AppInfo info = new AppInfoService().parseAppInfo(url);
        System.out.println(info.toString());

    }

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
