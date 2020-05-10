package com.dharillo.teamcity.favro;

import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static com.dharillo.teamcity.favro.FavroConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

public class FavroIssueProviderTypeTest {
    @Mock
    PluginDescriptor pluginDescriptorMock;
    final String configUrl = "admin/editIssueProvider.jsp";
    final String issueUrl = "popup.jsp";
    final String defaultUrl = "test/page.jsp";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(pluginDescriptorMock.getPluginResourcesPath(anyString())).thenReturn(defaultUrl);
    }

    @Nested
    @DisplayName("constructor")
    class Constructor {
        @Test
        void createsInstance() {
            FavroIssueProviderType sut = new FavroIssueProviderType(pluginDescriptorMock); // Act
            assertNotNull(sut); // Assert
        }
        @Test
        void extractsConfigPagePath() {
            new FavroIssueProviderType(pluginDescriptorMock); // Act
            Mockito.verify(pluginDescriptorMock).getPluginResourcesPath(eq(configUrl)); // Assert
        }
        @Test
        void extractsIssuePagePath() {
            new FavroIssueProviderType(pluginDescriptorMock); // Act
            Mockito.verify(pluginDescriptorMock).getPluginResourcesPath(eq(issueUrl)); // Assert
        }
    }

    @Nested
    @DisplayName("getType")
    class GetType {
        @Test
        void returnsFavroIssues() {
            FavroIssueProviderType sut = new FavroIssueProviderType(pluginDescriptorMock); // Act
            assertEquals("FavroIssues", sut.getType()); // Assert
        }
    }

    @Nested
    @DisplayName("getDisplayName")
    class GetDisplayName {
        @Test
        void returnsFavro() {
            FavroIssueProviderType sut = new FavroIssueProviderType(pluginDescriptorMock); // Act
            assertEquals("Favro", sut.getDisplayName()); // Assert
        }
    }

    @Nested
    @DisplayName("getParametersUrl")
    class GetParametersUrl {
        final String resourcePath = "resources/admin/editIssueProvider.jsp";
        @BeforeEach
        void setUp() {
            Mockito.when(pluginDescriptorMock.getPluginResourcesPath(configUrl)).thenReturn(resourcePath);
        }
        @Test
        void returnsFavro() {
            FavroIssueProviderType sut = new FavroIssueProviderType(pluginDescriptorMock); // Act
            assertEquals(resourcePath, sut.getEditParametersUrl()); // Assert
        }
    }

    @Nested
    @DisplayName("getIssueDetailsUrl")
    class GetIssueDetailsUrl {
        final String resourcePath = "resources/popup.jsp";
        @BeforeEach
        void setUp() {
            Mockito.when(pluginDescriptorMock.getPluginResourcesPath(issueUrl)).thenReturn(resourcePath);
        }
        @Test
        void returnsFavro() {
            FavroIssueProviderType sut = new FavroIssueProviderType(pluginDescriptorMock); // Act
            assertEquals(resourcePath, sut.getIssueDetailsUrl()); // Assert
        }
    }

    @Nested
    @DisplayName("getDefaultProperties")
    class GetDefaultProperties {
        FavroIssueProviderType sut;
        @BeforeEach
        void setUp() {
            sut = new FavroIssueProviderType(pluginDescriptorMock);
        }
        @Test
        void setsDefaultValueForParamAuth() {
            Map<String, String> properties = sut.getDefaultProperties(); // Act
            assertEquals(AUTH_LOGIN_PASSWORD, properties.get(PARAM_AUTH_TYPE)); // Assert
        }
        @Test
        void setsDefaultPatternAuth() {
            Map<String, String> properties = sut.getDefaultProperties(); // Act
            assertEquals("#(\\w+-\\d+)", properties.get(PARAM_PATTERN)); // Assert
        }
    }
}
