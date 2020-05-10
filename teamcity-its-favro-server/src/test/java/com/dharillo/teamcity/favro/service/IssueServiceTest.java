package com.dharillo.teamcity.favro.service;

import com.dharillo.teamcity.favro.auth.FavroUsernamePasswordCredentials;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.*;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class IssueServiceTest {
    @Mock
    FavroUsernamePasswordCredentials credentialsMock;
    @Mock
    HttpClient clientMock;
    @Mock
    HttpResponse responseMock;
    @Mock
    HttpEntity responseEntityMock;
    @Mock
    StatusLine statusLineMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Nested
    @DisplayName("getIssue")
    class GetIssue {
        private IssueService sut;
        private final String organization = "testOrganization";
        private final String validIssueUrl = "https://favro.com/organization/4b90bc5348125e5242281e6d/?card=Gol-7";
        @BeforeEach
        void setUp() throws IOException {
            sut = new IssueService(credentialsMock, clientMock);
            Mockito.when(credentialsMock.getOrganization()).thenReturn(organization);
            Mockito.when(clientMock.execute(any(HttpGet.class))).thenReturn(responseMock);
            Mockito.when(responseMock.getEntity()).thenReturn(responseEntityMock);
            Mockito.when(responseEntityMock.getContent()).thenReturn(getResource("validIssue.json"));
            Mockito.when(responseMock.getStatusLine()).thenReturn(statusLineMock);
            Mockito.when(statusLineMock.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        }
        @Test
        void throwsWithInvalidUrl() {
            assertThrows(IllegalArgumentException.class, () -> sut.getIssue("")); // Assert
        }
        @Test
        void callsWithCorrectMethod() throws IOException {
            sut.getIssue("https://favro.com/organization/someid/?card=Test-1"); // Act
            Mockito.verify(clientMock).execute(any(HttpGet.class)); // Assert
        }
        @Test
        void setsOrganizationHeader() throws IOException {
            // Arrange
            ArgumentCaptor<HttpGet> requestCaptor = ArgumentCaptor.forClass(HttpGet.class);
            // Act
            sut.getIssue("https://favro.com/organization/testOrganization/?card=Test-1");
            // Assert
            Mockito.verify(clientMock).execute(requestCaptor.capture());
            Header organizationHeader = requestCaptor.getValue().getFirstHeader("organizationId");
            assertNotNull(organizationHeader);
            assertEquals(organization, organizationHeader.getValue());
        }
        @Test
        void setsAuthHeader() throws IOException {
            // Arrange
            ArgumentCaptor<HttpGet> requestCaptor = ArgumentCaptor.forClass(HttpGet.class);
            // Act
            sut.getIssue("https://favro.com/organization/testOrganization/?card=Test-1");
            // Assert
            Mockito.verify(clientMock).execute(requestCaptor.capture());
            Header authHeader = requestCaptor.getValue().getFirstHeader(HttpHeaders.AUTHORIZATION);
            assertNotNull(authHeader);
        }
        @Test
        void usesCorrectPath() throws IOException {
            // Arrange
            ArgumentCaptor<HttpGet> requestCaptor = ArgumentCaptor.forClass(HttpGet.class);
            // Act
            sut.getIssue("https://favro.com/organization/testOrganization/?card=Test-1");
            Mockito.verify(clientMock).execute(requestCaptor.capture());
            URI requestUri = requestCaptor.getValue().getURI();
            // Assert
            assertEquals("/api/v1/cards/", requestUri.getPath());
        }
        @Test
        void usesCorrectHost() throws IOException {
            // Arrange
            ArgumentCaptor<HttpGet> requestCaptor = ArgumentCaptor.forClass(HttpGet.class);
            // Act
            sut.getIssue("https://favro.com/organization/testOrganization/?card=Test-1");
            Mockito.verify(clientMock).execute(requestCaptor.capture());
            URI requestUri = requestCaptor.getValue().getURI();
            // Assert
            assertEquals("favro.com", requestUri.getHost());
        }
        @Test
        void usesCorrectParameters() throws IOException {
            // Arrange
            ArgumentCaptor<HttpGet> requestCaptor = ArgumentCaptor.forClass(HttpGet.class);
            // Act
            sut.getIssue("https://favro.com/organization/testOrganization/?card=Test-1");
            Mockito.verify(clientMock).execute(requestCaptor.capture());
            URI requestUri = requestCaptor.getValue().getURI();
            // Assert
            assertEquals("unique=true&cardSequentialId=1", requestUri.getQuery());
        }
        @Test
        void usesCorrectScheme() throws IOException {
            // Arrange
            ArgumentCaptor<HttpGet> requestCaptor = ArgumentCaptor.forClass(HttpGet.class);
            // Act
            sut.getIssue("https://favro.com/organization/testOrganization/?card=Test-1");
            Mockito.verify(clientMock).execute(requestCaptor.capture());
            URI requestUri = requestCaptor.getValue().getURI();
            // Assert
            assertEquals("https", requestUri.getScheme());
        }
        @Test
        void returnsIssue() {
            FavroIssue issue = sut.getIssue(validIssueUrl); // Act
            assertNotNull(issue); // Assert
        }
        @Test
        void indicatesIssueSequentialId() {
            FavroIssue issue = sut.getIssue(validIssueUrl); // Act
            assertEquals(7, issue.getSequentialId()); // Assert
        }
        @Test
        void indicatesIssueCommonId() {
            FavroIssue issue = sut.getIssue(validIssueUrl); // Act
            assertEquals("b03cf86c39b46996e31da014", issue.getCardCommonId()); // Assert
        }
        @Test
        void indicatesIssueName() {
            FavroIssue issue = sut.getIssue(validIssueUrl); // Act
            assertEquals("Test card title", issue.getName()); // Assert
        }
        @Test
        void indicatesIssueUrl() {
            FavroIssue issue = sut.getIssue(validIssueUrl); // Act
            assertEquals(validIssueUrl, issue.getUrl()); // Assert
        }
        @Test
        void indicatesIssueDescription() {
            FavroIssue issue = sut.getIssue(validIssueUrl); // Act
            assertEquals("Description\n\nSome description", issue.getDescription()); // Assert
        }
        @Test
        void indicatesIssueArchivedStatus() {
            FavroIssue issue = sut.getIssue(validIssueUrl); // Act
            assertFalse(issue.isArchived()); // Assert
        }
        @Test
        void indicatesIssueState() {
            FavroIssue issue = sut.getIssue(validIssueUrl); // Act
            assertEquals("Open", issue.getState()); // Assert
        }
        @Test
        void indicatesIssueStartDate() throws ParseException {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // Arrange
            FavroIssue issue = sut.getIssue(validIssueUrl); // Act
            assertEquals(format.parse("2020-04-20T07:00:00.000Z"), issue.getStartDate()); // Assert
        }
        @Test
        void indicatesIssueDueDate() throws ParseException {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // Arrange
            FavroIssue issue = sut.getIssue(validIssueUrl); // Act
            assertEquals(format.parse("2020-05-15T08:00:00.000Z"), issue.getDueDate()); // Assert
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

    @Nullable
    public InputStream getResource(@NotNull final String path) {
        ClassLoader loader = getClass().getClassLoader();
        return loader.getResourceAsStream(path);
    }
}