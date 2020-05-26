<%@include file="config.jsp" %>

<!DOCTYPE html>

<%
		//String accessToken = getAccessToken();
		
		Map<String, String> headers = new HashMap<String, String>();

		headers.put("ping.uname", pfRefUsername);
		headers.put("ping.pwd", pfRefPassword);
		headers.put("ping.instanceId", pfRefAdapter);

		String refToken = "";
		String consentJson = "";
		String openbanking_intent_id = java.util.UUID.randomUUID().toString();
		String userSubject = "";
		String ref = request.getParameter("REF");
		
		List<String> userAccounts = new ArrayList<String>();
		userAccounts.add("accounta");
		userAccounts.add("accountb");
		userAccounts.add("accountc");

		String [] selectedAccounts = request.getParameterValues("accounts");

		if(selectedAccounts == null)
			selectedAccounts = new String[]{};

		String resumePath = request.getParameter("resumePath");

		// Process User Response
		if (ref == null || ref.equals("")) {

			String consentAction = request.getParameter("Consent");
			openbanking_intent_id = request.getParameter("openbanking_intent_id");
			userSubject = request.getParameter("userSubject");

			//TODO fix bad bad hardcoded stuff!!
			String consentText = "Yes, I consent";
			
			if(consentAction != null && consentAction.equals("Approve"))
				consentAction = "Authorised";
			else if (consentAction == null || !consentAction.equals("Authorised"))
			{
				consentText = "No, I do not consent";
				consentAction = "Rejected";
			}
			
			String consentPurpose = "This is a dynamic client registration demo.";
			String actor = "PingFederate";
			
			boolean hasInvalidAccount = false;
			
			List<String> accounts = new ArrayList<String>();
			for(String currentAccount : selectedAccounts)
			{
			
				//something is suspicious here, let's error
/*
				if(!isValidAccount(accessToken, userSubject, currentAccount, request))
				{
					hasInvalidAccount = true;
					break;
				}
*/				
				accounts.add(currentAccount);
			}
			
			if(hasInvalidAccount)
			{
				consentAction = "Rejected";
				consentText = "Account mismatch during consent";
				//updateConsentObject(accessToken, request, openbanking_intent_id, consentAction, consentPurpose, actor, accounts, userSubject, consentText);
				
			}
			else
			{
				String subject = userSubject;
			
				//if(!updateConsentObject(accessToken, request, openbanking_intent_id, consentAction, consentPurpose, actor, accounts, userSubject, consentText))
					consentAction = "Rejected";
			}
			
			String referenceValue = dropoffRef(request, response, headers, consentAction, accounts);
			response.sendRedirect(resumePath + "?REF=" + referenceValue);
		} 
		// Initial Page Load
		else {

				JSONObject incomingREFObj = pickupRef(request, headers);

				refToken = incomingREFObj.toJSONString();

				if (incomingREFObj.containsKey("signedreqattr.claims"))
				{
/*
						JSONObject consentObj = (JSONObject)getConsentDetails(accessToken, request, openbanking_intent_id).get("Data");

						if(!consentObj.get("Status").toString().equals("AwaitingAuthorisation"))
						{
								String referenceValue = setReferenceValue(request, response, headers, "IncorrectState", null);
								response.sendRedirect(resumePath + "?REF=" + referenceValue);
						}

						consentJson = consentObj.toJSONString();
*/
				}

				if (incomingREFObj.containsKey("chainedattr.entryUUID"))
				{
					userSubject = incomingREFObj.get("chainedattr.entryUUID").toString();
					
					//userAccounts = getAccounts(accessToken, userSubject, request);
				}
		}
%>		

<html lang="en">
    <HEAD>
	
        <script type="text/javascript">
function syntaxHighlight(json) {
    if (typeof json != 'string') {
         json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
}

	</script>
        <style>
        	
        	body, html {   
				width: 100%;
				margin: 0;
				padding: 0;
				display:table;
				font-family: "ProximaNovaRegular", helvetica, arial, sans-serif;
			}
			body {
				display:table-cell;
				vertical-align:middle;
				text-align: center;
				background-color: #fff;
			}

		a {
			text-decoration: none;
			color: #611c4a;
		}   
        	.header{
				width: 100%;
				background-color: #3d454d;
				height: 100px;
				padding: 0px;
				text-align: center;
			}
			
			.content {
				width: 700px;
				padding: 0px;
				padding-bottom: 100px;
				margin: auto;
				text-align: center;
				background-color: #fff;
				padding-top: 30px;
			}
			
			.outerContent {
				width: 100%;
				text-align: center;
				background-color: #fff;
			}
			
			h1
			{
				margin: 0px;
			}
			
			input {
				width: 325px;
				height: 20px;
				font-size: 15px;
				padding: 10px;
				margin-top: 5px;
				font-family: "ProximaNovaRegular", helvetica, arial, sans-serif;
			}
			
			input[type=button],input[type=submit]
			{
				border: 1px solid #2996cc;
				background-color: #2996cc;
				width: 350px;
				height: 40px;
				padding: 0px;
				color: #fff;
				font-weight: bold;
				font-family: "ProximaNovaRegular", helvetica, arial, sans-serif;
			}
			
			input[type=checkbox]{
				width: 20px;
			}
			
			input.logout{
				border: 1px solid #959595;
				background-color: #d0d0d0;
			}
			
			.json
			{
				text-align: left;
				overflow: scroll;
				padding: 3px;
				margin: 24px;
				overlay: scroll;
				background-color: #f9fafa;
				border: 1px solid lightgray;
			}


			
			.json
			{
				width: 600px;
				text-align: left;
				overflow: scroll;
				padding: 3px;
				margin: 24px;
				overlay: scroll;
				background-color: #f9fafa;
				border: 1px solid lightgray;
			}

/* Customize the label (the chkboxContainer) */
.chkboxContainer {
  display: block;
  position: relative;
  padding-left: 35px;
  margin-bottom: 12px;
  cursor: pointer;
  font-size: 22px;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

label.chkboxContainer
{
	font-weight: 200;
}
/* Hide the browser's default checkbox */
.chkboxContainer input {
  position: absolute;
  opacity: 0;
  cursor: pointer;
}

/* Create a custom checkbox */
.checkmark {
  position: absolute;
  top: 0;
  left: 0;
  height: 25px;
  width: 25px;
  background-color: #eee;
}

/* On mouse-over, add a grey background color */
.chkboxContainer:hover input ~ .checkmark {
  background-color: #ccc;
}

/* When the checkbox is checked, add a blue background */
.chkboxContainer input:checked ~ .checkmark {
  background-color: #2196F3;
}

/* Create the checkmark/indicator (hidden when not checked) */
.checkmark:after {
  content: "";
  position: absolute;
  display: none;
}

/* Show the checkmark when checked */
.chkboxContainer input:checked ~ .checkmark:after {
  display: block;
}

/* Style the checkmark/indicator */
.chkboxContainer .checkmark:after {
  left: 9px;
  top: 5px;
  width: 5px;
  height: 10px;
  border: solid white;
  border-width: 0 3px 3px 0;
  -webkit-transform: rotate(45deg);
  -ms-transform: rotate(45deg);
  transform: rotate(45deg);
}

.chkboxDiv
{

	width: 300px;
	margin: auto;
	margin-top: 30px;
}
			
        </style>

    </HEAD>

    <BODY>
<div class="header">
&nbsp;</div>
<div class="outerContent">
  <div class="content">
        <h1>Select Accounts</h1>

	<form method="POST">
	<div>
		<h2>Requested Permissions</h2><span id="permissionView"/>
	</div>
	<div class="chkboxDiv">
		<h2>Select Accounts</h2>
		
<%
	for(String accountId : userAccounts)
	{
%>
<label class="chkboxContainer"><%=accountId%>
  <input type="checkbox" value="<%=accountId%>" name="accounts">
  <span class="checkmark"></span>
</label>

<%
	}
%>
	</div>

		<!-- TODO HACK: Managing state in a very hacky way, but hey, it's just a demo -->
		<!-- Maybe think of a better way to preserve these values -->
		<input type="hidden" name="resumePath" value="<%=resumePath%>"/>
		<input type="hidden" name="openbanking_intent_id" value="<%=openbanking_intent_id%>"/>
		<input type="hidden" name="userSubject" value="<%=userSubject%>"/>
		<input type="Submit" name="Consent" value="Approve"/>
		<input type="Submit" name="Deny" value="Deny"/>
		
	</form>
  </div>

<script type="text/javascript">
function displayConsent()
{
	document.getElementById("btnHideConsent").style.display = "block";
	document.getElementById("consentContext").style.display = "block";
	document.getElementById("btnDisplayConsent").style.display = "none";
}

function hideConsent()
{
        document.getElementById("btnHideConsent").style.display = "none";
        document.getElementById("consentContext").style.display = "none";
        document.getElementById("btnDisplayConsent").style.display = "block";
}

function displayAuthnCtx()
{
        document.getElementById("btnHideAuthnCtx").style.display = "block";
        document.getElementById("authenticationContext").style.display = "block";
        document.getElementById("btnDisplayAuthnCtx").style.display = "none";
}

function hideAuthnCtx()
{
        document.getElementById("btnHideAuthnCtx").style.display = "none";
        document.getElementById("authenticationContext").style.display = "none";
        document.getElementById("btnDisplayAuthnCtx").style.display = "block";
}
</script>
<div>
<a href="#" onclick="displayConsent()" id="btnDisplayConsent">Display Consent</a>
<a href="#" onclick="hideConsent()" id="btnHideConsent" style="display:none">Hide Consent</a>
  <pre class="json" id="consentContext" style="display:none"></pre>
</div>
<div>
<a href="#" onclick="displayAuthnCtx()" id="btnDisplayAuthnCtx">Display Authentication Context</a>
<a href="#" onclick="hideAuthnCtx()" id="btnHideAuthnCtx" style="display:none">Hide Authentication Context</a>
  <pre class="json" id="authenticationContext" style="display:none"></pre>
</div>
<script type="text/javascript">
document.getElementById("consentContext").innerHTML = syntaxHighlight(<%=consentJson%>);
document.getElementById("authenticationContext").innerHTML = syntaxHighlight(<%=refToken%>);

var consentDetails = <%=consentJson%>;

document.getElementById("permissionView").innerHTML = consentDetails.Permissions;
</script>

</div>
    </BODY>
</html>