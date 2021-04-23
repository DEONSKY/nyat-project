package proj.server;

public interface ICooler {
    void setCoolerOn();
    void setCoolerOff();
    void setCoolerMode(String mode);
    boolean getCoolerStatus();
}
