package org.example.proxy;

import org.example.annotations.Log;

public class TestLoggingImpl implements TestLoggingInterface {
    @Log
    @Override
    public void calculation(int param) {}

    @Override
    public void calculation(int param1, int param2) {}
    @Log
    @Override
    public void calculation(String param1, String param2) {}

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {}
}
