package com.zereao.apphunter.pojo.vo;

import com.zereao.apphunter.pojo.entity.AppInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Zereao
 * @version 2019/05/10 14:39
 */
@Data
@Builder
public class AppPriceInfoVO {
    private Integer upNum;
    private Integer downNum;
    private String mailTime;
    private Map<String, List<AppInfo>> upPriceMap;
    private Map<String, List<AppInfo>> downPriceMap;
}
