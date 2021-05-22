package com.springbootutils.util;

import java.util.HashMap;
import java.util.Map;

public class RegionCodeMap {

    private static final Map<String, String> regionMap = new HashMap<>();

    static {
        regionMap.put("北京市", "110000000000");
        regionMap.put("天津市", "120000000000");
        regionMap.put("河北省", "130000000000");
        regionMap.put("山西省", "140000000000");
        regionMap.put("内蒙古自治区", "150000000000");
        regionMap.put("辽宁省", "210000000000");
        regionMap.put("吉林省", "220000000000");
        regionMap.put("黑龙江省", "230000000000");
        regionMap.put("上海市", "310000000000");
        regionMap.put("江苏省", "320000000000");
        regionMap.put("浙江省", "330000000000");
        regionMap.put("安徽省", "340000000000");
        regionMap.put("福建省", "350000000000");
        regionMap.put("江西省", "360000000000");
        regionMap.put("山东省", "370000000000");
        regionMap.put("河南省", "410000000000");
        regionMap.put("湖北省", "420000000000");
        regionMap.put("湖南省", "430000000000");
        regionMap.put("广东省", "440000000000");
        regionMap.put("广西壮族自治区", "450000000000");
        regionMap.put("海南省", "460000000000");
        regionMap.put("重庆市", "500000000000");
        regionMap.put("四川省", "510000000000");
        regionMap.put("贵州省", "520000000000");
        regionMap.put("云南省", "530000000000");
        regionMap.put("西藏自治区", "540000000000");
        regionMap.put("陕西省", "610000000000");
        regionMap.put("甘肃省", "620000000000");
        regionMap.put("青海省", "630000000000");
        regionMap.put("宁夏回族自治区", "640000000000");
        regionMap.put("新疆维吾尔自治区", "650000000000");
    }

    public static String getRegionCode(String province) {
        return regionMap.get(province);
    }
}
