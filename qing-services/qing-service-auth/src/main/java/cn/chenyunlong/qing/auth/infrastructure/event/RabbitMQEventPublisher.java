package cn.chenyunlong.qing.auth.infrastructure.event;

//@Component
//public class RabbitMQEventPublisher implements DomainEventPublisher {
//    private final RabbitTemplate rabbitTemplate;
//
//    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//        // 设置事件发布器
//        //        DomainEventPublisher.setPublisher(this);
//    }
//
//    @Override
//    public void publish(DomainEvent event) {
//        rabbitTemplate.convertAndSend("event.exchange", "event.routing-key", event);
//    }
//
//    @Override
//    public void publishAll(Collection<Object> events) {
//
//    }
//}
