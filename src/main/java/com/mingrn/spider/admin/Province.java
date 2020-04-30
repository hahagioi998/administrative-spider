package com.mingrn.spider.admin;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class Province implements Administrative {

    final AdministrativeLevel PROVINCE = AdministrativeLevel.PROVINCE;

    private static int cpu_count = Runtime.getRuntime().availableProcessors();

    private ThreadFactory nameFactory = new ThreadFactoryBuilder().setNameFormat("spider-pool-%d").build();

    private ExecutorService executor = new ThreadPoolExecutor(cpu_count * 2, (cpu_count + 1) * 2, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), nameFactory, new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public List<Node> parsingHtml(Document html, String url) {
        if (Objects.isNull(html)) {
            return null;
        }

        Elements tables = html.getElementsByClass(PROVINCE.level + TABLE);
        if (tables.isEmpty()) {
            return null;
        }

        List<Future<Node>> futures = new ArrayList<>();

        Element table = tables.get(0);
        Elements trs = table.getElementsByClass(PROVINCE.level + TR);
        for (Element tr : trs) {
            Elements tds = tr.getElementsByTag(TD);
            for (Element td : tds) {

                Future<Node> future = executor.submit(() -> {
                    Elements a = td.getElementsByTag(A);
                    if (a.isEmpty()) {
                        return null;
                    }
                    final String href = a.attr(A_HREF);

                    String name = a.text();
                    StringBuilder code = new StringBuilder(href);
                    if (href.contains("/")) {
                        code = new StringBuilder(href.substring(href.lastIndexOf("/")));
                    }
                    code = new StringBuilder(code.substring(0, code.lastIndexOf(".")));
                    int len = code.length();
                    while (len < 12) {
                        code.append("0");
                        len = code.length();
                    }
                    System.out.println(code + " ---- " + name);

                    String nextUrl = url;
                    nextUrl = nextUrl.substring(0, url.lastIndexOf("/") + 1);
                    nextUrl += href;

                    Node node = new Node();
                    node.setCode(code.toString());
                    node.setName(name);

                    List<Node> children = new SpiderExecutor().execute(PROVINCE.nextLevel(), nextUrl);
                    node.setChildren(children);
                    return node;
                });
                futures.add(future);
            }
        }

        // wait all thread is done
        return threadAllDone(futures);
    }

    private List<Node> threadAllDone(List<Future<Node>> futures) {
        List<Node> nodes = new ArrayList<>();
        while (true) {
            boolean isAllDown = true;
            for (Future<Node> future : futures) {
                isAllDown &= (future.isDone() || future.isCancelled());
            }

            if (isAllDown) {
                for (Future<Node> future : futures) {
                    try {
                        nodes.add(future.get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
        return nodes;
    }
}
