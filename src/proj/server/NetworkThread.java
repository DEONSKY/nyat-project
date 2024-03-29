package proj.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Observable;

public class NetworkThread implements Runnable,IObserver{

    private Thread networkThread;
    private final String threadName;

    private final Socket clientSocket;
    private final IManager manager;
    private final List<IObservable> observables;

    NetworkThread(List<IObservable> observables,String threadName, Socket clientSocket, IManager manager) {
        this.threadName = threadName;
        this.clientSocket = clientSocket;
        this.manager = manager;
        this.observables = observables;
    }

    public void run() {
        try {
            for(IObservable observable:observables)
                observable.addObserver(this);

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
                        case "end":
                            break;
                        default:
                            inputLine = "Komut bulunamadı. Daha fazla bilgi için help komutunu çalıştırın";
                            break;
                    }

                }else {
                    inputLine=in.readLine();
                    if(!inputLine.equals("end")){
                        String tokens[] = inputLine.split(":");
                        System.out.println(inputLine);
                        String username;
                        String password;
                        if(tokens[0].equals("Login")&&tokens.length==3){
                            username = tokens[1];
                            password = tokens[2];
                            isLogged=manager.Login(username,password);
                            System.out.println(isLogged);
                        }
                        if(tokens[0].equals("Register")&&tokens.length==3){
                            username = tokens[1];
                            password = tokens[2];
                            manager.Register(username,password);
                            System.out.println(isLogged);
                        }

                        //inputLine=username+""+password;
                        if(isLogged){
                            inputLine="true";
                        }else{
                            inputLine="false";
                        }
                    }
                }


                System.out.println(clientSocket.getRemoteSocketAddress() + "istemcisinden gelen :" + inputLine);
                outputLine = inputLine; //

                out.println(outputLine); //
                manager.setManagerStatus(ManagerStatus.PENDING);
            } while (!inputLine.equals("end"));
            System.out.println(clientSocket.getLocalSocketAddress() + " baglantisi kesildi.");
            // stream ve socketleri kapat.
            for(IObservable observable:observables)
                observable.detachObserver(this);

            try{
                networkThread.interrupt();
            }catch (NullPointerException e){
                System.out.println("Thread Killed");
            }



        } catch (IOException e) {
            for(IObservable observable:observables)
                observable.detachObserver(this);

            try{
                networkThread.interrupt();
            }catch (NullPointerException er){
                System.out.println("Thread Killed");
            }
            System.out.println(e);
        }
    }
    public void start(){
        if(networkThread==null){
            networkThread = new Thread(new NetworkThread(observables,threadName, clientSocket,manager));
            networkThread.start();
        }
    }

    @Override
    public void update(String m) {
        System.out.println(threadName +"->" + m);
    }
}
