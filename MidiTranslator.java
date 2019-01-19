import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

public class MidiTranslator {

    public MidiDevice       devin;
    public MidiDevice       devout;

    public static void main(String[] args) {
        new MidiTranslator().start();
    }

    public void start() {
        init();
        try {
            devout.open();
            MyReceiver rcvr = new MyReceiver();
            MidiSystem.getTransmitter().setReceiver(rcvr);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    protected class MyReceiver implements Receiver  {
        Receiver rcvr;
        public MyReceiver() { //get device receiver or print stack trace
            try {
                this.rcvr = MidiSystem.getReceiver();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }

        @Override // to overload method
        public void send(MidiMessage message, long zeit) {
            rcvr.send(message, zeit);
        }

        @Override // to overload method
        public void close() {
            rcvr.close();
        }
    }

    //private in package
    void init() {

        MidiDevice.Info[] devices;

        devices = MidiSystem.getMidiDeviceInfo();
        try{

            for (MidiDevice.Info dev: devices) {

                if (MidiSystem.getMidiDevice(dev).getMaxTransmitters() == 0) {
                    devin = MidiSystem.getMidiDevice(dev);
                }

                if (! devout.isOpen()) {
                    devout.open();
                }

                if (MidiSystem.getMidiDevice(dev).getMaxReceivers() == -1){
                    devout = MidiSystem.getMidiDevice(dev);
                }

                if (devout != null) {//not need to use devin!=null
                    devin.getTransmitter().setReceiver(devout.getReceiver());
                }
            }
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }
}
