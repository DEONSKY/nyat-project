package proj.server;

public interface ICooler extends Subscriable{
    void setCoolerOn();
    void setCoolerOff();
    void setCoolerMode(String mode);
    boolean getCoolerStatus();
}
