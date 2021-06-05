<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
    <#include  "navbar.ftl">
    <#if notAuthorized == "true">
        <div class="alert alert-dismissible alert-danger">
            <strong>Not Authorized! </strong>
            You have been redirected to the home page as you were not authorized to view the page you selected.
        </div>
    </#if>
    <div style="display: flex;justify-content: center">
        <div class="card text-black mb-3" style="width:100%; border: none">
            <h2 class="card-header text-black-50 mb-3" style="padding:10pt">
                Welcome, Maheen Contractor!
            </h2>
            <div style="display: flex; justify-content: space-evenly">
                <div class="card text-black border-primary mb-3" style="width:30%; margin-top:5%">
                    <h2 class="card-header text-white bg-primary mb-3" style="padding:10pt">
                        Courses
                    </h2>
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item">Introduction to Software Engineering</li>
                        <li class="list-group-item">Colloquium</li>
                        <li class="list-group-item">Programming Language Concepts</li>
                        <li class="list-group-item">Human Computer Interaction</li>
                        <li class="list-group-item">Machine Learning</li>
                    </ul>
                </div>
                <div class="card text-black border-primary mb-3" style="width:30%; margin-top:5%">
                    <h2 class="card-header text-white bg-primary mb-3" style="padding:10pt">
                        Discussion Groups
                    </h2>
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item">Introduction to Software Engineering</li>
                        <li class="list-group-item">Trekking</li>
                        <li class="list-group-item">Swimming</li>
                        <li class="list-group-item">Foodies in RIT</li>
                        <li class="list-group-item">Machine Learning</li>
                    </ul>
                </div>
                <div class="card text-black border-primary mb-3" style="width:30%; margin-top:5%">
                    <h2 class="card-header text-white bg-primary mb-3" style="padding:10pt">
                        Rating
                    </h2>
                    <div style="display: flex; justify-content: space-evenly;">
                        <span class="fa fa-star checked large"></span>
                        <span class="fa fa-star checked large"></span>
                        <span class="fa fa-star checked large"></span>
                        <span class="fa fa-star large"></span>
                        <span class="fa fa-star large"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>