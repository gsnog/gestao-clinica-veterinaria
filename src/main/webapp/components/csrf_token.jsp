<%@ page import="com.uff.gestaoclinicaveterinaria.util.CsrfUtil" %><input type="hidden" name="_csrf" value="<%= CsrfUtil.obterToken(session) %>"/>
