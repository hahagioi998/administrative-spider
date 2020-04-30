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
}
