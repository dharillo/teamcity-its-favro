package com.dharillo.teamcity.favro;

import jetbrains.buildServer.issueTracker.IssueMention;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dharillo.teamcity.favro.FavroConstants.*;
import static com.dharillo.teamcity.favro.FavroIssueProviderType.DEFAULT_ISSUE_PATTERN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class FavroIssueProviderTest {
    @Mock
    FavroIssueProviderType issueProviderTypeMock;
    @Mock
    FavroIssueFetcher issueFetcherMock;
    Map<String, String> defaultProperties;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(issueProviderTypeMock.getType()).thenReturn("Favro");
        defaultProperties = new HashMap<>();
        defaultProperties.put(PARAM_PATTERN, DEFAULT_ISSUE_PATTERN);
        defaultProperties.put(PARAM_AUTH_TYPE, AUTH_LOGIN_PASSWORD);
        Mockito.when(issueProviderTypeMock.getDefaultProperties()).thenReturn(defaultProperties);
    }

    @Nested
    @DisplayName("constructor")
    class Constructor {
        @Test
        void createsInstance() {
            FavroIssueProvider sut = new FavroIssueProvider(issueProviderTypeMock, issueFetcherMock);
            assertNotNull(sut);
        }
        @Test
        void setsType() {
            FavroIssueProvider sut = new FavroIssueProvider(issueProviderTypeMock, issueFetcherMock);
            assertEquals("Favro", sut.getType());
        }
    }

    @Nested
    @DisplayName("setProperties")
    class SetProperties {
        @Spy
        FavroIssueProvider sut;

        FavroIssueProviderFactory factory;
        Map<String, String> defaultProperties;
        @BeforeEach
        void setUp() {
            factory = new FavroIssueProviderFactory(issueProviderTypeMock, issueFetcherMock);
            sut = (FavroIssueProvider)factory.createProvider();
            defaultProperties = issueProviderTypeMock.getDefaultProperties();
            MockitoAnnotations.initMocks(this);
        }
        @Test
        void setsHostProperty() {
            sut.setProperties(defaultProperties); // Act
            assertNotNull(sut.getProperties().get("host")); // Assert
        }
        @Test
        void buildsHostPropertyWithOrganizationParam() {
            defaultProperties.put(PARAM_ORGANIZATION, "testOrganization"); // Arrange
            sut.setProperties(defaultProperties); // Act
            assertEquals("https://favro.com/organization/testOrganization/", sut.getProperties().get("host")); // Assert
        }
        @Test
        void callsCompilePattern() {
            sut.setProperties(defaultProperties); // Act
            Mockito.verify(sut).compilePattern(any()); // Assert
        }
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = { " ", "\n", "\t" })
        void addsDefaultPatternIfPropertyIsInvalid(String pattern) {
            // Arrange
            Map<String, String> incompleteProperties = new HashMap<>();
            incompleteProperties.put(PARAM_AUTH_TYPE, AUTH_LOGIN_PASSWORD);
            if (pattern != null) {
                incompleteProperties.put(PARAM_PATTERN, pattern);
            }
            // Act
            sut.setProperties(incompleteProperties);
            // Assert
            assertNotNull(sut.getProperties().get(PARAM_PATTERN));
        }
    }

    @Nested
    @DisplayName("compilePattern")
    class CompilePattern {
        FavroIssueProvider sut;
        FavroIssueProviderFactory factory;
        Map<String, String> defaultProperties;
        @BeforeEach
        void setUp() {
            factory = new FavroIssueProviderFactory(issueProviderTypeMock, issueFetcherMock);
            sut = (FavroIssueProvider)factory.createProvider();
            defaultProperties = issueProviderTypeMock.getDefaultProperties();
        }
        @Test
        void createsPattern() {
            Pattern pattern = sut.compilePattern(defaultProperties); // Act
            assertNotNull(pattern); // Assert
        }
        @Test
        void createdPatternDetectsIssue() {
            Pattern pattern = sut.compilePattern(defaultProperties); // Act
            assertTrue(pattern.matcher("#TIS-893").matches()); // Assert
        }
        @Test
        void firstGroupReturnsIssueIdentifier() {
            // Act
            Pattern pattern = sut.compilePattern(defaultProperties);
            // Assert
            Matcher match = pattern.matcher("#GOL-321");
            assertTrue(match.find() && match.groupCount() >= 1);
            assertEquals("GOL-321", match.group(1));
        }
    }

    @Nested
    @DisplayName("extractId")
    class ExtractId {
        FavroIssueProvider sut;
        FavroIssueProviderFactory factory;
        Map<String, String> defaultProperties;
        @BeforeEach
        void setUp() {
            factory = new FavroIssueProviderFactory(issueProviderTypeMock, issueFetcherMock);
            sut = (FavroIssueProvider)factory.createProvider();
            defaultProperties = issueProviderTypeMock.getDefaultProperties();
            sut.setProperties(defaultProperties);
        }
        @Test
        void extractsIdFromShortComment() {
            String result = sut.extractId("#TVP-123"); // Act
            assertEquals("TVP-123", result); // Assert
        }
        @Test
        void extractsIdFromLongComment() {
            String result = sut.extractId("This commit solves the #TVP-321 issue");
            assertEquals("TVP-321", result);
        }
        @Test
        void returnsCommentUpperCasedForNonMatchingComment() {
            String comment = "This commit does not reference any issue";
            String result = sut.extractId("This commit does not reference any issue");
            assertEquals(comment.toUpperCase(), result);
        }
    }

    @Nested
    @DisplayName("getRelatedIssues")
    class GetRelatedIssues {
        FavroIssueProvider sut;
        FavroIssueProviderFactory factory;
        Map<String, String> defaultProperties;
        @BeforeEach
        void setUp() {
            factory = new FavroIssueProviderFactory(issueProviderTypeMock, issueFetcherMock);
            sut = (FavroIssueProvider)factory.createProvider();
            defaultProperties = issueProviderTypeMock.getDefaultProperties();
            sut.setProperties(defaultProperties);
        }
        @Test
        void returnsEmptyListForCommentWithoutIssueMentions() {
            Collection<IssueMention> issues = sut.getRelatedIssues("Normal comment");
            assertEquals(0, issues.size());
        }
        @Test
        void returnsSingleItemForCommentWithTaskMention() {
            Collection<IssueMention> issues = sut.getRelatedIssues("Finishes #TEST-12");
            assertEquals(1, issues.size());
        }
        @Test
        void returnedIssueHasCorrectInformation() {
            Collection<IssueMention> issues = sut.getRelatedIssues("Finishes #TEST-12");
            IssueMention issue = issues.iterator().next();
            assertEquals("TEST-12", issue.getId());
        }
        @Test
        void detectsMultipleIssueMentions() {
            Collection<IssueMention> issues = sut.getRelatedIssues("Finishes #TEST-12 #TEST-13 and #TEST-14");
            assertEquals(3, issues.size());
        }
    }
}
