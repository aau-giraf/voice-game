package dk.aau.cs.giraf.cars.sound;

public class InputThread extends Thread {
    private Thread thread = new Thread();

    public InputThread(){}

    public void StartThread()
    {
        thread.start();
    }

    public void StopThread()
    {
        thread.stop();
    }
}
