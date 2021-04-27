<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Project</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css"
        integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l" crossorigin="anonymous">
</head>

<body>
    <div class="container">
        <h1 class="display-2">
            <c:out value="${event.name}" />
        </h1>
        <a href="/events">Home</a>
        <h5>Host:
            <c:out value="${event.host.firstName}" />
        </h5>
        <h5>Date:
            <fmt:formatDate pattern="MMMM dd, yyyy" value="${event.date}" />
        </h5>
        <h5>Location:
            <c:out value="${event.city}" />
        </h5>
        <h5>People who are attending this event:
            <c:out value="${event.attendees.size()}" />
        </h5>
        <table class="table eventTable">
            <thead class="thead-dark">
                <tr>
                    <th scope="col">Name</th>
                    <th scope="col">Location</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="attendees" items="${event.attendees }">
                    <tr>
                        <th>
                            <c:out value="${attendees.firstName}" />
                            <c:out value="${attendees.lastName}" />
                        </th>
                        <td>
                            <c:out value="${attendees.city}" />
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

    </div>
</body>

</html>