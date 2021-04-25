package proj.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ServerRunner implements Runnable {


    Socket clientSocket;

    ServerRunner(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    IUserRepository userRepository = new UserRepositoryPostgreSql();
    IHeatSensor heatSensor = new HeatSensorDriver();
    ICooler cooler = new CoolerDriver();
    Manager manager = new Manager(userRepository, heatSensor, cooler);

    String username;
    String password;

    public static void main(String[] args) throws IOException {


        ServerSocket serverSocket = new ServerSocket(8001);
        System.out.println("Sunucu oluşturuldu. İstek gelmesi bekleniyor...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println(clientSocket.getRemoteSocketAddress() + " baglandi.");
            new Thread(new ServerRunner(clientSocket)).start();
        }

    }

    public void run() {
        SubscriberUser subscriber= new SubscriberUser();
        heatSensor.addSubscriber(subscriber);
        cooler.addSubscriber(subscriber);
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate((TimerTask) heatSensor, 0, 5000);

        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine, outputLine;
            System.out.println("istemciden girdi bekleniyor...");
            boolean isLogged = false;
            //
            do { // istemciden gelen string okunuyor...
                inputLine = null;
                if(isLogged){
                    inputLine=in.readLine();
                    switch (inputLine){
                        case "getHeat":
                            inputLine=Integer.toString(manager.getHeat());
                            break;
                        case "setCoolerOn":
                            manager.setCoolerOn();
                            inputLine="Soğutucu Açıldı";
                            break;
                        case "setCoolerOff":
                            manager.setCoolerOff();
                            inputLine="Soğutucu Kapatıldı";
                            break;
                        case "getCoolerStatus":
                            inputLine =manager.getCoolerStatus()?"Soğutucu Açık":"Soğutucu Kapalı";
                            break;
                    }

                }else {
                    inputLine=in.readLine();
                    String tokens[] = inputLine.split(":");
                    System.out.println(inputLine);
                    if(tokens[0].equals("Login")&&tokens.length==3){
                        username = tokens[1];
                        password = tokens[2];
                        isLogged=manager.Login(username,password);
                        System.out.println(isLogged);
                    }

                    //inputLine=username+""+password;
                    if(isLogged){
                        inputLine="true";
                    }else{
                        inputLine="false";
                    }
                }


                System.out.println(clientSocket.getRemoteSocketAddress() + "istemcisinden gelen :" + inputLine);
                outputLine = inputLine.toUpperCase(); //

                out.println(outputLine); //
            } while (!inputLine.equals("son"));
            System.out.println(clientSocket.getLocalSocketAddress() + " baglantisi kesildi.");
            // stream ve socketleri kapat.

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}