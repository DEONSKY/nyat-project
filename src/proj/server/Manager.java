package proj.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Manager implements IManager {

    ISubject publisher = new Publisher() ;
    ManagerStatus managerStatus;
    private final IUserRepository userRepository;
    private final IHeatSensor heatSensor;
    private final ICooler cooler;


    public Manager(IUserRepository userRepository, IHeatSensor heatSensor, ICooler cooler) {
        this.userRepository = userRepository;
        this.heatSensor = heatSensor;
        this.cooler = cooler;
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate((TimerTask) heatSensor, 0, 5000);
        setManagerStatus(ManagerStatus.ON);
    }

    public boolean Login(String username,String password){
        setManagerStatus(ManagerStatus.PROCESSING);
        User user =userRepository.findByUsername(username);
        if(user!=null&&user.getPassword().equals(password)){

            return true;
        }
        return false;
    }
    public void Register(String username,String password){
        setManagerStatus(ManagerStatus.PROCESSING);
        User user = userRepository.findByUsername(username);
        if(user==null){
            System.out.println("HHEHEHEHE");
            userRepository.add(new User.Builder().username(username).password(password).build());
        }
    }

    public int getHeat(){
        return heatSensor.getHeat();
    }
    public void setCoolerOn(){
        setManagerStatus(ManagerStatus.PROCESSING);
        cooler.setCoolerOn();
    }
    public void setCoolerOff(){
        setManagerStatus(ManagerStatus.PROCESSING);
        cooler.setCoolerOff();
    }
    public boolean getCoolerStatus(){
        setManagerStatus(ManagerStatus.PERCEPTION);
        return cooler.getCoolerStatus();
    }
    public void setManagerStatus(ManagerStatus managerStatus){
        this.managerStatus = managerStatus;
        publisher.notify("Manager Status "+managerStatus.toString());
    }


    @Override
    public void addObserver(IObserver observer) {
        publisher.attach(observer);
        System.out.println("Manager Sub added");
    }

    @Override
    public void detachObserver(IObserver observer) {
        publisher.detach(observer);
        System.out.println("Manager Sub detach");
    }

    @Override
    public void run() throws IOException {
        int clientCount = 1;
        ServerSocket serverSocket = new ServerSocket(8001);
        System.out.println("Sunucu oluşturuldu. İstek gelmesi bekleniyor...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println(clientSocket.getRemoteSocketAddress() + " baglandi.");
            List<IObservable> observables = new ArrayList<>();
            observables.add(heatSensor);
            observables.add(cooler);
            observables.add(this);
            NetworkThread nt1 = new NetworkThread(observables,"Connection " +clientCount+" handler", clientSocket,this);
            /*heatSensor.addObserver(nt1);
            cooler.addObserver(nt1);
            this.addObserver(nt1);*/
            nt1.start();
            clientCount++;
        }
    }
}
