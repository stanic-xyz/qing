package com.stan.zhangli.rpc;

import org.springframework.stereotype.Service;

@Service
public class TestImpl implements ITest {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}