package edu.escuelaing.arem.ASE.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class HttpServerFachada {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "http://localhost:36000/compreflex";


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        BoubleSort.main();
        while (true) {
            try {
                serverSocket = new ServerSocket(35000);
            } catch (IOException e) {
                System.err.println("Could not listen on port: 35000.");
                System.exit(1);
            }

            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            boolean isCalculadora = false;
            boolean isComputar = false;
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            String lineaf = null;
            while ((inputLine = in.readLine()) != null) {
                if(inputLine.contains("GET")){
                    if (inputLine.contains("/calculadora")){
                        isCalculadora = true;
                        System.out.println("SIIII");
                    }
                        if (inputLine.contains("/computar")){
                        isComputar = true;
                        lineaf = inputLine;
                            System.out.println("Computa");
                    }
                }
                System.out.println("Recibí: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            if (isCalculadora){
                outputLine="HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n" +
                        "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "    <title>Form Example</title>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h1>Form with GET</h1>\n" +
                        "<form action=\"/hello\">\n" +
                        "    <label for=\"name\">Name:</label><br>\n" +
                        "    <input type=\"text\" id=\"name\" name=\"name\" value=\"John\"><br><br>\n" +
                        "    <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n" +
                        "</form>\n" +
                        "<div id=\"getrespmsg\"></div>\n" +
                        "\n" +
                        "<script>\n" +
                        "    function loadGetMsg() {\n" +
                        "        let nameVar = document.getElementById(\"name\").value;\n" +
                        "        const xhttp = new XMLHttpRequest();\n" +
                        "        xhttp.onload = function() {\n" +
                        "            document.getElementById(\"getrespmsg\").innerHTML =\n" +
                        "            this.responseText;\n" +
                        "        }\n" +
                        "        xhttp.open(\"GET\", \"/hello?name=\"+nameVar);\n" +
                        "        xhttp.send();\n" +
                        "    }\n" +
                        "</script>\n" +
                        "\n" +
                        "<h1>Form with POST</h1>\n" +
                        "<form action=\"/hellopost\">\n" +
                        "    <label for=\"postname\">Name:</label><br>\n" +
                        "    <input type=\"text\" id=\"postname\" name=\"name\" value=\"John\"><br><br>\n" +
                        "    <input type=\"button\" value=\"Submit\" onclick=\"loadPostMsg(postname)\">\n" +
                        "</form>\n" +
                        "\n" +
                        "<div id=\"postrespmsg\"></div>\n" +
                        "\n" +
                        "<script>\n" +
                        "    function loadPostMsg(name){\n" +
                        "        let url = \"/hellopost?name=\" + name.value;\n" +
                        "\n" +
                        "        fetch (url, {method: 'POST'})\n" +
                        "            .then(x => x.text())\n" +
                        "            .then(y => document.getElementById(\"postrespmsg\").innerHTML = y);\n" +
                        "    }\n" +
                        "</script>\n" +
                        "</body>\n" +
                        "</html>";
                out.println(outputLine);
                out.close();
                in.close();
                clientSocket.close();
                serverSocket.close();
            } else if (isComputar){
                System.out.println(lineaf);
                String queryParams = lineaf.replace("GET /computar?comando=","");
                queryParams = queryParams.replace(" HTTP/1.1","");

                URL obj = new URL(GET_URL);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);


                //The following invocation perform the connection implicitly before getting the code
                int responseCode = con.getResponseCode();
                System.out.println("GET Response Code :: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in1 = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine1;
                    StringBuffer response1 = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response1.append(inputLine);
                    }
                    in.close();

                    // print result
                    System.out.println(response1.toString());
                } else {
                    System.out.println("GET request not worked");
                }
                System.out.println("GET DONE");
            }
            }
            outputLine =
                    "<!DOCTYPE html>" +
                            "<html>" +
                            "<head>" +
                            "<meta charset=\"UTF-8\">" +
                            "<title>Title of the document</title>\n" +
                            "</head>" +
                            "<body>" +
                            "<h1>Mi propio mensaje</h1>" +
                            "</body>" +
                            "</html>";
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
            serverSocket.close();
        }


    }
}
