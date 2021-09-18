package com.mingrn.spider.admin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SpiderExecutor implements Spider {

    private static final List<String> BROWSER_AGENT = new LinkedList<>();

    static {
        BROWSER_AGENT.add("Mozilla/5.0");
        BROWSER_AGENT.add("Safari/537.36");
        BROWSER_AGENT.add("AppleWebKit/537.36");
        BROWSER_AGENT.add("Chrome/81.0.4044.122");
    }

    @Override
    public List<Node> execute(Administrative administrative, String url) {

        try {
            // 随机沉睡
            int minVal = 1500, maxVal = 10000;
            int sleep = new Random().nextInt(maxVal) % (maxVal - minVal + 1) + minVal;

            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (administrative == null) {
            return null;
        }

        Document document = doExecute(url);

        // 死循环处理
        while (document == null || document.toString().contains("请开启JavaScript并刷新该页")) {
            try {
                // 随机沉睡
                int minVal = 3, maxVal = 20;
                int sleep = new Random().nextInt(maxVal) % (maxVal - minVal + 1) + minVal;
                System.out.println("请求失败, " + sleep + "s 后再次请求 ...");
                TimeUnit.SECONDS.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            document = doExecute(url);
        }

        return administrative.parsingHtml(document, url);
    }

    private Document doExecute(String url) {
        Document document = null;
        try {
            // 随机获取浏览器代理
            int nextInt = new Random().nextInt(100);
            int idx = nextInt < BROWSER_AGENT.size() ? nextInt : nextInt % BROWSER_AGENT.size();

            document = Jsoup.connect(url).timeout(Integer.MAX_VALUE).userAgent(BROWSER_AGENT.get(idx)).get();
        } catch (IOException e) {
            System.out.println("请求失败: [" + e.getMessage() + "], 重新请求... [" + url + "]");
        }
        return document;
    }
}
