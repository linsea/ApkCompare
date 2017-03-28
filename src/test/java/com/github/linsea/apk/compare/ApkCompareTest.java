package com.github.linsea.apk.compare;
import org.junit.Test;

public class ApkCompareTest {

    @Test
    public void test() {
        String[] args = new String[]{
                "/home/andy/tmp/yymobile_client-6.0.0-SNAPSHOT-43116-official.apk",
                "/home/andy/tmp/yymobile_client-6.0.5-SNAPSHOT-43366-official.apk",
                "changes"};
        Main.main(args);
    }
}
