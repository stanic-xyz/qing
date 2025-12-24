package cn.chenyunlong.qing.infrastructure.email.api.vo;

public class EmailMessage {
    private String to;
    private String subject;
    private String content;
    private boolean isHtml;

    public EmailMessage() {
    }

    public EmailMessage(String to, String subject, String content, boolean isHtml) {
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.isHtml = isHtml;
    }

    public static EmailMessageBuilder builder() {
        return new EmailMessageBuilder();
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public static class EmailMessageBuilder {
        private final EmailMessage msg = new EmailMessage();

        public EmailMessageBuilder to(String to) {
            msg.to = to;
            return this;
        }

        public EmailMessageBuilder subject(String subject) {
            msg.subject = subject;
            return this;
        }

        public EmailMessageBuilder content(String content) {
            msg.content = content;
            return this;
        }

        public EmailMessageBuilder isHtml(boolean isHtml) {
            msg.isHtml = isHtml;
            return this;
        }

        public EmailMessage build() {
            return msg;
        }
    }
}
