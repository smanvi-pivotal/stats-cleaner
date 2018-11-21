# stats-cleaner
Apache Geode Stats file cleaner

In some rare cases, the stats files produced by older versions of Apache Geode(1.0.0-incubating) or Gemfire(older than 8.2.3) might be corrupted.
Stats files are corrupted when some headers are not written to the stats files. This is a quick and dirty utility which cleans the corrupted files so that it can be loaded and analyzed in the VSD tool. 

### Cleaning up the stats files is a 2 step process.

#### Step 1:
Get the ResourceType id and corresponding ResourceName by running geode class `org.apache.geode.internal.SystemAdmin` by passing  the below program args. You should have Apache Geode libraries on your class path. 

```
-debug stats -details -archive="/path_to_bad/stats_file.gfs"
```
If the stats file is corrupted then the above will throw a `java.lang.IllegalStateException` accompanied by the typeId and the TypeName of the missing resource  as shown below.
```
java.lang.IllegalStateException: ResourceType is missing for resourceTypeId : 24, resource name : ClientSubscriptionStats-_gf_non_durable_client_with_id_11.11.11.11(default_GemfireDS:1111:loner):2:GFNative_89kiXDueIe1111:default_GemfireDS_1_queue
	at org.apache.geode.internal.statistics.StatArchiveReader$StatArchiveFile.readResourceInstanceCreateToken(StatArchiveReader.java:3234)
	at org.apache.geode.internal.statistics.StatArchiveReader$StatArchiveFile.readToken(StatArchiveReader.java:3396)
	at org.apache.geode.internal.statistics.StatArchiveReader$StatArchiveFile.update(StatArchiveReader.java:2955)
  ```

#### Step 2:
Modify the 3 properties of [StatsCleaner.java](https://github.com/smanvi-pivotal/stats-cleaner/blob/master/src/main/java/StatsCleaner.java#L13) using the info from the above exception - 
* `corruptFilePath`
* `missingResourceName` (Ex: ClientSubscriptionStats_gf_non_durable_client_with_id_11.11.11.11(default_GemfireDS:1111:loner):2:GFNative_89kiXDueIe1111:default_GemfireDS_1_queue)
* `missingResourceTypeId`(Ex: 24) and run the class StatsCleaner. This will create a new corected file by replacing .gfs with `_cleaned.gfs` in the provied filename..

Run the class StatsCleaner after the above changes. A new gfs file will be created whose name is same as the corrupted file except that .gfs is replaced with _cleaned.gfs..
