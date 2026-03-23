package com.child.common.entity.enums;

public enum ChildVaccineEnum {

    BCGV_1(0,"第1针卡介苗"),
    HBV_1(0,"第1针乙肝"),
    HBV_2(1,"第2针乙肝"),
    OPV_1(2,"第1针脊髓灰质炎"),
    DTaP_1(2,"第1针百白破"),
    OPV_2(3,"第2针脊髓灰质炎"),
    DTaP_2(3,"第2针百白破"),
    OPV_3(4,"第3针脊髓灰质炎"),
    DTaP_3(4,"第3针百白破"),
    DTaP_4(5,"第3针百白破"),
    HBV_3(6,"第3针乙肝"),
    GACPV_1(6,"第1针A群流脑"),
    MMR_1(8,"第1针麻腮风"),
    JEVL_1(8,"第1针乙脑"),
    GACPV_2(9,"第2针A群流脑"),
    DTaP_5(18,"第4针百白破"),
    MMR_2(18,"第2针麻腮风"),
    HAV_1(18,"第1针甲肝"),
    JEVL_2(24,"第2针乙脑"),
    GACMV_1(36,"第1针AC群流脑"),
    OPV_4(48,"第4针脊髓灰质炎"),
    DT_1(72,"第1针白破"),
    GACMV_2(72,"第2针AC群流脑"),

    ;

    private Integer months;
    private String vaccine;

    ChildVaccineEnum(Integer months, String vaccine) {
        this.months = months;
        this.vaccine = vaccine;
    }

    public Integer getMonths() {
        return months;
    }
    public String getVaccine() {
        return vaccine;
    }
}
