package toolbox;
import java.util.HashMap;
import java.util.Map;

public class RuntimeUtil {

    private static class Measurements {
        private long countRuns = 0;
        private long totalRuntime = 0;
        private long maxRuntime = 0;
        private long minRuntime = Long.MAX_VALUE;
        private double avgRuntime = 0;

        public void addRuntime(long runtime) {
            countRuns++;
            totalRuntime += runtime;
            maxRuntime = Long.max(maxRuntime, runtime);
            minRuntime = Long.min(minRuntime, runtime);
            avgRuntime = avgRuntime + (runtime - avgRuntime) / countRuns;
        }
    }

    private final static Map<String, Measurements> measurementsMap = new HashMap<>();
    private final static Map<String, Long> measurementStartTimeMap = new HashMap<>();

    private RuntimeUtil() {

    }

    /**
     * clears all measurements
     */
    private static void clearRuntimes() {
        measurementStartTimeMap.clear();
        measurementsMap.clear();
    }

    /**
     * @param identifier starts a runtime measurement with identifier as its name
     */
    public static void startRun(String identifier) {
        if (measurementStartTimeMap.containsKey(identifier)) {
            System.out.println("WARNING: runtime measurement with id '" + identifier + "' has been started, although the last runtime measurement under this id wasn't finished");
        }

        try {
            measurementStartTimeMap.put(identifier, System.currentTimeMillis());
        } catch (StackOverflowError e) {
            System.out.println(identifier);
            System.out.println(System.currentTimeMillis());
            throw e;
        }
    }

    /**
     * @param identifier stops a runtime measurement with identifier as its name
     */
    public static void stopRun(String identifier) {
        long endTime = System.currentTimeMillis();
        if (!measurementStartTimeMap.containsKey(identifier)) {
            System.out.println("WARNING: no runtime measurement with identifier '" + identifier + "' has been started yet; no data will be gathered.");
            return;
        }
        long startTime = measurementStartTimeMap.get(identifier);
        measurementStartTimeMap.remove(identifier);

        Measurements measurements = measurementsMap.get(identifier);
        if (measurements == null) {
            measurements = new Measurements();
            measurementsMap.put(identifier, measurements);
        }
        measurements.addRuntime(endTime - startTime);
    }

    /**
     * builds a console readable csv string with a header row and value rows
     */
    public static String createCsvString() {
        StringBuilder sb = new StringBuilder();
        final int identifierPadding = 40;
        final int padding = 25;
        sb.append(padString("identifier", identifierPadding, true));
        sb.append(padString("total", padding, false));
        sb.append(padString("min", padding, false));
        sb.append(padString("max", padding, false));
        sb.append(padString("avg", padding, false));
        sb.append(padString("run count", padding, false));
        sb.append("\n");
        measurementsMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach((e) -> {
            sb.append(padString(e.getKey(), identifierPadding, true));
            sb.append(padString("" + e.getValue().totalRuntime, padding, false));
            sb.append(padString("" + e.getValue().minRuntime, padding, false));
            sb.append(padString("" + e.getValue().maxRuntime, padding, false));
            sb.append(padString("" + e.getValue().avgRuntime, padding, false));
            sb.append(padString("" + e.getValue().countRuns, padding, false));
            sb.append("\n");
        });
        return sb.toString();
    }

    /**
     * @param string       the string to pad
     * @param length       length to how many chars the string shall be padded, string is unchanged if it is already longer
     * @param rightPadding if rightPadding is false, it will pad the string on the left side instead
     * @return padded string
     */
    private static String padString(String string, int length, boolean rightPadding) {
        return String.format("%" + (rightPadding ? "-" : "") + length + "s", string + ",");
    }
}