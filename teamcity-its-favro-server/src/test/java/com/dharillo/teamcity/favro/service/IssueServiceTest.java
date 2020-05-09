package com.dharillo.teamcity.favro.service;

import org.apache.commons.httpclient.Credentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class IssueServiceTest {
    @Mock
    Credentials credentialsMock;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Nested
    @DisplayName("getIssue")
    class GetIssue {
        private IssueService sut;
        @BeforeEach
        void setUp() {
            sut = new IssueService(credentialsMock);
        }
        @Test
        void invalidUrl() {
            assertThrows(IllegalArgumentException.class, () -> sut.getIssue(""));
        }
    }
}