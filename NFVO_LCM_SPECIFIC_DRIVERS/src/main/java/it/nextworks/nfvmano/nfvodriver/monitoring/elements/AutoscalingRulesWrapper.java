package it.nextworks.nfvmano.nfvodriver.monitoring.elements;

import it.nextworks.nfvmano.libs.ifa.common.enums.LogicOperation;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.AutoscalingAction;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.AutoscalingRuleCondition;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.AutoscalingRuleCriteria;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsAutoscalingRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoscalingRulesWrapper {

    public static final Logger log = LoggerFactory.getLogger(AutoscalingRulesWrapper.class);

    private Map<String, AutoscalingRuleCondition> conditions = new HashMap<>();
    private Map<String, NsAutoscalingRule> condition2rule = new HashMap<>();
    private Map<String, Instant> condition2lastTrigger = new HashMap<>();
    private Map<String, AutoscalingRuleCriteria> criteria = new HashMap<>();
    private Map<String, AutoscalingRuleCondition> criterion2condition = new HashMap<>();
    private Map<String, Boolean> criteriaState = new HashMap<>();
    private Instant backoff = Instant.EPOCH;

    public AutoscalingRulesWrapper(List<NsAutoscalingRule> rules) {
        for (NsAutoscalingRule rule : rules) {
            AutoscalingRuleCondition condition = rule.getRuleCondition();
            String condName = condition.getName();
            if (conditions.containsKey(condName)) {
                throw new IllegalArgumentException(String.format(
                        "Condition name %s duplicated",
                        condName
                ));
            }
            conditions.put(condName, condition);
            condition2rule.put(condName, rule);
            condition2lastTrigger.put(condName, Instant.EPOCH);
            for (AutoscalingRuleCriteria criterion : condition.getScalingCriteria()) {
                String crtName = criterion.getName();
                if (criteria.containsKey(crtName)) {
                    throw new IllegalArgumentException(String.format(
                            "Criterion name %s duplicated",
                            crtName
                    ));
                }
                criteria.put(crtName, criterion);
                criterion2condition.put(crtName, condition);
                criteriaState.put(crtName, false);
            }
        }
    }

    public List<AutoscalingAction> fireCriterion(String criterionId) {
        if (criteriaState.get(criterionId)) {
            log.debug("Criterion {} already firing", criterionId);
        }
        criteriaState.put(criterionId, true);
        List<AutoscalingAction> autoscalingActions = getTriggeredActions();
        return getTriggeredActions();
    }

    public void stopFiring(String criterionId) {
        if (!criteriaState.get(criterionId)) {
            throw new IllegalArgumentException(String.format(
                    "Criterion %s in illegal state for stop",
                    criterionId
            ));
        }
        criteriaState.put(criterionId, false);
    }

    private List<AutoscalingAction> getTriggeredActions() {
        return criterion2condition.values().stream()
                .filter(this::getConditionState)  // triggered conditions
                .map(c -> condition2rule.get(c.getName())) // triggered rules
                .map(NsAutoscalingRule::getRuleActions)  // Stream<List<Action> actions to be undertaken
                .flatMap(Collection::stream)  // Flatten to a single list
                .collect(Collectors.toList());
    }

    private Boolean getConditionState(String conditionId) {
        AutoscalingRuleCondition condition = conditions.get(conditionId);
        if (null == condition) {
            throw new IllegalArgumentException(String.format("No such condition %s", conditionId));
        }
        return getConditionState(condition);
    }

    private LogicOperation getActualOperation(AutoscalingRuleCondition condition) {
        LogicOperation operation;
        if (condition.getScaleInOperationType() == null) {
            if (condition.getScaleOutOperationType() == null) {
                return LogicOperation.AND;
            }
            operation = condition.getScaleOutOperationType();
        } else {
            if (condition.getScaleOutOperationType() != null) {
                throw new IllegalArgumentException(String.format(
                        "Both scaleIn and scaleOut operation type non null in condition %s",
                        condition.getName()
                ));
            }
            operation = condition.getScaleInOperationType();
        }
        return operation;
    }

    private void setGeneralBackoff(AutoscalingRuleCondition condition) {
        Instant newBackoff = Instant.now().plusSeconds(condition.getCooldownTime());
        if (newBackoff.isAfter(backoff)) {
            backoff = newBackoff;
        }
    }

    private boolean checkConditionCriteria(AutoscalingRuleCondition condition) {
        LogicOperation operation = getActualOperation(condition);
        Stream<Boolean> states = condition.getScalingCriteria().stream()
                .map(crt -> criteriaState.get(crt.getName()));

        boolean out;
        switch (operation) {
            case AND:
                out = states.allMatch(s -> s);
                break;
            case OR:
                out = states.anyMatch(s -> s);
                break;
            default:
                throw new IllegalArgumentException(String.format(
                        "Unsupported logicOperation %s",
                        operation
                ));
        }
        if (out) {
            setGeneralBackoff(condition);
        }
        return out;
    }

    private boolean getConditionState(AutoscalingRuleCondition condition) {
        // Check general backoff  TODO: review...
        if (backoff.isAfter(Instant.now())) {
            return false;
        }

        // Check backoff
        Instant instant = condition2lastTrigger.get(condition.getName());
        if (instant.plusSeconds(condition.getCooldownTime()).isAfter(Instant.now())) {
            return false;
        }

        return checkConditionCriteria(condition);
    }

    public Map<AutoscalingRuleCriteria, Integer> getCriteriaAndThresholdTimes() {
        return criteria.values().stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        crt -> criterion2condition.get(crt.getName()).getThresholdTime()
                ));
    }
}