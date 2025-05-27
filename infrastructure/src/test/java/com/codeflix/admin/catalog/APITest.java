package com.codeflix.admin.catalog;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

public interface APITest {
    String ROLE_ADMIN = "ROLE_CATALOG_ADMIN";
    String ROLE_CATEGORIZATION = "ROLE_CATALOG_ADMIN_CATEGORIZATION";
    String ROLE_VIDEOS = "ROLE_CATALOG_ADMIN_VIDEOS";

    JwtRequestPostProcessor ADMIN_JWT = jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN));
    JwtRequestPostProcessor CATEGORIZATION_JWT = jwt().authorities(new SimpleGrantedAuthority(ROLE_CATEGORIZATION));
    JwtRequestPostProcessor VIDEOS_JWT = jwt().authorities(new SimpleGrantedAuthority(ROLE_VIDEOS));
}
