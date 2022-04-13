import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
/**
 * This program is served as the cloud class,
 * it only sends but does not receive
 * @author    Jian He
 */
public class cloud implements Runnable{
    public int port;
    public String multicastAddr;
    public router router;

    public cloud(int port, String multicastAddr, router router){
        this.port = port;
        this.multicastAddr = multicastAddr;
        this.router = router;
    }

    /**
     * Send packet to other cameras
     * @throws IOException
     */
    public void send() throws IOException {
        MulticastSocket socket = new MulticastSocket();
        InetAddress group = InetAddress.getByName(multicastAddr);
        InetAddress localhost = InetAddress.getLocalHost();
        packet p1 = new packet();
        p1.createPacket(router, localhost.toString().substring(localhost.toString().indexOf('/')+1));
        byte[] msg = p1.getPacketsArr();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, group, port);
        router.printRouterTable();
        java.util.Date date=new java.util.Date();
        System.out.println(date);
        System.out.println("\t");
        socket.send(packet);
        socket.close();
    }

    public void run(){
        try{
            while(true) {
                send();
                Thread.sleep(5000);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
