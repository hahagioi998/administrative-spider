package com.mingrn.spider.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class Province implements Administrative {

    static final String SEPARATOR = File.separator;

    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private final ThreadFactory nameFactory = new ThreadFactoryBuilder().setNameFormat("spider-pool-%d").build();

    private final ExecutorService executor = new ThreadPoolExecutor(
            CPU_COUNT * 2,
            (CPU_COUNT + 1) * 2,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            nameFactory,
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    @Override
    public List<Node> parsingHtml(Document html, String url) {
        if (Objects.isNull(html)) {
            return null;
        }

        Elements tables = html.getElementsByClass(getClassName() + TABLE);
        if (tables.isEmpty()) {
            return null;
        }

        List<Future<Node>> futures = new ArrayList<>();

        Element table = tables.get(0);
        Elements trs = table.getElementsByClass(getClassName() + TR);

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

                    List<Node> children = parsingNextLevelHtml(nextUrl);
                    node.setChildren(children);
                    return node;
                });
                futures.add(future);
            }
        }

        // wait all thread is done
        return threadAllDone(futures);
    }

    @Override
    public AdministrativeLevel level() {
        return AdministrativeLevel.PROVINCE;
    }

    private List<Node> threadAllDone(List<Future<Node>> futures) {

        List<Node> nodes = new ArrayList<>();

        do {
            Iterator<Future<Node>> iterator = futures.iterator();
            while (iterator.hasNext()) {
                final Future<Node> future = iterator.next();
                if (future.isDone()) {
                    try {
                        write2File(future.get());
                        iterator.remove();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                if (future.isCancelled()) {
                    iterator.remove();
                }
            }

        } while (futures.size() != 0);

        try {
            if (!executor.awaitTermination(10L, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return nodes;
    }

    private void write2File(Node node) {
        if (Objects.nonNull(node)) {

            String writePath = System.getenv("write.path");
            if (writePath == null || "".equals(writePath)) {
                writePath = "/data";
            }

            String fileName = writePath + SEPARATOR + node.getCode() + ".json";

            File file = new File(fileName);
            if (!file.exists()) {
                try {
                    if (file.createNewFile() && file.setWritable(true)) {
                        System.out.println("Create New File: [" + fileName + "]");
                    }
                } catch (IOException e) {
                    System.out.println("Create New File [" + fileName + "] Fail");
                    e.printStackTrace();
                }
            }

            ObjectWriter objectWriter = OBJECT_MAPPER.writerWithDefaultPrettyPrinter();

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(objectWriter.writeValueAsString(node));
                writer.flush();
                System.out.println("Write JSON to File [" + fileName + "] Success");
            } catch (IOException e) {
                System.out.println("Write JSON to File [" + fileName + "] Fail");
                e.printStackTrace();
            }
        }
    }
}
