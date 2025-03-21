package com.codeflix.admin.catalog;

import com.codeflix.admin.catalog.domain.castmember.CastMemberType;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.generator.ArbitraryGeneratorContext;
import com.navercorp.fixturemonkey.api.jqwik.JavaArbitraryResolver;
import com.navercorp.fixturemonkey.api.jqwik.JqwikPlugin;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.arbitraries.StringArbitrary;

public final class Fixture {
    public static String name() {
        FixtureMonkey.create();

        return FixtureMonkey.builder()
                .plugin(
                        new JqwikPlugin()
                                .javaArbitraryResolver(new JavaArbitraryResolver() {
                                    @Override
                                    public Arbitrary<String> strings(StringArbitrary stringArbitrary, ArbitraryGeneratorContext context) {
                                        return stringArbitrary.ofMinLength(3).ofMaxLength(254);
                                    }
                                })
                )
                .build().giveMeOne(String.class);
    }

    public static final class CastMember {
        public static CastMemberType type() {
            return FixtureMonkey.create().giveMeArbitrary(CastMemberType.class).sample();
        }
    }
}