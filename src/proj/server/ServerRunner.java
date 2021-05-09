package proj.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRunner {

    public static void main(String[] args) throws IOException {

        IUserRepository userRepository = new UserRepositoryPostgreSql();
        IHeatSensor heatSensor = new HeatSensorDriver();
        ICooler cooler = new CoolerDriver();
        Manager manager = new Manager(userRepository, heatSensor, cooler);
        manager.run();

    }

}