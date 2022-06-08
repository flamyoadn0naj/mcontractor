<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Enroll - MyPLS</title>
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

                    <p><b>Professor:</b> ${c.getProfessorName()}</p>
                    <p><b>Meeting Days:</b> ${c.getMeeting_days()}</p>
                    <p><b>Meeting Time:</b> ${c.getStartTime()} - ${c.getEndTime()} </p>
                    <p><b>Credits:</b> ${c.getCredits()}</p>
                    <p><b>Pre-requisite:</b> ${c.getPreReqName()}</p>
                    <p><b>Requirements:</b> To pass, you must get a C minimum</p>
                    <p><b>Learning Objectives:</b> ${c.getLearningObj()}</p>
                    <p><b>Total Capacity:</b> ${c.getCapacity()}</p>
                    <p><b>Seats Available:</b> 5</p>
                    <div style="display: flex">
                        <b>Rating: </b>
                        <div class="mb-3" style="margin-left: 5pt; width: 125pt; display: flex; justify-content: space-evenly">
                            <#if course.getRating() == 0>
                                <div style="text-align: center; margin-top: 2%"> No Rating Found </div>
                            <#else>
                                <#list 1..course.getRating() as i>
                                    <span class="fa fa-star checked large"></span>
                                </#list>
                                <#if course.getUnchecked() !=0>
                                    <#list 1..course.getUnchecked() as j>
                                        <span class="fa fa-star large"></span>
                                    </#list>
                                </#if>
                            </#if>
                        </div>
                    </div>
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