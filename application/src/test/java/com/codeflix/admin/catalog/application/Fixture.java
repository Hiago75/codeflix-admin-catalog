package com.codeflix.admin.catalog.application;

import com.codeflix.admin.catalog.domain.castmember.CastMember;
import com.codeflix.admin.catalog.domain.castmember.CastMemberType;
import com.codeflix.admin.catalog.domain.category.Category;
import com.codeflix.admin.catalog.domain.genre.Genre;
import com.codeflix.admin.catalog.domain.video.Rating;
import com.codeflix.admin.catalog.domain.video.Resource;
import net.datafaker.Faker;

import java.util.Arrays;

import static io.vavr.API.*;

public final class Fixture {
    private static final Faker FAKER = new Faker();

    public static Boolean bool() {
        return FAKER.bool().bool();
    }

    public static String name() {
        return FAKER.name().fullName();
    }

    public static String title() {
        return FAKER.book().title();
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020, 2030);
    }

    public static Double duration() {
        return FAKER.random().nextDouble(120.0, 190.0);
    }

    public static final class CastMembers {
        private static final CastMember JOHN_DOE = CastMember.newMember("John Doe", CastMemberType.DIRECTOR);
        private static final CastMember JANE_DOE = CastMember.newMember("Jane Doe", CastMemberType.ACTOR);

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.values());
        }

        public static CastMember johnDoe() {
            return CastMember.with(JOHN_DOE);
        }

        public static CastMember janeDoe() {
            return CastMember.with(JANE_DOE);
        }
    }

    public static final class Categories {
        private static final Category DOCUMENTARY = Category.newCategory("Documentary", "Documentaries about real-life events", true);

        public static Category documentary() {
            return Category.with(DOCUMENTARY);
        }
    }

    public static final class Genres {
        private static final Genre TECH = Genre.newGenre("Technology", true);

        public static Genre tech() {
            return Genre.with(TECH);
        }
    }

    public static final class Videos {
        public static Resource resource(final Resource.Type type) {
            final String contentType = Match(type).of(
                    Case($(List(Resource.Type.VIDEO, Resource.Type.TRAILER)::contains), "video/mp4"),
                    Case($(), "image/jpeg")
            );

            final byte[] content = "Content".getBytes();
            return Resource.of(content, contentType, type.name().toLowerCase(), type);
        }

        public static String description() {
            return FAKER.lorem().paragraph(255);
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }
    }
}
