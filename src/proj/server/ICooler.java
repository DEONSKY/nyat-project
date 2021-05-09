package proj.server;

public interface ICooler extends IObservable {
    void setCoolerOn();
    void setCoolerOff();
    //void setCoolerMode(String mode);
    boolean getCoolerStatus();
}
