package app.helper;

public class ProjectInfoHelper {
    public static String getProjectInfo(String version, String date) {
        return "=== Project Info ===\n" +
                "App for measures converting.\n" +
                "Version " + version + "\n" +
                "Author: Den\n" +
                "Date: " + date + "\n" +
                "====================\n";
    }
}

