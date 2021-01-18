package com.jefff.exercise.utility;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public class ArgParser {

    public static final String DEFAULT_DELIVERY_FILE = "delivery.csv";
    public static final String DEFAULT_PLACEMENTS_FILE = "placements.csv";
    private String deliveryFileName;
    private String placementFileName;
    private String queryFileName;
    String[] args;


    public ArgParser(String[] args) {
        this.args = args;
        deliveryFileName = DEFAULT_DELIVERY_FILE;
        placementFileName = DEFAULT_PLACEMENTS_FILE;
        queryFileName = null;
    }

    public boolean process() {
        int numArgs = args.length;
        for (int i = 0; i < numArgs; i++) {
            String arg = args[i];
            switch (arg) {
                case "-h":
                    printUsage();
                    return false;

                case "-df":
                    if (i + 1 >= numArgs) {
                        System.err.println("Missing -df argument");
                        printUsage();
                        return false;
                    }
                    deliveryFileName = args[++i];
                    break;

                case "-pf":
                    if (i + 1 >= numArgs) {
                        System.err.println("Missing -pf argument");
                        printUsage();
                        return false;
                    }
                    placementFileName = args[++i];
                    break;

                case "-qf":
                    if (i + 1 >= numArgs) {
                        System.err.println("Missing -qf argument");
                        printUsage();
                        return false;
                    }
                    queryFileName = args[++i];
                    break;

                default:
                    System.err.printf("Unexpected argument: %s\n", arg);
                    System.err.println("Extra command line arguments.");
                    printUsage();
                    return false;
            }
        }

        log.info("ArgumentSummary");
        log.info("  placementFileName = {}", placementFileName);
        log.info("  deliveryFileName  = {}", deliveryFileName);
        log.info("  queryFileName     = {}", queryFileName == null ? "<null>" : queryFileName);
        log.info("");
        return true;
    }

    private void printUsage() {
        System.err.println("Usage: |-df deliveryFileName| |-pf placementFileName| |-qf queryFileName| |-h|");
        System.err.printf("  deliveryFileName:     default = %s\n", DEFAULT_DELIVERY_FILE);
        System.err.printf("  placementFileName:    default = %s\n", DEFAULT_PLACEMENTS_FILE);
        System.err.printf("  queryFileName:        default = %s\n", "<null>");
    }


}
