package com.dharillo.teamcity.favro.service;

import jetbrains.buildServer.issueTracker.Issue;
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

    @Nested
    @DisplayName("getSequentialId")
    class GetSequentialId {
        @Test
        void parsesWithoutCollection() {
            final String url = "https://favro.com/organization/4b90bc5348125e5242281e6d/?card=Gol-57405";
            final int id = IssueService.getSequentialId(url);
            assertEquals(57405, id);
        }
        @Test
        void parsesWithCollection() {
            final String url = "https://favro.com/organization/4b90bc5348125e5242281e6d/516bb94010779e724fc029ba?card=Gol-57405";
            final int id = IssueService.getSequentialId(url);
            assertEquals(57405, id);
        }
        @Test
        void throwsWithInvalidUrl() {
            final String url = "https://www.google.com/?card=Pet-4321";
            assertThrows(IllegalArgumentException.class, () -> IssueService.getSequentialId(url));
        }
    }
}