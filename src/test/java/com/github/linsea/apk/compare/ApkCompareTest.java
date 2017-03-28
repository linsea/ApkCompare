package com.github.linsea.apk.compare;
import org.junit.Test;

public class ApkCompareTest {

    @Test
    public void test() {
        String[] args = new String[]{
                "/home/andy/tmp/app-6.0.0.apk",
                "/home/andy/tmp/app-6.0.5.apk",
                "changes"};
        Main.main(args);
    }
}
