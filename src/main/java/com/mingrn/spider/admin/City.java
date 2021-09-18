package com.mingrn.spider.admin;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class City implements Administrative {

    @Override
    public List<Node> parsingHtml(Document html, String url) {
        if (Objects.isNull(html)) {
            return null;
        }
        Elements tables = html.getElementsByClass(getClassName() + TABLE);
        if (tables.isEmpty()) {
            return null;
        }

        List<Node> nodes = new LinkedList<>();
        Element table = tables.get(0);
        Elements trs = table.getElementsByClass(getClassName() + TR);
        for (Element tr : trs) {
            Elements tds = tr.getElementsByTag(TD);
            if (tds.size() == 0) {
                continue;
            }
            Element codeElement = tds.get(0);
            Element textElement = tds.get(1);

            Elements codeA = codeElement.getElementsByTag(A);
            Elements textA = textElement.getElementsByTag(A);

            final String href = codeA.attr(A_HREF);

            String code = codeA.text();
            String name = textA.text();

            if (code == null || "".equals(code)) {
                code = codeElement.text();
            }
            if (name == null || "".equals(name)) {
                name = textElement.text();
            }

            System.out.println(code + " ---- " + name);

            Node node = new Node();
            node.setCode(code);
            node.setName(name);

            if (href != null && !"".equals(href)){
                String nextUrl = url;
                nextUrl = nextUrl.substring(0, url.lastIndexOf("/") + 1);
                nextUrl += href;
                List<Node> children = parsingNextLevelHtml(nextUrl);;
                node.setChildren(children);
            }

            nodes.add(node);
        }
        return nodes;
    }

    @Override
    public AdministrativeLevel level() {
        return AdministrativeLevel.CITY;
    }
}
