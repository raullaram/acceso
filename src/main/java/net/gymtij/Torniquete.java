package net.gymtij;

import com.fazecast.jSerialComm.SerialPort;

public class Torniquete {

public static Boolean torniquete() {

SerialPort[] ports = SerialPort.getCommPorts();
SerialPort rs485Port = null;

for (SerialPort port : ports) {
    System.out.println(port.getDescriptivePortName());
     if (port.getDescriptivePortName().contains("RS485")) {           
        
        rs485Port = port;
        break;
    }
}

if (rs485Port == null) {
    System.out.println("No se encontró un puerto RS485 disponible.");
    return false;
}

if (rs485Port.openPort()) {
    rs485Port.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
    rs485Port.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
} else {
    System.out.println("No se pudo abrir el puerto RS485.");
    return false;
}

return true;

}

}
