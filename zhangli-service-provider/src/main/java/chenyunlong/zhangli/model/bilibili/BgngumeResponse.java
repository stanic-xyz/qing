
package chenyunlong.zhangli.model.bilibili;

import com.google.gson.annotations.Expose;

public class BgngumeResponse {

    @Expose
    private Long code;
    @Expose
    private AnimeData data;
    @Expose
    private String message;

    public Long getCode() {
        return code;
    }

    public AnimeData getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class Builder {

        private Long code;
        private AnimeData data;
        private String message;

        public Builder withCode(Long code) {
            this.code = code;
            return this;
        }

        public Builder withData(AnimeData data) {
            this.data = data;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public BgngumeResponse build() {
            BgngumeResponse bgngumeResponse = new BgngumeResponse();
            bgngumeResponse.code = code;
            bgngumeResponse.data = data;
            bgngumeResponse.message = message;
            return bgngumeResponse;
        }

    }

}
