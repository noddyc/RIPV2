
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * This program is served as the router class
 * @author    Jian He
 */
public class router {
    private int id;
    private int port;
    private String IP;
    private routingTable routingTable;
    private Map<String, Long> timer;
    private static final long timeOut = 10500;

    public router(int id, int port, String IPAddr) throws UnknownHostException {
        this.id = id;
        this.port = port;
        this.IP = IPAddr;
        this.routingTable = new routingTable(id, IP);
        this.timer = new ConcurrentHashMap<>();

    }

    /**
     * Print the router table
     */
    public void printRouterTable(){
        this.routingTable.printRoutingTable();
    }

    /**
     * Check if any camera offline every 10 secs
     * @return
     */
    public boolean checkTimeOut(){
        List<String> timeOutIP = new ArrayList<>();
        long getNewTime = new Date().getTime();
        for (Map.Entry<String,Long> entry : this.timer.entrySet()) {
            long getOldTime = entry.getValue();
            if(getNewTime - getOldTime > timeOut){
                timeOutIP.add(entry.getKey());
            }
        }
        Map<String, routingTableEntry> temp = this.routingTable.getMap();
        for(int i = 0; i < timeOutIP.size(); i++){
            for(Map.Entry<String, routingTableEntry> entry: temp.entrySet()){
                if(timeOutIP.get(i).equals(entry.getValue().nextHop)){
                    entry.getValue().poisonEntry();
                }
            }
        }
        if(timeOutIP.size() > 0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Update router table from received packet
     * @param newList
     * @param sourceIp
     */
    public synchronized void receiveUpdate(List<routingTableEntry> newList,
                                           String sourceIp){
        // check updates
        Map<String, routingTableEntry> routingTable = this.getRoutingTable().getMap();
        List<routingTableEntry> updateEntries = new ArrayList<>();
        timer.put(sourceIp, new Date().getTime());
        for (routingTableEntry temp : newList) {
            if (temp.cost == 0) {
                routingTable.put(sourceIp,
                            new routingTableEntry(sourceIp, temp.id, sourceIp, 1));
                continue;
            }
            temp = temp.updateCost();
            String newDestIP = temp.ipAddr;
            if (!routingTable.containsKey(newDestIP)) {
                routingTable.put(newDestIP, temp);
            } else {
                routingTableEntry oldEntry = routingTable.get(newDestIP);
                if (oldEntry.nextHop.equals(sourceIp)) {
                    updateEntries.add(temp.updateHop(sourceIp));
                } else {
                    int cost = routingTable.get(newDestIP).cost;
                    int newCost = temp.cost;
                    if (newCost < cost) {
                        updateEntries.add(temp);
                    }
                }
            }
        }
        for (routingTableEntry updateEntry : updateEntries) {
            routingTable.get(updateEntry.ipAddr).updateEntry(updateEntry);
        }
    }

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public String getIP() {
        return IP;
    }

    public routingTable getRoutingTable() {
        return routingTable;
    }

    public Map<String, Long> getTimer() {
        return timer;
    }
}
