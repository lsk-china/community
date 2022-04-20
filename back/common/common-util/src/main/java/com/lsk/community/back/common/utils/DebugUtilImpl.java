package com.lsk.community.back.common.utils;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DebugUtilImpl extends DebugUtil{
    public void printMap(String prefix, Map<?, ?> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(": ");
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            sb.append(entry.getKey().toString());
            sb.append(": ");
            sb.append(entry.getValue().toString());
            sb.append(" ");
        }
        log.info(sb.toString());
    }
}
