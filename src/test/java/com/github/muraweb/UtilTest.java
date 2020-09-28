package com.github.muraweb;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    public void getRepoNameTest() {
        assertEquals("OpenFeign/feign", Util.getRepoName("https://github.com/OpenFeign/feign"));
        assertEquals("ZhongXiLu/LuMutator", Util.getRepoName("https://github.com/ZhongXiLu/LuMutator"));
        assertEquals("ZhongXiLu/MuRa-Web", Util.getRepoName("https://github.com/ZhongXiLu/MuRa-Web"));

        assertEquals("ZhongXiLu/MuRa-Web", Util.getRepoName("https://github.com/ZhongXiLu/MuRa-Web/"));
        assertEquals("ZhongXiLu/MuRa-Web", Util.getRepoName("https://github.com/ZhongXiLu/MuRa-Web///"));
    }

    @Test
    public void applyOptimalWeightsTest() throws IOException {
        String report = getClass().getClassLoader().getResource("report.html").getFile();

        Scanner scanner = new Scanner(new File(report));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.endsWith("// initial value")) {
                String[] parts = line.split(" ");
                assertEquals("0.5;", parts[3]);
            }
        }
        scanner.close();

        Util.applyOptimalWeights(report);

        Properties optimalWeights = new Properties();
        optimalWeights.load(new FileInputStream(getClass().getClassLoader().getResource("optimal_weights.txt").getFile()));
        scanner = new Scanner(new File(report));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.endsWith("// initial value")) {
                String[] parts = line.split(" ");
                double weightValue = Double.parseDouble(optimalWeights.getProperty(parts[1]));
                assertEquals(weightValue + ";", parts[3]);
            }
        }
        scanner.close();
    }

}