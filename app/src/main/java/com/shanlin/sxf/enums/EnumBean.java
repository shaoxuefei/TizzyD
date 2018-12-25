package com.shanlin.sxf.enums;

/**
 * @author : SXF
 * @ date   : 2018/10/26
 * Description : 枚举测试
 */
public enum EnumBean {
    //这些类别每个都是一个该EnumBean的类、也就是多个EnumBean的对象、只是这些枚举定义的方式有点不同、类似于EnumBean enumbean;只不过枚举调用是直接可以点出来的，也就是类似于static的
    TAB01(0, "随性一笔"),
    TAB02(1, "SeekBar"),
    TAB03(2, "PopWindow-List"),
    TAB04(3, "gson/fastJson-特殊字符转译"),
    TAB05(4, "Rxjava-请求示例"),

    TAB06(5, "Pic选择"),

    TAB07(6, "DialogFragment"),

    TAB08(7, "跳转App"),

    TAB09(8, "事件分析"),

    TAB10(9, "重定项-URL2JS"),

    TAB11(10, "方法作用域"),

    TAB12(11, "语音播报");

    private String enumName;
    private int enumPosition;

    EnumBean(int position, String tab01) {
        enumName = tab01;
        enumPosition = position;
    }

    @Override
    public String toString() {
        EnumBean[] values = EnumBean.values();
        for (EnumBean enumBean : values) {
            if (enumBean.getEnumPosition() == 10) {
                return enumBean.getEnumName();
            }
        }
        return "";
    }


    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }

    public int getEnumPosition() {
        return enumPosition;
    }

    public void setEnumPosition(int enumPosition) {
        this.enumPosition = enumPosition;
    }


}
