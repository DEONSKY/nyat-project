package proj.server;

public class CoolerDriver implements ICooler{

    private boolean status;

    @Override
    public void setCoolerOn() {
        status=true;
    }

    @Override
    public void setCoolerOff() {
        status=false;
    }

    @Override
    public void setCoolerMode(String mode) {

    }
    @Override
    public boolean getCoolerStatus(){
        return status;
    }
}
