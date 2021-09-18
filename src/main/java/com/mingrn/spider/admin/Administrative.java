package com.mingrn.spider.admin;

import org.jsoup.nodes.Document;

import java.util.List;

/**
 * 解析行政区划 HTML
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2020/4/29 13:24
 */
public interface Administrative {

    String TABLE = "table";

    String TD = "td";

    String TR = "tr";

    String A = "a";

    String A_HREF = "href";

    /**
     * 解析 HTML
     *
     * @param html html Document
     * @param url  Request Url For Document
     * @return Nodes
     */
    List<Node> parsingHtml(Document html, String url);

    /**
     * 行政区划级别
     *
     * @return AdministrativeLevel
     */
    AdministrativeLevel level();

    /**
     * 获取下级行政区划
     *
     * @return Administrative
     */
    default Administrative getNext() {
        return level().getNext();
    }

    /**
     * 获取行政区划 ClassName
     *
     * @return className
     */
    default String getClassName() {
        return level().getClassName();
    }


    /**
     * 解析下级行政区划
     *
     * @param nextLevelUrl 下级行政区划 URL
     * @return list
     */
    default List<Node> parsingNextLevelHtml(String nextLevelUrl) {
        return new SpiderExecutor().execute(getNext(), nextLevelUrl);
    }
}
