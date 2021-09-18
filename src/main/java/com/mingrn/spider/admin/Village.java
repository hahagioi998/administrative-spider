package com.mingrn.spider.admin;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Village implements Administrative {

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

            String code = tds.get(0).text();
            String name = tds.get(2).text();
            System.out.println(code + " ---- " + name);

            Node node = new Node();
            node.setCode(code);
            node.setName(name);
            nodes.add(node);
        }
        return nodes;
    }

    @Override
    public AdministrativeLevel level() {
        return AdministrativeLevel.VILLAGE;
    }
}
