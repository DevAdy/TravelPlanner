import java.io.*;

public class PythonConnector {
    public static String getAIResponse(String prompt) {
        try {
            // Create process builder for Python script
            ProcessBuilder pb = new ProcessBuilder("python", 
                "C:/Users/Aditya B/javapythonintegrate/PyTravelJava/src/app.py", prompt);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Read the output
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }

            process.waitFor();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}