package Utils.Observer;

import Utils.Events.Event;

public interface Observable {
    void addObserver(Observer e);
    void removeObserver(Observer e);
    void notifyObservers();
}
