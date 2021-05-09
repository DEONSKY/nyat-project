package proj.server;

public interface IObservable {
    void addObserver(IObserver observer);
    void detachObserver(IObserver observer);
}

