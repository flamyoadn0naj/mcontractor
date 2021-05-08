<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
<#include "navbar.ftl">
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <h2 class="card-header border-primary text-black-50 mb-3">
            Courses
        </h2>
        <div style="width: 90%; margin-left: 2%">
            <table class="table table-bordered">
            <tr class="table-primary">
                <th scope="col">Name</th>
                <th scope="col">Professor</th>
                <th scope="col">Start Date</th>
                <th scope="col">End Date</th>
            </tr>
            <tr>
                <th scope="row">Introduction to Software Engineering</th>
                <td>AbdulMutalib Wahaishi</td>
                <td>September 2020</td>
                <td>December 2020</td>
            </tr>
            <tr>
                <th scope="row">Colloquium</th>
                <td>Pengchang Shi</td>
                <td>September 2020</td>
                <td>December 2020</td>
            </tr>
            <tr>
                <th scope="row">Programming Language Concepts</th>
                <td>Matthew Fluet</td>
                <td>September 2020</td>
                <td>December 2020</td>
            </tr>
            <tr>
                <th scope="row">Human Computer Interaction</th>
                <td>Matt Huenerfauth</td>
                <td>January 2020</td>
                <td>May 2020</td>
            </tr>
            <tr>
                <th scope="row">Machine Learning</th>
                <td>Christopher M. Homan</td>
                <td>January 2020</td>
                <td>May 2020</td>
            </tr>
            </table>
        </div>
    </div>
</div>
</body>
</html>