<%!
void processVerifiedClaims(org.json.simple.JSONObject baseObj)
{
  org.json.simple.JSONArray verifiedClaims = (org.json.simple.JSONArray)baseObj.get("verified_claims");

  for(Object verifiedClaim : verifiedClaims)
  {
    org.json.simple.JSONObject verifiedClaimObj = (org.json.simple.JSONObject) verifiedClaim;
    org.json.simple.JSONObject claims = (org.json.simple.JSONObject)verifiedClaimObj.get("claims");
    if(claims.containsKey("given_name"))
    {
      claims.put("given_name", "Tam");
    }
    if(claims.containsKey("family_name"))
    {
      claims.put("family_name", "Tran");
    }
    if(claims.containsKey("birthdate"))
    {
      claims.put("birthdate", "1980-01-01");
    }
  }
}
%>
<%


  java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
  java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();

  String requestBody = org.apache.commons.io.IOUtils.toString(request.getInputStream());

  org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();

  org.json.simple.JSONObject requestJSON = (org.json.simple.JSONObject)parser.parse(requestBody);

  String requestValue = String.valueOf(requestJSON.get("request"));

  String decodedRequestValue = new String(decoder.decode(java.net.URLDecoder.decode(requestValue, "UTF-8")));

  org.json.simple.JSONObject requestValueJSON = (org.json.simple.JSONObject)parser.parse(decodedRequestValue);

  org.json.simple.JSONObject idToken = (org.json.simple.JSONObject)requestValueJSON.get("id_token");
  processVerifiedClaims(idToken);

  org.json.simple.JSONObject userinfo = (org.json.simple.JSONObject)requestValueJSON.get("userinfo");
  processVerifiedClaims(userinfo);

  String newResponse = requestValueJSON.toJSONString();
  String encodedNewResponse = encoder.encodeToString(newResponse.getBytes());

  requestJSON.put("response", encodedNewResponse);

  response.setContentType("application/json");
%>

<%=requestJSON%>
