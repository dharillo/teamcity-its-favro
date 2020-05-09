<%@ page import="jetbrains.buildServer.web.util.SessionUser" %>
<%@ include file="/include.jsp"%>
<%@ include file="providerConstants.jsp"%>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags"%>

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

<jsp:useBean id="providerType" scope="request" type="com.dharillo.teamcity.favro.FavroIssueProviderType"/>

<script type="text/javascript">
  (function() {
    BS.FavroIssues = {
      selectedAuth: undefined,
      selector: undefined,

      init: function(select) {
        this.selector = $(select);
        this.selectAuthType();
      },

      selectAuthType: function() {
        this.selectedAuth = this.selector.value;
        this.onTypeChanged();
      },

      onTypeChanged: function() {
        var s = '.' + this.selectedAuth;
        $j('.js_authsetting')
                .filter(s).removeClass('hidden').end()
                .not(s).addClass('hidden');
        BS.MultilineProperties.updateVisible();
      }
    };
  })();
</script>

<div>
  <table class="editProviderTable">
    <%--@elvariable id="showType" type="java.lang.Boolean"--%>
    <c:if test="${showType}">
      <tr>
        <th><label class="shortLabel">Connection Type:</label></th>
        <td><bs:out value=" ${providerType.displayName}"/></td>
      </tr>
    </c:if>
    <tr>
      <th><label for="${name}" class="shortLabel">Display Name:<l:star/></label></th>
      <td>
        <props:textProperty name="${name}" maxlength="100"/>
        <span id="error_${name}" class="error"></span>
      </td>
    </tr>
    <tr>
      <th><label for="${organization}" class="shortLabel">Organization Id:<l:star/></label></th>
      <td>
        <props:textProperty name="${organization}" maxlength="100"/>
        <span id="error_${organization}" class="error"></span>
      </td>
    </tr>
    <tr>
      <th><label for="${authType}_select">Authentication:</label></th>
      <td>
        <props:selectProperty name="${authType}"
                              id="${authType}_select"
                              onchange="BS.FavroIssues.selectAuthType();">
          <props:option value="${authLoginPassword}">Username / Password</props:option>
          <props:option value="${authAccessToken}">Username / Token</props:option>
        </props:selectProperty>
        <span id="error_${authType}" class="error"></span>
      </td>
    </tr>
    <tr>
      <th><label for="${username}" class="shortLabel">Username:<l:star/></label></th>
      <td>
        <props:textProperty name="${username}" maxlength="100"/>
        <span id="error_${username}" class="error"></span>
      </td>
    </tr>
    <tr class="js_authsetting ${authLoginPassword}">
      <th><label for="${password}" class="shortLabel">Password:<l:star/></label></th>
      <td>
        <props:passwordProperty name="${password}" maxlength="100"/>
        <span id="error_${password}" class="error"></span>
      </td>
    </tr>

    <tr class="js_authsetting ${authAccessToken}">
      <th><label for="${accessToken}" class="shortLabel">Access token:<l:star/></label></th>
      <td>
        <props:passwordProperty name="${accessToken}" maxlength="100"/>
        <span class="fieldExplanation">Favro <a href="https://help.ally.io/en/articles/3080813-favro-generating-an-api-token"> access token</a></span>
        <span id="error_${accessToken}" class="error"></span>
      </td>
    </tr>
  </table>
</div>

<script type="text/javascript">
  BS.FavroIssues.init('${authType}_select');
</script>