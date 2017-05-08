package com.sjb.environment.singleton;

import com.sjb.environment.util.Constants;

import java.io.Serializable;

/**
 * Created by SLB on 16/2/24.
 */
public class CommonSingleton implements Serializable {

    public String userCode = Constants.CommenValue.USERCODEDEFAULT;
    public String token = "";

    private String currentCompanyName = "";

    public String getCurrentCompanyName() {
        return currentCompanyName;
    }

    public void setCurrentCompanyName(String currentCompanyName) {
        this.currentCompanyName = currentCompanyName;
    }

    private static class SingletonHolder {
      /**
         * 单例对象实例
        */
        static final CommonSingleton INSTANCE = new CommonSingleton();
    }

    public static CommonSingleton getInstance() {
       return SingletonHolder.INSTANCE;
   }



    /**
     * private的构造函数用于避免外界直接使用new来实例化对象
     */
    protected CommonSingleton() {

    }

    /**
    * readResolve方法应对单例对象被序列化时候
    */
    private Object readResolve() {
        return getInstance();
    }


    /**
     * 校验风机台数的方法
     * @param org 接口传回的风机台数原始值
     * @return 校验后的风机台数，要求在0~999之间且为int型
     */
    private String verifyLatheValue(String org){
        String temp = "";
        if (org.contains(".")){
            temp = org.substring(0, org.indexOf("."));
        }else{
            temp = org;
        }
        int org_int = 0;
        try {
            org_int = Integer.parseInt(temp);
        } catch (NumberFormatException e) {
            return Constants.CommenValue.NUM_OF_WIND_DRIVE_GENERATOR_MAX + "";
        }
        if (org_int > Constants.CommenValue.NUM_OF_WIND_DRIVE_GENERATOR_MAX){
            return Constants.CommenValue.NUM_OF_WIND_DRIVE_GENERATOR_MAX + "";
        }else if (org_int < Constants.CommenValue.ZERO_INT){
            return Constants.CommenValue.ZERO_INT + "";
        }else {
            return org_int + "";
        }
    }

    /**
     * 校验年计划完成率的方法
     * @param percent 接口传回的年计划完成率的值
     * @return 校验后的结果,范围在0~999.99之间
     */
//    private String verifyPercent(String percent){
//        String percentNum = "";
//        if (percent.endsWith("%")){
//            percentNum = percent.substring(0, percent.length() - 1);
//        }else{
//            percentNum = percent;
//        }
//        return getSuitableNumber(percentNum, Constants.CommenValue.ZERO_DOUBLE, Constants.CommenValue.DONEPERCENT_MAX)+ "";
//    }

    /**
     * 校验并修改发电量值的方法
     * @param electricity 原始发电量
     * @param type 不同类型的发电量的区分类，0是日发电量，1是月发电量，2是年发电量
     * @return 修改完的电量
     */
//    private String verifyOutputElectricity(String electricity, int type){
//        double max = -0;
//        switch (type){
//            case Constants.CommenValue.DAILY_OUTPUT_ELECTRICITY_TYPE:
//                max = Constants.CommenValue.DAILY_OUTPUT_ELECTRICITY_MAX;
//                break;
//            case Constants.CommenValue.MONTHLY_OUTPUT_ELECTRICITY_TYPE:
//                max = Constants.CommenValue.MONTHLY_OUTPUT_ELECTRICITY_MAX;
//                break;
//            case Constants.CommenValue.ANNUAL_OUTPUT_ELECTRICITY_TYPE:
//                max = Constants.CommenValue.ANNUAL_OUTPUT_ELECTRICITY_MAX;
//                break;
//        }
//        return  getSuitableNumber(electricity, Constants.CommenValue.ZERO_DOUBLE , max) + "" ;
//    }

    /**
     * 校验并修改平均风速的方法
     * @param wideAvgSpeed 原始平均风速
     * @return 修改完的平均风速
     */
//    private String verifyAvgWideSpeed(String wideAvgSpeed){
//        return getSuitableNumber(wideAvgSpeed, Constants.CommenValue.ZERO_DOUBLE, Constants.CommenValue.WIDEAVGSPEED_MAX) + "";
//    }

    /**
     * 校验并修改实时功率
     * @param outputRate 原有的实时功率
     * @return 修改完的实时功率
     */
//    private String verifyOutputRate(String outputRate){
//        return getSuitableNumber(outputRate, Constants.CommenValue.OUTPUTRATE_MIN, Constants.CommenValue.OUTPUTRATE_MAX) + "";
//    }

    /**
     * 判断一个字符串中的数字是否在min和max之间的方法
     * @param orgString 待判断的字符串
     * @param min 取值的最小值
     * @param max 取值的最大值
     * @return 是否在范围之内
     */
    private boolean ifStringRight(String orgString, double min , double max){
        double org_double = 0;
        try {
            org_double = Double.parseDouble(orgString);
        } catch (NumberFormatException e) {
            return false;
        }
        if (org_double >= min && org_double <= max){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获得合适的数值
     * @param orgString 原始值
     * @param min 最小值
     * @param max 最大值
     * @return 合适的字符串（min和max之间，且保留两位小数）
     */
//    private String getSuitableNumber(String orgString, double min, double max){
//        double org_double = 0;
//        String returnStr = "";
//        try {
//            org_double = Double.parseDouble(orgString);
//        } catch (NumberFormatException e) {
//            returnStr = max + "";
//        }
//
////        org_double = org_double * getRadomDouble() + org_double;
//
//        if (org_double < min){
//            returnStr = min + "";
//        }else if (org_double > max){
//            returnStr = max + "";
//        }else {
//            returnStr = org_double + "";
//        }
//        return keepTowDecimal(returnStr, keepTowDecimal((max + ""), "0.00"));
//    }

//    private double getRadomDouble(){
//        double max = 0.2;
//        double min = -0.2;
//        double resule = Math.random() * (max - min) + min;
////        Log.i("123456",  "随机系数---------》》》》》》" + resule);
//        return resule;
//    }

    /**
     * 保留两位小数
     * @param num 原始数据
     * @param defaultNum 默认数据
     * @return 保留两位的小数后默认值
     */
//    public String keepTowDecimal(String num, String defaultNum) {
//        String result = "0";
//        if(num.matches(Constants.TWODECMATCH)) {
//            try {
//                result = String.format("%.2f", Double.parseDouble(num));
//            } catch (NumberFormatException e) {
//                result = defaultNum;
//            }
//        } else {
//            result = defaultNum;
//        }
//        return  result;
//    }


}

