import org.apache.geode.StatisticDescriptor;
import org.apache.geode.internal.statistics.StatisticDescriptorImpl;

import java.io.*;

/**
 * Created by smanvi on 2/20/17.
 * Change the properties corruptFilePath and missingResourceName to fit your needs..
 *
 *
 *
 */
public class StatsCleaner {

    String corruptFilePath = "stats-cleaner/data/bad/corrupted_file.gfs";

    String missingResourceName = "cacheClientProxyStats-id_host1(default_GemfireDS:20234:loner):2:GFNative_89kiXDueIe69204:default_GemfireDS_at_host1:20234.";
    int missingResourceTypeId = 23;

//    String missingResourceName = "ClientSubscriptionStats-_gfe_non_durable_client_with_id_host1(default_GemfireDS:69204:loner):2:GFNative_89kiXDueIe69204:default_GemfireDS_1_queue.";
//    int missingResourceTypeId = 24;

    DataInputStream reader;
    DataOutputStream writer;

    public static void main(String args[]) {
        StatsCleaner cleaner = new StatsCleaner();
        cleaner.start();
    }

    private void start() {
        try {
            validateInputs();
            reader = new DataInputStream(new FileInputStream(corruptFilePath));
            writer = new DataOutputStream(new FileOutputStream(renameFile(corruptFilePath)));
            processHeader();
            fillInMissingResourceType(missingResourceName, missingResourceTypeId);
            copyRestOfFileAsIs();
            writer.flush();
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void validateInputs() {
        //TODO
    }

    private String renameFile(String corruptFilePath) {
        return corruptFilePath.replace(".gfs", "_cleaned.gfs");
    }

    private void copyRestOfFileAsIs() throws IOException {
        byte[] buffer = new byte[1024*10];
        int len;
        while ((len = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, len);
        }
    }

    private void fillInMissingResourceType(String missingResourceName, int missingResourceTypeId) throws IOException {

        RType type = CleanerUtil.resourceTypeMap.get(extraceResourceType(missingResourceName));

        writer.writeByte(CleanerUtil.RESOURCE_TYPE_TOKEN);
        writer.writeInt(missingResourceTypeId);             //resourceType.getId()
        writer.writeUTF(missingResourceName);               //resourceType.getStatisticsType().getName()
        writer.writeUTF(type.getDescription());             //resourceType.getStatisticsType().getDescription()

        StatisticDescriptor[] statisticDescriptors = type.getStatisticDescriptors();
        writer.writeShort(statisticDescriptors.length);

        for (int i = 0; i < statisticDescriptors.length; i++) {
            writer.writeUTF(statisticDescriptors[i].getName());
            writer.writeByte(((StatisticDescriptorImpl) statisticDescriptors[i]).getTypeCode());
            writer.writeBoolean(statisticDescriptors[i].isCounter());
            writer.writeBoolean(statisticDescriptors[i].isLargerBetter());
            writer.writeUTF(statisticDescriptors[i].getUnit());
            writer.writeUTF(statisticDescriptors[i].getDescription());
        }
    }

    private void processHeader() throws IOException {
        writer.writeByte(reader.readByte()); //HEADER_TOKEN 77
        writer.writeByte(reader.readByte()); //ARCHIVE_VERSION 4
        writer.writeLong(reader.readLong()); //initialDate
        writer.writeLong(reader.readLong()); //archiveDescriptor.getSystemId()
        writer.writeLong(reader.readLong()); //archiveDescriptor.getSystemStartTime()
        writer.writeInt(reader.readInt())  ; //timeZone.getRawOffset()
        writer.writeUTF(reader.readUTF())  ; //timeZone.getID()
        writer.writeUTF(reader.readUTF())  ; //archiveDescriptor.getSystemDirectoryPath()
        writer.writeUTF(reader.readUTF())  ; //archiveDescriptor.getProductDescription()
        writer.writeUTF(reader.readUTF())  ; //getOSInfo()
        writer.writeUTF(reader.readUTF())  ; //getMachineInfo()
    }

    private String extraceResourceType(String missingResourceName){
        if(missingResourceName.toLowerCase().contains("cacheclientproxystats")){
            return CleanerUtil.CACHE_CLIENT_PROXY_STATS;
        }else if(missingResourceName.toLowerCase().contains("clientsubscriptionstats")){
            return CleanerUtil.CLIENT_SUBSCRIPTION_STATS;
        }
        return null;
    }
}
