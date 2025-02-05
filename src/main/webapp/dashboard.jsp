<%--
  Created by IntelliJ IDEA.
  User: salonipriyani
  Date: 07/04/23
  Time: 5:29 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dashboard</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            padding: 8px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #4CAF50;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        caption {
            font-size: 24px;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<table id="log">
    <caption><b>Operational Metrics</b></caption>
    <tr>
        <th>Analysis</th>
        <th>Result</th>
    </tr>
    <tr>
        <td>Detect Language API Average time:</td>
        <td><%= request.getAttribute("averageDetectLang") %></td>
    </tr>
    <tr>
        <td>Translate API Average time:</td>
        <td><%= request.getAttribute("averageTranslate") %></td>
    </tr>
    <tr>
        <td>Browser in descending order of number of requests:</td>
        <td><%= request.getAttribute("descBrowserList") %></td>
    </tr>
</table>

<br>

<table>
    <caption><b>Application Logs</b></caption>
    <tr>
        <th>Date</th>
        <th>Browser</th>
        <th>Device</th>
        <th>Text to detect</th>
        <th>Detected Language</th>
        <th>Converted Text</th>
    </tr>
    <%= request.getAttribute("logs") %>
</table>
</body>
</html>