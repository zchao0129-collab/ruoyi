package com.ruoyi.common.utils.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * 功能描述
 *  根据 signKeys 中的参数值  按ASCII 字符排序 生成字符串   StringA=ValueA&StringB=ValueB
 * @author: scott
 * @date: 2023年04月17日 9:21
 */
public class SignStrUtil {
    public static String getSignStr(Map<String,Object> map,String[] signKeys){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(null != entry.getValue() && !"".equals(entry.getValue()) && Arrays.asList(signKeys).contains(entry.getKey())){
                list.add(entry.getKey() + "=" + entry.getValue());
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
            if (i != size - 1){
                sb.append("&");
            }
        }
        String result = sb.toString();
        return result;
    }
}
