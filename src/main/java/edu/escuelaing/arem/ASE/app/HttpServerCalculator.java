package edu.escuelaing.arem.ASE.app;

import java.io.*;
import java.net.*;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HttpServerCalculator {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        while (true) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine, outputLine;

                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Recibí: " + inputLine);
                    if (inputLine.startsWith("GET /compreflex")) {
                        String command = inputLine.split("=")[1].split(" ")[0];
                        command = java.net.URLDecoder.decode(command, "UTF-8");
                        command = command.substring(1, command.length() - 1);
                        System.out.println("Comando decodificado: " + command);
                        try {
                            outputLine = calculateResult(command);
                        } catch (Exception e) {
                            outputLine = "Error: " + e.getMessage();
                        }
                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-Type: application/json");
                        out.println();
                        out.println("{\"result\": \"" + outputLine + "\"}");
                        break;
                    }
                    if (!in.ready()) {
                        break;
                    }
                }
                out.close();
                in.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port 36000 or listening for a connection");
                System.out.println(e.getMessage());
            }
        }
    }

    private static String calculateResult(String command) throws Exception {
        if (command.contains("+")) {
            String[] parts = command.split("\\+");
            return String.valueOf(Double.parseDouble(parts[0]) + Double.parseDouble(parts[1]));
        } else if (command.contains("-")) {
            String[] parts = command.split("-");
            return String.valueOf(Double.parseDouble(parts[0]) - Double.parseDouble(parts[1]));
        } else if (command.contains("*")) {
            String[] parts = command.split("\\*");
            return String.valueOf(Double.parseDouble(parts[0]) * Double.parseDouble(parts[1]));
        } else if (command.contains("/")) {
            String[] parts = command.split("/");
            return String.valueOf(Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]));
        } else if (command.startsWith("bbl(")) {
            String[] params = command.substring(4, command.length() - 1).split(",");
            double[] numbers = Arrays.stream(params).mapToDouble(Double::parseDouble).toArray();
            return Arrays.toString(bubbleSort(numbers));
        } else {
            String[] parts = command.split("\\(");
            String methodName = parts[0];
            double param = Double.parseDouble(parts[1].replace(")", ""));
            Method method = Math.class.getMethod(methodName, double.class);
            return String.valueOf(method.invoke(null, param));
        }
    }

    private static double[] bubbleSort(double[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    double temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        return arr;
    }
}
