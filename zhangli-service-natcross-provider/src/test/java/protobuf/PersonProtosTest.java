package protobuf;

import org.junit.jupiter.api.Test;
import stan.zhangli.protobuf.PersonProtos;

import java.io.IOException;

class PersonProtosTest {

    @Test
    public void testProtobuf() throws IOException {
        PersonProtos.Person john = PersonProtos.Person.newBuilder()
                .setId(1234)
                .setName("John Doe")
                .setEmail("jdoe@example.com")
                .addPhone(PersonProtos.Person.PhoneNumber.newBuilder()
                        .setNumber("555-4321")
                        .setType(PersonProtos.Person.PhoneType.HOME))
                .build();
        System.out.println(john.toString());
    }

}