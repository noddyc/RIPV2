import java.util.Map;
/**
 * This program is served as the packet class
 * @author    Jian He
 */
public class packet {
    private byte[] packetsArr;

    public packet(){
        packetsArr = new byte[512];
    }

    /**
     * Create a packet to send
     * @param router
     * @param sourceIp
     */
    public void createPacket(router router, String sourceIp){
        int counter = 0;
        byte[] sourceIpArr = convertStringtoByte(sourceIp);
        for(int i = 0; i < sourceIpArr.length; i++){
            packetsArr[counter] = sourceIpArr[i];
            counter++;
        }
        Map<String, routingTableEntry> map = router.getRoutingTable().getMap();
        int entrySize = map.size();
        packetsArr[counter] = (byte) entrySize;
        counter++;
        for(Map.Entry<String, routingTableEntry> entry: map.entrySet()){
            byte[] ipAddrArr = convertStringtoByte(entry.getValue().ipAddr);
            for(int i = 0; i < ipAddrArr.length; i++){
                packetsArr[counter] = ipAddrArr[i];
                counter++;
            }
            packetsArr[counter] = (byte) entry.getValue().id;
            counter += 1;
            byte[] nextHopArr = convertStringtoByte(entry.getValue().nextHop);
            for(int i = 0; i < nextHopArr.length; i++){
                packetsArr[counter] = nextHopArr[i];
                counter++;
            }
            packetsArr[counter] = (byte) entry.getValue().cost;
            counter += 1;
        }
    }
    public byte[] getPacketsArr() {
        return packetsArr;
    }

    public static byte[] convertStringtoByte(String IPAddr){
        String[] tokens = IPAddr.split("\\.");
        byte[] result = new byte[tokens.length];
        for(int i = 0; i < result.length; i++){
            result[i] = (byte) Integer.parseInt(tokens[i]);
        }
        return result;

    }
}
