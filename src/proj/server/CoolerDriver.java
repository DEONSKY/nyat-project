package proj.server;

public class CoolerDriver implements ICooler{

    private boolean status;
    ISubject publisher = new Publisher();

    @Override
    public void setCoolerOn() {

        status=true;
        publisher.notify("Opened");
    }

    @Override
    public void setCoolerOff() {
        status=false;
        publisher.notify("Closed");
    }

    @Override
    public boolean getCoolerStatus(){
        return status;
    }

    @Override
    public void addObservable(IObserver observer) {
        publisher.attach(observer);
        System.out.println("Cooler Sub added");
    }
}
