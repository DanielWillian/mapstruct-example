///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.projectlombok:lombok:1.18.30
//DEPS org.mapstruct:mapstruct:1.5.5.Final
//DEPS org.mapstruct:mapstruct-processor:1.5.5.Final

import lombok.Builder;
import lombok.Value;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static java.lang.System.*;

public class MapStructExample {

    public static void main(String... args) {
        UserPost post = UserPost.of("first", "last");
        User user = UserMapper.INSTANCE.fromPost(post, "123", LocalDate.parse("2023-11-12"));
        UserResponse response = UserMapper.INSTANCE.toResponse(user);
        out.printf("post: %s%nuser: %s%nresponse: %s%n", post, user, response);
    }

    @Value(staticConstructor = "of")
    public static class UserId {
        String id;
    }

    @Value
    @Builder
    public static class User {
        UserId userId;
        String firstName;
        String lastName;
        LocalDate creationDate;
    }

    @Value
    @Builder
    public static class UserResponse {
        String firstName;
        String lastName;
        String createdDate;
    }

    @Value(staticConstructor = "of")
    public static class UserPost {
        String nameFirst;
        String nameLast;
    }

    @Mapper
    public interface UserMapper {
        UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

        @Mapping(source = "post.nameFirst", target = "firstName")
        @Mapping(source = "post.nameLast", target = "lastName")
        @Mapping(source = "id", target = "userId")
        User fromPost(UserPost post, String id, LocalDate creationDate);

        @Mapping(source = "creationDate", target = "createdDate")
        UserResponse toResponse(User user);

        default UserId toUserId(String id) {
            return UserId.of(id);
        }
    }
}
