package com.mingrn.spider.admin;

import java.util.List;

public interface Spider {

    List<Node> execute(Administrative administrative, String url);
}