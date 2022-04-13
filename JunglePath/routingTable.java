import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * This program is served as the routingTable class
 * @author    Jian He
 */
public class routingTable {
    private Map<String, routingTableEntry> map;
    private int routerId;
    private String IP;

    public routingTable(int routerId, String IP){
        map = new ConcurrentHashMap<>();
        this.routerId = routerId;
        this.IP = IP;
        map.put(IP, new routingTableEntry(IP, routerId, IP, 0));
    }

    /**
     * print routing table
     */
    public void printRoutingTable(){
        System.out.println("Address\t\t\tNextHop\t\t\tCost");
        System.out.println("============================================");
        for(Map.Entry<String, routingTableEntry> entry: map.entrySet()){
            System.out.println(String.format("%s\t\t\t%s\t\t\t%s",
                    entry.getValue().ipAddr, entry.getValue().nextHop +" (" + entry.getValue().id + ") ",
                    entry.getValue().cost));
        }
    }

    public void put(String IP, routingTableEntry rTE){
        this.map.put(IP, rTE);
    }

    public Map<String, routingTableEntry> getMap() {
        return map;
    }

}
