package edu.escuelaing.arem.ASE.app;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class HttpServerFachada {
    private static final String CALCULATOR_HOST = "localhost";
    private static final int CALCULATOR_PORT = 35000;
    private static final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(36000);
        System.out.println("Servidor escuchando en el puerto 36000...");

        while (true) {
            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Recibí: " + inputLine);

                    if (inputLine.startsWith("GET /calculadora")) {
                        String response = "HTTP/1.1 200 OK\r\n"
                                + "Content-Type: text/html\r\n"
                                + "Connection: close\r\n"
                                + "\r\n"
                                + "<!DOCTYPE html>\n"
                                + "<html>\n"
                                + "<head>\n"
                                + "    <title>Calculadora</title>\n"
                                + "    <meta charset=\"UTF-8\">\n"
                                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                                + "</head>\n"
                                + "<body>\n"
                                + "    <h1>Calculadora</h1>\n"
                                + "    <p>Ingrese un comando matemático:</p>\n"
                                + "    <p>Ejemplos:</p>\n"
                                + "    <ul>\n"
                                + "        <li>sin(0.5)</li>\n"
                                + "        <li>cos(0.5)</li>\n"
                                + "        <li>tan(0.5)</li>\n"
                                + "        <li>asin(0.5)</li>\n"
                                + "        <li>1+1</li>\n"
                                + "        <li>2-1</li>\n"
                                + "        <li>3*2</li>\n"
                                + "        <li>4/2</li>\n"
                                + "    </ul>\n"
                                + "    <p>Nota: los comandos deben ser ingresados en inglés.</p>\n"
                                + "    <p>Si usan BubbleSort usar de esta forma:</p>\n"
                                + "    <p>bbl(4,7,1,6,4,6,6,9)</p>\n"
                                + "    <input type=\"text\" id=\"command\" placeholder=\"Ingrese comando (ej: sin(0.5))\">\n"
                                + "    <button onclick=\"calculate()\">Calcular</button>\n"
                                + "    <div id=\"result\"></div>\n"
                                + "    <script>\n"
                                + "        function calculate() {\n"
                                + "            var command = document.getElementById('command').value;\n"
                                + "            var xhttp = new XMLHttpRequest();\n"
                                + "            xhttp.onreadystatechange = function() {\n"
                                + "                if (this.readyState == 4 && this.status == 200) {\n"
                                + "                    var response = JSON.parse(this.responseText);\n"
                                + "                    document.getElementById('result').innerHTML = 'Resultado: ' + response.result;\n"
                                + "                }\n"
                                + "            };\n"
                                + "            xhttp.open('GET', '/computar?comando=(' + encodeURIComponent(command) + ')', true) ;\n"
                                + "            xhttp.send();\n"
                                + "        }\n"
                                + "    </script>\n"
                                + "</body>\n"
                                + "</html>\n";
                        out.println(response);
                        break;

                    } else if (inputLine.startsWith("GET /computar")) {
                        String command = inputLine.split(" ")[1].split("=")[1];
                        command = URLDecoder.decode(command, "UTF-8");
                        String request = "GET /compreflex?comando=" + URLEncoder.encode(command, "UTF-8") + " HTTP/1.1\r\n"
                                + "Host: " + CALCULATOR_HOST + "\r\n"
                                + "User-Agent: " + USER_AGENT + "\r\n"
                                + "Connection: close\r\n"
                                + "\r\n";

                        // Send the HTTP request to the calculator server
                        try (Socket calcSocket = new Socket(CALCULATOR_HOST, CALCULATOR_PORT);
                             PrintWriter calcOut = new PrintWriter(calcSocket.getOutputStream(), true);
                             BufferedReader calcIn = new BufferedReader(new InputStreamReader(calcSocket.getInputStream()))) {

                            calcOut.print(request);
                            calcOut.flush();

                            // Read the response from the calculator server
                            StringBuilder responseBuilder = new StringBuilder();
                            String line;
                            boolean headerEnd = false;
                            while ((line = calcIn.readLine()) != null) {
                                if (line.isEmpty()) {
                                    headerEnd = true;
                                } else if (headerEnd) {
                                    responseBuilder.append(line).append("\n");
                                }
                            }

                            // Print the response to the client
                            String responseBody = responseBuilder.toString().trim();
                            out.println("HTTP/1.1 200 OK\r\n"
                                    + "Content-Type: application/json\r\n"
                                    + "Content-Length: " + responseBody.length() + "\r\n"
                                    + "Connection: close\r\n"
                                    + "\r\n"
                                    + responseBody);
                        }
                        break;
                    }
                    if (!in.ready()) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
