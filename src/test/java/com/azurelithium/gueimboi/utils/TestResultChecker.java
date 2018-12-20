package com.azurelithium.gueimboi.utils;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class TestResultChecker implements Callable<Boolean> {

    private final int TEST_RESULT_CHECK_MS_DELAY = 1000;

    private StringBuilder serialContent;

    public TestResultChecker(StringBuilder _serialContent) {
        serialContent = _serialContent;
    }

    @Override
    public Boolean call() {
        while(true) {
            if (Pattern.compile(Pattern.quote("passed"), Pattern.CASE_INSENSITIVE)
                .matcher(serialContent.toString())
                .find()) {
                    return true;
            } else if (Pattern.compile(Pattern.quote("fail"), Pattern.CASE_INSENSITIVE)
                .matcher(serialContent.toString())
                .find()) {
                    return false;
            }
            try {
                Thread.sleep(TEST_RESULT_CHECK_MS_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}