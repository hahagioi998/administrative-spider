package com.mingrn.spider.admin;

/**
 * 行政级别
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2020/4/28 15:43
 */
public enum AdministrativeLevel {

    /** 省 */
    PROVINCE("province"),

    /** 市 */
    CITY("city"),

    /** 区县 */
    COUNTY("county"),

    /** 乡镇 */
    TOWN("town"),

    /** 行政村, 居委会 */
    VILLAGE("village");

    public String level;

    AdministrativeLevel(String level) {
        this.level = level;
    }

    public Administrative level() {

        switch (this) {
            case PROVINCE:
                return new Province();
            case CITY:
                return new City();
            case COUNTY:
                return new County();
            case TOWN:
                return new Town();
            default:
                return new Village();
        }
    }

    public Administrative nextLevel() {

        switch (this) {
            case PROVINCE:
                return new City();
            case CITY:
                return new County();
            case COUNTY:
                return new Town();
            case TOWN:
                return new Village();
            default:
                return null;
        }
    }
}
