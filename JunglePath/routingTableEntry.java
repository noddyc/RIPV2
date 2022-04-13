/**
 * This program is served as the routingTableEntry class
 * @author    Jian He
 */
public class routingTableEntry {
    public final static int maxCost = 16;
    public String ipAddr;
    public int id;
    public String nextHop;
    public int cost;

    public routingTableEntry(String ipAddr, int id, String hop, int cost){
        this.ipAddr = ipAddr;
        this.nextHop = hop;
        this.id = id;
        this.cost = cost;
    }

    /**
     * Update the cost of the routingTableEntry
     * @return
     */
    public routingTableEntry updateCost(){
        this.cost += 1;
        if(this.cost >= maxCost){
            this.cost = maxCost;
        }
        return this;
    }

    /**
     * Poison the entry
     */
    public void poisonEntry(){
        this.cost = maxCost;
    }

    /**
     * Update the hop
     * @param newHop
     * @return
     */
    public routingTableEntry updateHop(String newHop){
        this.nextHop = newHop;
        return this;
    }

    /**
     * Update entry
     * @param newEntry
     */
    public void updateEntry(routingTableEntry newEntry){
        this.ipAddr = newEntry.ipAddr;
        this.nextHop = newEntry.nextHop;
        this.cost = newEntry.cost;
        this.id = newEntry.id;
    }

    @Override
    public String toString() {
        return "routingTableEntry{" +
                "ipAddr='" + ipAddr + '\'' +
                ", id=" + id +
                ", nextHop='" + nextHop + '\'' +
                ", cost=" + cost +
                '}';
    }
}
