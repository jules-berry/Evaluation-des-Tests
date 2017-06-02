package Arduino;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class ArduinoUsbChannel {

    protected final SerialPort serialPort;

    protected PipedInputStream vcpOutputDataStream;
    protected PipedOutputStream vcpOutputInnerStreamWriter;

    protected PipedInputStream vcpInputInnerStream;
    protected PipedOutputStream vcpInputDataStreamWriter;

    protected Thread writingThread;

    public ArduinoUsbChannel(String port) throws IOException {

        this.serialPort = new SerialPort(port);

        this.vcpOutputDataStream = new PipedInputStream();
        this.vcpOutputInnerStreamWriter = new PipedOutputStream(vcpOutputDataStream);

        this.vcpInputInnerStream = new PipedInputStream();
        this.vcpInputDataStreamWriter = new PipedOutputStream(this.vcpInputInnerStream);

    }

    public static String getOneComPort() {

        String myVirtualComPort = null;

        System.err.println("COM Port Names:");

        String[] portNames = SerialPortList.getPortNames();
        for (String portName : portNames) {
            System.err.println(portName);
            if (myVirtualComPort == null) {
                myVirtualComPort = portName;
            }
        }
        System.err.println();
        //return myVirtualComPort;
        //return "/dev/ttyACM3";
        return null;
    }

    public void open() throws SerialPortException, IOException {

        //System.err.println("Opening VCP...");
        serialPort.openPort();//Open serial port
        serialPort.setParams(SerialPort.BAUDRATE_115200,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);

        int mask = SerialPort.MASK_RXCHAR; // + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
        serialPort.setEventsMask(mask);//Set mask
        serialPort.addEventListener(new SerialPortEventListener() {

            
            @Override
			public void serialEvent(SerialPortEvent event) {
                if (event.isRXCHAR()) {//If data is available

                    try {
                        byte buffer[] = serialPort.readBytes();

                        //System.err.println("=> VCP Data String received: " + new String(buffer));

                        if (buffer != null) {
                            vcpOutputInnerStreamWriter.write(buffer);
                            vcpOutputInnerStreamWriter.flush();
                        }

                    } catch (SerialPortException ex) {
                        ex.printStackTrace(System.err);
                    } catch (IOException ex) {
                        ex.printStackTrace(System.err);
                    }
                } else if (event.isCTS()) {//If CTS line has changed state
                    if (event.getEventValue() == 1) {//If line is ON
                        System.err.println("CTS - ON");
                    } else {
                        System.err.println("CTS - OFF");
                    }
                } else if (event.isDSR()) {///If DSR line has changed state
                    if (event.getEventValue() == 1) {//If line is ON
                        System.err.println("DSR - ON");
                    } else {
                        System.err.println("DSR - OFF");
                    }
                }
            }
        });

        this.writingThread = new Thread(new Runnable() {

            
            @Override
			public void run() {

                PipedInputStream input = ArduinoUsbChannel.this.vcpInputInnerStream;

                try {

                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = input.read(buffer)) > 0) {

                        //System.err.println("=> VCP Data String to be written: " + new String(buffer, 0, length));

                        for (int i = 0; i < length; i++) {
                            //VCPChannel.this.serialPort.writeBytes(data);
                            ArduinoUsbChannel.this.serialPort.writeByte(buffer[i]);
                        }

                    }
                } catch (IOException ex) {

                    ex.printStackTrace(System.err);
                } catch (SerialPortException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        });

        this.writingThread.start();
    }

    public void close() throws IOException {

        this.vcpOutputDataStream.close();
        this.vcpInputDataStreamWriter.close();

        //System.err.println("Closing VCP...");
        try {
            this.serialPort.closePort();//Close serial port
        } catch (SerialPortException ex) {
            ex.printStackTrace(System.err);
        }

        this.vcpOutputInnerStreamWriter.close();
        this.vcpInputInnerStream.close();

        this.writingThread.interrupt();
        try {
            this.writingThread.join(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public OutputStream getWriter() {
        return this.vcpInputDataStreamWriter;
    }

    public InputStream getReader() {
        return this.vcpOutputDataStream;
    }

}