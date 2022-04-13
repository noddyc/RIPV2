import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * This program is served as the Main class
 * @author    Jian He
 */
public class Main {
    public static final String cloudIPStr = "172.18.0.10";
    public static final int portNum = 63001;
    public static final String subnetMask = "172.18.0.0/16";
    public static final String lowerBound = "172.18.0.1";
    public static final String upperBound = "172.18.255.254";

    /**
     * Check if the router IP is in the subnet as cloud
     * @param cloudIP
     * @return
     */
    public static boolean validateSubnetMask(String cloudIP){
        String[] cloudIPSplit = cloudIP.split("\\.");
        String[] lowerBoundSplit = lowerBound.split("\\.");
        String[] upperBoundSplit = upperBound.split("\\.");
        if(!cloudIPSplit[0].equals(lowerBoundSplit[0])){
            return false;
        }
        if(!cloudIPSplit[1].equals(lowerBoundSplit[1])){
            return false;
        }
        if(Integer.parseInt(cloudIPSplit[2]) < Integer.parseInt(lowerBoundSplit[2])
        || Integer.parseInt(cloudIPSplit[2]) > Integer.parseInt(upperBoundSplit[2])){
            return false;
        }
        if(Integer.parseInt(cloudIPSplit[3]) < Integer.parseInt(lowerBoundSplit[3])
                || Integer.parseInt(cloudIPSplit[3]) > Integer.parseInt(upperBoundSplit[3])){
            return false;
        }
        return true;
    }

    /**
     * Main to start
     * @param args
     * @throws UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException {
        if(args.length != 2){
            System.out.println("Usage: Main id 172.18.0.10:63001");
            System.exit(0);
        }
        int id = Integer.parseInt(args[0]);
        String IpAndPort = args[1];
        String[] splitStr = IpAndPort.split(":");
        if(splitStr.length != 2){
            System.out.println("Usage: Main id 172.18.0.10:63001");
            System.exit(0);
        }
        String cloudIP = splitStr[0];
        int port = Integer.parseInt(splitStr[1]);
        if(!(cloudIP.equals(cloudIPStr)) || !(port == portNum)){
            System.out.println("Usage: Main id 172.18.0.10:63001");
            System.exit(0);
        }
        if(id == 108){
            try{
                InetAddress localhost = InetAddress.getLocalHost();
                router r1 = new router(id, port,
                        localhost.toString().substring(localhost.toString().indexOf('/')+1));
                System.out.println("cloud " + id + " created");
                Thread cloud=new Thread(new cloud(port,"230.230.230.230",r1));
                cloud.start();
                while(true){Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            try
            {
                InetAddress localhost = InetAddress.getLocalHost();
                String IpAddress = localhost.toString().substring(localhost.toString().indexOf('/')+1);
                router r1 = new router(id, port, IpAddress);
                if(validateSubnetMask(IpAddress)){
                    System.out.println("router " + id + " created");
                }else{
                    System.out.println("router " + id + " created");
                    System.out.println("SubnetMask of cloud is " + subnetMask);
                    System.out.println("router not in the same network as cloud");
                }

                Thread client=new Thread(new receiver(port,"230.230.230.230", r1));
                client.start();
                Thread sender=new Thread(new sender(port,"230.230.230.230",r1));
                sender.start();

                while(true){Thread.sleep(1000);}
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
