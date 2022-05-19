package com.vmo.core.configs.http;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CacheServletInputStream extends ServletInputStream {
    private int lastIndexRetrieved = 0;
    private final ByteArrayInputStream in;
    private final int le;
    private final String content;

    public CacheServletInputStream(ByteArrayInputStream in, int le, String content) {
        this.in = in;
        this.le = le;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean isFinished() {
        return lastIndexRetrieved == le;
    }

    @Override
    public boolean isReady() {
        return lastIndexRetrieved < le;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
    }

    @Override
    public int read() throws IOException {
        lastIndexRetrieved++;
        return in.read();
    }
}
