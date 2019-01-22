package com.liber;

import javax.annotation.processing.FilerException;
import java.io.*;
import java.net.*;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        ServerSocket serv = new ServerSocket(80);

        while(true)
        {
            //przyjecie polaczenia
            System.out.println("Oczekiwanie na polaczenie...");
            Socket sock=serv.accept();

            //strumienie danych
            InputStream is=sock.getInputStream();
            OutputStream os=sock.getOutputStream();
            BufferedReader inp=new BufferedReader(new InputStreamReader(is));
            DataOutputStream outp=new DataOutputStream(os);

            //przyjecie zadania (request)
            String request=inp.readLine();
            System.out.println(request);
            String space = " ";
            String plik = "";

            if(!request.equals("GET /favicon.ico HTTP/1.1") && !request.equals("GET / HTTP/1.1")) {
                char[] requestToArray = request.toCharArray();
                for(int i=4; i<requestToArray.length; i++) {
                    if(requestToArray[i] == space.toCharArray()[0]){
                        break;
                    }
                    plik = plik + requestToArray[i];

                }
                System.out.println(plik);
            }

            try {
                FileInputStream fis = new FileInputStream(plik);
            } catch (FileNotFoundException e){
                System.out.println("HTTP/1.0 404 Not Found");
            }

            //wyslanie odpowiedzi (response)
            if(request.startsWith("GET"))
            {
                //response header
                outp.writeBytes("HTTP/1.0 200 OK\r\n");
                outp.writeBytes("Content-Type: text/html\r\n");
                outp.writeBytes("Content-Length: \r\n");
                outp.writeBytes("\r\n");

                //response body
                outp.writeBytes("<html>\r\n");
                outp.writeBytes("<H1>Hello World!</H1>\r\n");
                outp.writeBytes("<iframe src=\"plik.txt\"> <iframe/>");
                outp.writeBytes("</html>\r\n");
            }
            else
            {
                outp.writeBytes("HTTP/1.1 501 Not supported.\r\n");
            }

            //zamykanie strumieni
            inp.close();
            outp.close();
            sock.close();
        }
    }
}
