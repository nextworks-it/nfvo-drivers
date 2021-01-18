package it.nextworks.nfvmano.nfvodriver.monitoring.driver.prometheus;

import it.nextworks.nfvmano.libs.common.enums.RelationalOperation;
import it.nextworks.nfvmano.libs.monit.interfaces.elements.ThresholdDetails;
import it.nextworks.nfvmano.libs.monit.interfaces.enums.ThresholdFormat;

/**
 * Created by Marco Capitani on 20/06/19.
 *
 * @author Marco Capitani <m.capitani AT nextworks.it>
 */
public class PrometheusTDetails extends ThresholdDetails {

    private int value;
    private RelationalOperation comparison;
    private int thresholdTime;
    private String pmJobId;

    public PrometheusTDetails(int value, RelationalOperation comparison, int thresholdTime, String pmJobId) {
        super(ThresholdFormat.PROMETHEUS);
        this.value = value;
        this.comparison = comparison;
        this.thresholdTime = thresholdTime;
        this.pmJobId = pmJobId;
    }

    public double getValue() {
        return value;
    }

    public RelationalOperation getRelationalOperation() {
        return comparison;
    }

    public int getThresholdTime() {
        return thresholdTime;
    }

    public String getPmJobId() {
        return pmJobId;
    }
}
