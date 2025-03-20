package com.codeflix.admin.catalog;

import com.codeflix.admin.catalog.domain.castmember.CastMemberType;
import com.navercorp.fixturemonkey.FixtureMonkey;

public final class Fixture {
    public static String name() {
        return FixtureMonkey.create().giveMeOne(String.class);
    }

    public static final class CastMember {
        public static CastMemberType type() {
            return FixtureMonkey.create().giveMeArbitrary(CastMemberType.class).sample();
        }
    }
}