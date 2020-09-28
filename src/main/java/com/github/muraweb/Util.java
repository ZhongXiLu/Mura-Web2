package com.github.muraweb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class Util {

    /**
     * Get the repository name and its owner from the repository url.
     *
     * @param repoLink The GitHub repository url.
     * @return The repository name.
     */
    public static String getRepoName(String repoLink) {
        // Strip trailing slashes
        while (repoLink.charAt(repoLink.length() - 1) == '/') {
            repoLink = repoLink.substring(0, repoLink.length() - 1);
        }

        String[] parts = repoLink.split("/");

        return parts[parts.length - 2] + "/" + parts[parts.length - 1];
    }

    /**
     * Apply optimal weights to a generated report of MuRa.
     *
     * @param report The file path to the generated report by MuRa.
     */
    public static void applyOptimalWeights(String report) throws IOException {
        Properties optimalWeights = new Properties();
        optimalWeights.load(new FileInputStream("data/optimal_weights.txt"));

        Scanner scanner = new Scanner(new File(report));
        StringBuilder buffer = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.endsWith("// initial value")) {    // replace initial values for the weights
                String[] parts = line.split(" ");
                double weightValue = Double.parseDouble(optimalWeights.getProperty(parts[1]));
                parts[3] = weightValue + ";";
                buffer.append(String.join(" ", parts)).append(System.lineSeparator());
            } else {
                buffer.append(line).append(System.lineSeparator());
            }
        }
        scanner.close();

        FileWriter writer = new FileWriter(report);
        writer.write(buffer.toString());
        writer.close();
    }

}
