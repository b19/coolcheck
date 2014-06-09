<%@page import="com.google.appengine.api.datastore.Entity"%>
<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<link rel="stylesheet"
	href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
<style>
.cool-list {
	display: block;
	width: 100%;
	padding: 10px;
	text-align: center;
	font-size: large;
}

.cool-list:hover {
	color: #fff;
	background-color: #428bca;
}

#cool-btn {
	display: block;
	position: fixed;
	width: 100%;
	border-radius: 0;
	border: 0;
	bottom: 0;
	left: 0;
}
</style>
</head>
<body>
<div style="margin-bottom:50px;">
	<h1
		style="background-color: #eee; color: #555; margin: 0 0 20px 0; padding: 10px;">COOL-CHECK</h1>
	<%Iterator itr = (Iterator<Entity>)request.getAttribute("stampList"); %>
	<%Entity e;%>
	<%while(itr.hasNext()){%>
	<%e = (Entity)itr.next();%>
	<a class="cool-list" data-target="#cool-modal-event"
		data-toggle="modal" href="#"
		onclick="showPosition(<%=e.getProperty("alt")%>,<%=e.getProperty("lon")%>)">
		<%=e.getProperty("date")%>에 <%=e.getProperty("name")%>님
		출첵!
	</a>
	<%} %>
	<form action="/" method="post" enctype="application/x-www-form-urlencoded">
		<input type="hidden" id="cool-lat" name="alt"> <input
			type="hidden" id="cool-lon" name="lon"> <input type="hidden"
			name="name" value="${user}"> <input type="submit"
			class="btn btn-primary btn-lg" id="cool-btn" value="STAMPING">
	</form>

	<div class="modal fade" id="cool-modal-event" tabindex="-1"
		role="dialog" aria-labelledby="cool-modal-eventLabel"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="cool-modal-eventLabel">WHERE ARE
						YOU?</h4>
				</div>
				<div class="modal-body" id="cool-mapholder"
					style="text-align: center;"></div>
			</div>
		</div>
	</div>
</div>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script
		src="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	<script type="text/javascript">
			if (navigator.geolocation) {
				navigator.geolocation.getCurrentPosition(setPosition,
						alertError);
			} else {
				alert("GPS 정보를 받아올 수 없는 구형 브라우저 입니다.");//사용자가 위치정보에 대한 요청을 거부했을 때.
			}
			function setPosition(position) {
				$("#cool-lat").val(position.coords.latitude);
				$("#cool-lon").val(position.coords.longitude);
			}
			function showPosition(lat, lon) {
				$(".cool-pos").remove();
				$("#cool-mapholder")
						.append(
								"<img class='cool-pos' style='max-width:100%;height:auto' src=http://maps.googleapis.com/maps/api/staticmap?center="
										+ lat
										+ ","
										+ lon
										+ "&zoom=18&size=400x300&sensor=false>");
				return false;
			}
			function alertError(error) {
				$("#cool-btn").addClass("disabled");
				switch (error.code) {
				case error.PERMISSION_DENIED:
					alert("위치 정보에 대한 접근이 금지되어 있습니다.");
					break;
				case error.POSITION_UNAVAILABLE:
					alert("위치 정보 사용이 불가능합니다.");
					break;
				case error.TIMEOUT:
					alert("TIMEOUT 되었습니다.");
					break;
				case error.UNKNOWN_ERROR:
					alert("시스템이 불안정합니다. 잠시 후 다시 시도해 주십시오.");
					break;
				}
			}
		</script>
</body>
</html>