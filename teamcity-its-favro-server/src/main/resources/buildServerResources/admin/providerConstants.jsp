<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.dharillo.teamcity.favro.FavroConstants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  ~ Copyright 2000-2020 JetBrains s.r.o.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<c:set var="name" value="<%=FavroConstants.PARAM_NAME%>"/>
<c:set var="authType" value="<%=FavroConstants.PARAM_AUTH_TYPE%>"/>
<c:set var="username" value="<%=FavroConstants.PARAM_USERNAME%>"/>
<c:set var="password" value="<%=FavroConstants.PARAM_PASSWORD%>"/>
<c:set var="accessToken" value="<%=FavroConstants.PARAM_ACCESS_TOKEN%>"/>
<c:set var="organization" value="<%=FavroConstants.PARAM_ORGANIZATION%>"/>


<c:set var="authLoginPassword" value="<%=FavroConstants.AUTH_LOGIN_PASSWORD%>"/>
<c:set var="authAccessToken" value="<%=FavroConstants.AUTH_ACCESS_TOKEN%>"/>