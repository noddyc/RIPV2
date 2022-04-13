import java.util.ArrayList;
import java.util.List;
/**
 * This program is served as the class to decode an upcoming packet
 * @author    Jian He
 */
public class decodePacket {
    private List<routingTableEntry> lstEntries;
    private String sourceIp;
    private int numberEntries;

    public decodePacket(){
        lstEntries = new ArrayList<>();
    }

    /**
     * decode an upcoming packet
     * @param packetArr
     */
    public void decodePacket(byte[] packetArr){
        String sourceIp = "";
        int counter = 0;
        for(int i = 0; i < 4; i++){
            if(packetArr[i] < 0){
                sourceIp = sourceIp + (packetArr[i] + 256) + ".";
            }else{
                sourceIp = sourceIp + packetArr[i] + ".";
            }
        }
        sourceIp = sourceIp.substring(0, sourceIp.length()-1);
        this.sourceIp = sourceIp;
        int entrySize = packetArr[4];
        this.numberEntries = entrySize;
        counter += 5;
        for(int i = 0; i < entrySize; i++){
            String destIp = "";
            for(int j = 0; j < 4; j++){
                if(packetArr[j] < 0){
                    destIp = destIp + (packetArr[counter] + 256) + ".";
                    counter++;
                }else{
                    destIp = destIp + packetArr[counter] + ".";
                    counter++;
                }
            }
            destIp = destIp.substring(0, destIp.length()-1);

            int id = packetArr[counter];
            counter += 1;

            String nextHop = "";
            for(int j = 0; j < 4; j++){
                if(packetArr[j] < 0){
                    nextHop = nextHop + (packetArr[counter] + 256) + ".";
                    counter++;
                }else{
                    nextHop = nextHop + packetArr[counter] + ".";
                    counter++;
                }
            }
            nextHop = nextHop.substring(0, nextHop.length()-1);

            int cost = packetArr[counter];
            counter += 1;
            routingTableEntry rTE = new routingTableEntry(destIp, id, nextHop, cost);
            lstEntries.add(rTE);
        }
    }

    public int getNumberEntries() {
        return numberEntries;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public List<routingTableEntry> getLstEntries() {
        return lstEntries;
    }

}
