package com.child.common.utils;

import com.child.common.entity.enums.ChildVaccineEnum;

import java.util.Arrays;
import java.util.Optional;

public class ChildVaccineUtil {
    /**
     * 通过疫苗描述文本匹配枚举
     * @param vaccineDesc 疫苗描述（如"第一针乙肝"）
     * @return 匹配的枚举，无匹配则返回空
     */
    public static Optional<ChildVaccineEnum> getEnumByVaccineDesc(String vaccineDesc){
        return Arrays.stream(ChildVaccineEnum.values())
                .filter(item->item.getVaccine().equals(vaccineDesc))
                .findFirst();
    }

    /**
     * 从疫苗描述中提取疫苗类型（如从"第一针乙肝"提取"乙肝"）
     * @param vaccineDesc 疫苗描述
     * @return 疫苗类型简称
     */
    public static String getVaccineTypeFromDesc(String vaccineDesc){
        if (vaccineDesc.contains("针")){
            return vaccineDesc.split("针")[1];
        }
        return "";
    }

    public static Integer getNeedleNumFromDesc(String vaccineDesc){
        if (vaccineDesc.contains("第") && vaccineDesc.contains("针")){
            String numStr = vaccineDesc.replaceAll("[^0-9]", "");
            return numStr.isEmpty() ? 0 : Integer.parseInt(numStr);
        }
        return 0;
    }
}
