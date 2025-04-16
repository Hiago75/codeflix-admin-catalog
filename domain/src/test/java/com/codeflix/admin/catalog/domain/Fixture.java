package com.codeflix.admin.catalog.domain;

import com.codeflix.admin.catalog.domain.castmember.CastMember;
import com.codeflix.admin.catalog.domain.castmember.CastMemberType;
import com.codeflix.admin.catalog.domain.category.Category;
import com.codeflix.admin.catalog.domain.genre.Genre;
import com.codeflix.admin.catalog.domain.resource.Resource;
import com.codeflix.admin.catalog.domain.utils.IdUtils;
import com.codeflix.admin.catalog.domain.video.*;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        double originalValue = FAKER.random().nextDouble(120.0, 190.0);
        BigDecimal bd = new BigDecimal(Double.toString(originalValue));

        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String checksum() {
        return "03fe62de";
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
        private static final Category MOVIES = Category.newCategory("Movies", "Movies about anything", true);

        public static Category documentary() {
            return Category.with(DOCUMENTARY);
        }
        public static Category movies() {
            return Category.with(MOVIES);
        }
    }

    public static final class Genres {
        private static final Genre TECH = Genre.newGenre("Technology", true);
        private static final Genre BUSINESS = Genre.newGenre("Business", true);

        public static Genre tech() {
            return Genre.with(TECH);
        }
        public static Genre business() {
            return Genre.with(BUSINESS);
        }
    }

    public static final class Videos {

        private static final Video SYSTEM_DESIGN = Video.newVideo(
                "System Design no Mercado Livre na prática",
                description(),
                Year.of(2022),
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

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }

        public static VideoMediaType mediaType() {
            return FAKER.options().option(VideoMediaType.values());
        }

        public static Resource resource(final VideoMediaType type) {
            final String contentType = Match(type).of(
                    Case($(List(VideoMediaType.VIDEO, VideoMediaType.TRAILER)::contains), "video/mp4"),
                    Case($(), "image/jpg")
            );

            final String checksum = IdUtils.uuid();
            final byte[] content = "Conteudo".getBytes();

            return Resource.of(checksum, content, contentType, type.name().toLowerCase());
        }

        public static String description() {
            return FAKER.options().option(
                    """
                            Descrição bem legal do vídeo, com informações relevantes sobre o conteúdo abordado.
                            """,
                    """
                            Outra descrição legal do vídeo, com informações relevantes sobre o conteúdo abordado.
                            """
            );
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
}
