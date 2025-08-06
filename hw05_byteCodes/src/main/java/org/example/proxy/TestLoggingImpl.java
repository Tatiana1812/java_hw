package org.example.proxy;

import org.example.annotations.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLoggingImpl implements TestLoggingInterface {
    @Log
    @Override
    public void calculation(int param) {}

    @Override
    public void calculation(int param1, int param2) {}

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {}
}
