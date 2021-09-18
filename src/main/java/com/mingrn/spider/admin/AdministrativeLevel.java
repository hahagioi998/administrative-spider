package com.mingrn.spider.admin;

/**
 * 行政级别
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2020/4/28 15:43
 */
public enum AdministrativeLevel {

    /** 省 */
    PROVINCE("province", new Province(), new City()),

    /** 市 */
    CITY("city", new City(), new County()),

    /** 区县 */
    COUNTY("county", new County(), new Town()),

    /** 乡镇 */
    TOWN("town", new Town(), new Village()),

    /** 行政村, 居委会 */
    VILLAGE("village", new Village(), null);

    private final String className;

    private final Administrative level;

    private final Administrative next;

    AdministrativeLevel(String className, Administrative level, Administrative next) {
        this.className = className;
        this.level = level;
        this.next = next;
    }

    public String getClassName() {
        return className;
    }

    public Administrative getLevel() {
        return level;
    }

    public Administrative getNext() {
        return next;
    }
}
