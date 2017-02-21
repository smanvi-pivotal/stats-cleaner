import org.apache.geode.StatisticDescriptor;
import org.apache.geode.StatisticsTypeFactory;
import org.apache.geode.internal.statistics.StatisticsTypeFactoryImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by smanvi on 2/21/17.
 */
public class CleanerUtil {

    //Token defines a new resource type.
    public static final byte RESOURCE_TYPE_TOKEN = 1;

    //********** cacheClientProxyStat ***************************
    /** Name of the messages received statistic */
    private static final String MESSAGES_RECEIVED = "messagesReceived";
    /** Name of the messages queued statistic */
    private static final String MESSAGES_QUEUED = "messagesQueued";
    /** Name of the messages not queued because originator statistic */
    private static final String MESSAGES_NOT_QUEUED_ORIGINATOR = "messagesNotQueuedOriginator";
    /** Name of the messages not queued because not interested statistic */
    private static final String MESSAGES_NOT_QUEUED_NOT_INTERESTED = "messagesNotQueuedNotInterested";
    /** Name of the messages failed to be queued statistic */
    private static final String MESSAGES_FAILED_QUEUED = "messagesFailedQueued";
    /** Name of the message queue size statistic */
    private static final String MESSAGE_QUEUE_SIZE = "messageQueueSize";
    /** Name of the messages removed statistic */
    private static final String MESSAGES_PROCESSED = "messagesProcessed";
    /** Name of the message processing time statistic */
    private static final String MESSAGE_PROCESSING_TIME = "messageProcessingTime";
    /** Name of the delta messages sent statistic */
    private static final String DELTA_MESSAGES_SENT = "deltaMessagesSent";
    /** Name of the delta full messages sent statistic */
    private static final String DELTA_FULL_MESSAGES_SENT = "deltaFullMessagesSent";
    /** Name of the CQ count statistic */
    private static final String CQ_COUNT = "cqCount";


    //********** ClientSubscriptionStats ***************************
    /** Name of the events queued statistic */
    protected static final String EVENTS_QUEUED = "eventsQueued";
    /** Name of the events conflated statistic */
    protected static final String EVENTS_CONFLATED = "eventsConflated";
    /** Name of the marker events conflated statistic */
    protected static final String MARKER_EVENTS_CONFLATED = "markerEventsConflated";
    /** Name of the events removed statistic */
    protected static final String EVENTS_REMOVED = "eventsRemoved";
    /** Name of the events taken statistic */
    protected static final String EVENTS_TAKEN = "eventsTaken";
    /** Name of the events expired statistic */
    protected static final String EVENTS_EXPIRED = "eventsExpired";
    /** Name of the events removed by QRM statistic */
    protected static final String EVENTS_REMOVED_BY_QRM = "eventsRemovedByQrm";
    /** Name of the thread identifiers statistic */
    protected static final String THREAD_IDENTIFIERS = "threadIdentifiers";
    /** Name of the events dispatched statistic */
    protected static final String EVENTS_DISPATCHED = "eventsDispatched";
    protected static final String NUM_VOID_REMOVALS = "numVoidRemovals";
    protected static final String NUM_SEQUENCE_VIOLATED = "numSequenceViolated";


    static final String CACHE_CLIENT_PROXY_STATS="CacheClientProxyStats";
    static final String CLIENT_SUBSCRIPTION_STATS="ClientSubscriptionStats";

    static final StatisticsTypeFactory f = StatisticsTypeFactoryImpl.singleton();

    static Map<String, RType> resourceTypeMap = new HashMap<String, RType>();

    static{
        /**
         *  Keep evolving resourceTypeMap here, as and how other statsTypes need to be handled.
         */
        resourceTypeMap.put(CACHE_CLIENT_PROXY_STATS, createCacheClientProxyType());
        resourceTypeMap.put(CLIENT_SUBSCRIPTION_STATS, createClientSubscriptionType());
    }

    private static RType createClientSubscriptionType() {
        return new RType("ClientSubscriptionStats", new StatisticDescriptor[]{
                f.createLongCounter(EVENTS_QUEUED, "Number of events added to queue.", "operations"),
                f.createLongCounter(EVENTS_CONFLATED, "Number of events conflated for the queue.",
                        "operations"),
                f.createLongCounter(MARKER_EVENTS_CONFLATED,
                        "Number of marker events conflated for the queue.", "operations"),
                f.createLongCounter(EVENTS_REMOVED, "Number of events removed from the queue.",
                        "operations"),
                f.createLongCounter(EVENTS_TAKEN, "Number of events taken from the queue.", "operations"),
                f.createLongCounter(EVENTS_EXPIRED, "Number of events expired from the queue.",
                        "operations"),
                f.createLongCounter(EVENTS_REMOVED_BY_QRM, "Number of events removed by QRM message.",
                        "operations"),
                f.createIntCounter(THREAD_IDENTIFIERS, "Number of ThreadIdenfier objects for the queue.",
                        "units"),
                f.createLongCounter(EVENTS_DISPATCHED, "Number of events that have been dispatched.",
                        "operations"),
                f.createLongCounter(NUM_VOID_REMOVALS, "Number of void removals from the queue.",
                        "operations"),
                f.createLongCounter(NUM_SEQUENCE_VIOLATED, "Number of events that has violated sequence.",
                        "operations")

        });
    }


    /*
    Creates the Type data which will be written to the corrected file.
     */
    private static RType createCacheClientProxyType() {
        RType rType = new RType("CacheClientProxyStatistics", new StatisticDescriptor[]{
                f.createIntCounter(MESSAGES_RECEIVED, "Number of client messages received.", "operations"),
                f.createIntCounter(MESSAGES_QUEUED, "Number of client messages added to the message queue.",
                        "operations"),
                f.createIntCounter(MESSAGES_FAILED_QUEUED,
                        "Number of client messages attempted but failed to be added to the message queue.",
                        "operations"),
                f.createIntCounter(MESSAGES_NOT_QUEUED_ORIGINATOR,
                        "Number of client messages received but not added to the message queue because the receiving proxy represents the client originating the message.",
                        "operations"),
                f.createIntCounter(MESSAGES_NOT_QUEUED_NOT_INTERESTED,
                        "Number of client messages received but not added to the message queue because the client represented by the receiving proxy was not interested in the message's key.",
                        "operations"),
                f.createIntGauge(MESSAGE_QUEUE_SIZE, "Size of the message queue.", "operations"),
                f.createIntCounter(MESSAGES_PROCESSED,
                        "Number of client messages removed from the message queue and sent.", "operations"),
                f.createLongCounter(MESSAGE_PROCESSING_TIME,
                        "Total time spent sending messages to clients.", "nanoseconds"),
                f.createIntCounter(DELTA_MESSAGES_SENT,
                        "Number of client messages containing only delta bytes dispatched to the client.",
                        "operations"),
                f.createIntCounter(DELTA_FULL_MESSAGES_SENT,
                        "Number of client messages dispatched in reponse to failed delta at client.",
                        "operations"),
                f.createLongCounter(CQ_COUNT, "Number of CQs on the client.", "operations"),
                f.createLongCounter("sentBytes", "Total number of bytes sent to client.", "bytes")
        });

        return rType;
    }
}
