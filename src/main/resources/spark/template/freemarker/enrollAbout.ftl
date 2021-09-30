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
            Enroll
        </h2>
        <h4 class="card-header border-primary text-black-50 mb-3" style="margin-top: -1.5%">
            Introduction to Software Engineering
        </h4>
        <div style="width: 90%; margin-left: 2%">
            <div id="myTabContent" class="tab-content" style="margin:2%">
                <div class="tab-pane fade active show" id="about">

                    <p><b>Professor:</b> ${profName}</p>
                    <p><b>Meeting Days:</b> ${meeting_days}</p>
                    <p><b>Meeting Time:</b> ${start_time} - ${end_time} </p>
                    <p><b>Credits:</b> ${credits}</p>
                    <p><b>Pre-requisite:</b> ${prereq_course}</p>
                    <p><b>Requirements:</b> To pass, you must get a C minimum</p>
                    <p><b>Learning Objectives:</b> ${obj}</p>
                    <p><b>Total Capacity:</b> ${cap}</p>
                    <p><b>Seats Available:</b> 5</p>
                    <p><b>Rating:</b>
                        <span class="fa fa-star checked "></span>
                        <span class="fa fa-star checked "></span>
                        <span class="fa fa-star checked "></span>
                        <span class="fa fa-star checked"></span>
                        <span class="fa fa-star "></span>
                    </p>
                    <p class="mb-3"><b>Schedule Compatability:</b> Yes</p>
                    <button type="button" class="btn btn-primary mb-3" style="float: right">
                        Enroll
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>