import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;
/**
 * This program is served as the receiver of camera
 * @author    Jian He
 */
public class receiver implements Runnable{
    public int port;
    public String multicastAddr;
    public router router;

    public receiver(int port, String multicastAddr, router router){
        this.port = port;
        this.multicastAddr = multicastAddr;
        this.router = router;
    }

    /**
     * Avoid packets from itself
     * @param decodeP
     * @return
     */
    public boolean comingFromSelf(byte[] decodeP){
        String sourceIp = "";
        for(int i = 0; i < 4; i++){
            if(decodeP[i] < 0){
                sourceIp = sourceIp + (decodeP[i] + 256) + ".";
            }else{
                sourceIp = sourceIp + decodeP[i] + ".";
            }
        }
        sourceIp = sourceIp.substring(0, sourceIp.length()-1);
        if(sourceIp.equals(router.getIP())){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Receive upcoming packet
     * @throws IOException
     */
    public void receivePacket() throws IOException {
        byte[] buffer = new byte[1024];
        MulticastSocket socket = new MulticastSocket(port);
        InetAddress group = InetAddress.getByName(multicastAddr);
        socket.joinGroup(group);

        while(true){
            try{
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                byte[] decodeP = packet.getData();
                if(!comingFromSelf(decodeP)){
                    decodePacket dP = new decodePacket();
                    dP.decodePacket(decodeP);
                    List<routingTableEntry> newEntries = dP.getLstEntries();
                    String sourceIp = dP.getSourceIp();
                    router.receiveUpdate(newEntries, sourceIp);
                    router.checkTimeOut();
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }
    public void run(){
        try{
            receivePacket();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
