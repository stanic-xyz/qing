package cn.chenyunlong.security.deserializes;

import cn.chenyunlong.security.entity.AuthUser;
import cn.chenyunlong.security.userdetails.TemporaryUser;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Set;

public class TemporaryUserDeserializer extends StdDeserializer<TemporaryUser> {

    public TemporaryUserDeserializer() {
        super(TemporaryUser.class);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public TemporaryUser deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode jsonNode = mapper.readTree(p);

        Set<? extends GrantedAuthority> authorities = mapper.convertValue(jsonNode.get("authorities"), new TypeReference<Set<SimpleGrantedAuthority>>() {
        });
        JsonNode password = this.readJsonNode(jsonNode, "password");
        TemporaryUser result = new TemporaryUser(this.readJsonNode(jsonNode, "username").asText(), password.asText(""), this.readJsonNode(jsonNode, "enabled").asBoolean(), this.readJsonNode(jsonNode, "accountNonExpired").asBoolean(), this.readJsonNode(jsonNode, "credentialsNonExpired").asBoolean(), this.readJsonNode(jsonNode, "accountNonLocked").asBoolean(), authorities, mapper.convertValue(jsonNode.get("authUser"), new TypeReference<AuthUser>() {
        }), jsonNode.get("encodeState").asText());

        if (password.asText(null) == null) {
            result.eraseCredentials();
        }

        return result;
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        if (jsonNode.has(field)) {
            return jsonNode.get(field);
        }
        return MissingNode.getInstance();
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
    @JsonDeserialize(using = TemporaryUserDeserializer.class)
    public interface TemporaryUserMixin {
    }

}