package com.codeflix.admin.catalog.application;

import com.codeflix.admin.catalog.domain.castmember.CastMember;
import com.codeflix.admin.catalog.domain.castmember.CastMemberType;
import com.codeflix.admin.catalog.domain.category.Category;
import com.codeflix.admin.catalog.domain.genre.Genre;
import com.codeflix.admin.catalog.domain.video.*;
import net.datafaker.Faker;

import java.time.Year;
import java.util.Set;

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

    public static Video video() {
        return Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.Videos.rating(),
                Fixture.bool(),
                Fixture.bool(),
                Set.of(Categories.documentary().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.johnDoe().getId(), CastMembers.janeDoe().getId())
        );
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
        private final static Video SYSTEM_DESIGN = Video.newVideo(
                "System Design",
                description(),
                Year.of(2002),
                Fixture.duration(),
                rating(),
                Fixture.bool(),
                Fixture.bool(),
                Set.of(Categories.documentary().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.johnDoe().getId(), CastMembers.janeDoe().getId())
        );

        public static Video systemDesign() {
            return Video.with(SYSTEM_DESIGN);
        }

        public static Resource resource(final VideoMediaType type) {
            final String contentType = Match(type).of(
                    Case($(List(VideoMediaType.VIDEO, VideoMediaType.TRAILER)::contains), "video/mp4"),
                    Case($(), "image/jpeg")
            );

            final byte[] content = "Content".getBytes();
            return Resource.of(content, contentType, type.name().toLowerCase(), type);
        }

        public static String description() {
            return FAKER.lorem().characters(1500);
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }
    }

    public static String checksum() {
        return "03fe62de";
    }

    public static AudioVideoMedia audioVideo(final VideoMediaType type) {
        final var checksum = Fixture.checksum();
        return AudioVideoMedia.with(
                checksum,
                type.name().toLowerCase(),
                "/videos/" + checksum
        );
    }

    public static ImageMedia image(final VideoMediaType type) {
        final var checksum = Fixture.checksum();
        return ImageMedia.with(
                checksum,
                type.name().toLowerCase(),
                "/images/" + checksum
        );
    }
}
