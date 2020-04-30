package com.mingrn.spider.admin;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * 执行Main
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2020/4/29 11:59
 */
public class Main {

    static final String SEPARATOR = File.separator;

    static final String GOV_INDEX = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2019/index.html";

    public static void main(String[] args) throws IOException {

        String fileName = "Administrative.json";
        String fullPath = args.length > 0 ? args[0] + SEPARATOR : System.getProperty("user.home") + SEPARATOR + "Downloads" + SEPARATOR;

        File file = new File(fullPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(fullPath + fileName);
        if (!file.exists()) {
            file.createNewFile();
            System.out.println("Create New File: [" + fullPath + fileName + "]");
        }
        file.setWritable(true);

        ObjectMapper objectMapper = new ObjectMapper();

        Spider spider = new SpiderExecutor();
        List<Node> nodes = spider.execute(AdministrativeLevel.PROVINCE.level(), GOV_INDEX);
        System.out.println(nodes.toString());

        String asString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(nodes);
        System.out.println(asString);

        FileWriter writer = new FileWriter(file);
        writer.write(asString);
        writer.flush();
        writer.close();
    }
}