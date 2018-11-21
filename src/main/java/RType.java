import org.apache.geode.StatisticDescriptor;

/**
 * Created by smanvi on 2/21/17.
 * Similar to geode ResourceType.
 */
public class RType {

    String description;
    StatisticDescriptor[] statisticDescriptors;

    public RType(String description, StatisticDescriptor[] statisticDescriptors) {
        this.description = description;
        this.statisticDescriptors = statisticDescriptors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatisticDescriptor[] getStatisticDescriptors() {
        return statisticDescriptors;
    }

    public void setStatisticDescriptors(StatisticDescriptor[] statisticDescriptors) {
        this.statisticDescriptors = statisticDescriptors;
    }
}
