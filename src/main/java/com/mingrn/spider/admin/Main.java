package com.mingrn.spider.admin;

/**
 * 执行Main
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2020/4/29 11:59
 */
public class Main {

    static final String GOV_INDEX = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2021/index.html";

    public static void main(String[] args) {

        System.out.println("================ 抓取国家统计局行政区划数据 ================");
        System.out.println("================      国家统计局URL      ================");
        System.out.println(GOV_INDEX);

        Spider spider = new SpiderExecutor();

        spider.execute(AdministrativeLevel.PROVINCE.getLevel(), GOV_INDEX);

        System.out.println("EXIT");
        System.exit(0);

    }
}
