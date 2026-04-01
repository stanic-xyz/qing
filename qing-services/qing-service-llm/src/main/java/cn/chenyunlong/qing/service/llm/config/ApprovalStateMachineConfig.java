package cn.chenyunlong.qing.service.llm.config;

import cn.chenyunlong.qing.service.llm.enums.ApprovalEvent;
import cn.chenyunlong.qing.service.llm.enums.ApprovalState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Slf4j
@Configuration
@EnableStateMachineFactory(name = "approvalStateMachineFactory")
public class ApprovalStateMachineConfig extends EnumStateMachineConfigurerAdapter<ApprovalState, ApprovalEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<ApprovalState, ApprovalEvent> states) throws Exception {
        states
            .withStates()
            .initial(ApprovalState.DRAFT)
            .states(EnumSet.allOf(ApprovalState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<ApprovalState, ApprovalEvent> transitions) throws Exception {
        transitions
            .withExternal()
                .source(ApprovalState.DRAFT).target(ApprovalState.PENDING)
                .event(ApprovalEvent.SUBMIT)
                .and()
            .withExternal()
                .source(ApprovalState.PENDING).target(ApprovalState.EFFECTIVE)
                .event(ApprovalEvent.APPROVE)
                .and()
            .withExternal()
                .source(ApprovalState.PENDING).target(ApprovalState.REJECTED)
                .event(ApprovalEvent.REJECT)
                .and()
            .withExternal()
                .source(ApprovalState.REJECTED).target(ApprovalState.DRAFT)
                .event(ApprovalEvent.SUBMIT);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<ApprovalState, ApprovalEvent> config) throws Exception {
        config.withConfiguration()
            .listener(new StateMachineListenerAdapter<ApprovalState, ApprovalEvent>() {
                @Override
                public void stateChanged(State<ApprovalState, ApprovalEvent> from, State<ApprovalState, ApprovalEvent> to) {
                    log.info("Approval State changed from {} to {}", from != null ? from.getId() : "none", to.getId());
                }
            });
    }
}
