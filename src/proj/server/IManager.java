package proj.server;

import java.io.IOException;

public interface IManager extends IObservable {
    boolean Login(String username,String password);
    void Register(String username,String password);
    int getHeat();
    void setCoolerOn();
    void setCoolerOff();
    boolean getCoolerStatus();
    void setManagerStatus(ManagerStatus managerStatus);
    void run() throws IOException;
}
