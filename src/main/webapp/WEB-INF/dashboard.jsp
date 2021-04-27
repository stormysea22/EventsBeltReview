<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
    <h1>Welcome ${loggedinuser.firstName}</h1>
    <a href="/logout">Logout</a>

    <h4>Here are some of the events in your state:</h4>
    <table class="table">
        <thead class="thead-dark">
            <tr>
                <th scope="col">Name</th>
                <th scope="col">Date</th>
                <th scope="col">Location</th>
                <th scope="col">Host</th>
                <th scope="col">Action/Status</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="event" items="${eventsIn }">
                <tr>
                    <th><a href="/events/${event.id}">
                            <c:out value="${event.name}" /></a></th>
                    <td>
                        <fmt:formatDate pattern="MMMM dd, yyyy" value="${event.date}" />
                    </td>
                    <td>
                        <c:out value="${event.city}" />
                    </td>
                    <td>
                        <c:out value="${event.host.firstName}" />
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${event.host.id==loggedinuser.id}">
                                <a href="/events/${event.id }/edit">Edit</a>
                                <a href="/events/${event.id }/delete">Delete</a>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${event.attendees.contains(loggedinuser) }">

                                        <span>Joined <a href="/events/${event.id}/remove">Cancel</a></span>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="/events/${event.id }/join">Join</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <h4>Here are some of the events in other states:</h4>
    <table class="table">
        <thead class="thead-dark">
            <tr>
                <th scope="col">Name</th>
                <th scope="col">Date</th>
                <th scope="col">Location</th>
                <th scope="col">Host</th>
                <th scope="col">Action/Status</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="e" items="${eventsOut }">
                <tr>
                    <th><a href="/events/${e.id}">
                            <c:out value="${e.name}" /></a></th>
                    <td>
                        <fmt:formatDate pattern="MMMM dd, yyyy" value="${e.date}" />
                    </td>
                    <td>
                        <c:out value="${e.city}" />
                    </td>
                    <td>
                        <c:out value="${e.host.firstName}" />
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${e.host.id==loggedinuser.id }">
                                <a href="/events/${e.id }/edit">Edit</a>
                                <a href="/events/${e.id }/delete">Delete</a>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${e.attendees.contains(loggedinuser) }">
                                        <span>Joined <a href="/events/${e.id}/remove">Cancel</a></span>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="/events/${e.id }/join">Join</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <div class="createEvent">
        <h3>Create an Event</h3>
        <form:form method="POST" action="/events/create" modelAttribute="event">
            <form:hidden path="host" value="${loggedinuser.id}" />
            <form:errors path="host" />
            <p>
                <form:label class="col-sm-2 col-form-label" path="name">Event Name:</form:label>
                <form:errors path="name" />
                <form:input class="form-control col-sm-6" type="text" path="name" />
            </p>
            <p>
                <form:label class="col-sm-2 col-form-label" path="date">Date:</form:label>
                <form:errors path="date" />
                <form:input class="form-control col-sm-6" type="date" path="date" />
                <p>
                    <form:label class="col-sm-2 col-form-label" path="city">Location:</form:label>
                    <form:errors path="city" />
                    <form:input class="form-control col-sm-6" type="text" path="city" />
                </p>
                <p>
                    <form:label class="col-sm-2 col-form-label" path="state">State:</form:label>
                    <form:select path="state">
                        <form:errors path="state" />
                        <c:forEach items="${states}" var="state">
                            <form:option value="${state}">${state}</form:option>
                        </c:forEach>
                    </form:select>
                </p>
                <input class="btn btn-warning" type="submit" value="Create" />
        </form:form>
    </div>

    </div>

</body>

</html>