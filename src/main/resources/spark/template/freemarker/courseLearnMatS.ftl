<!DOCTYPE html>
<html>

<head>
    <meta property="og:title"         content="My PLS" />
    <meta property="og:description"   content="Course" />
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Course Lessons - MyPLS</title>
</head>
<body>
<div id="fb-root"></div>
<script async defer crossorigin="anonymous" src="https://connect.facebook.net/en_US/sdk.js#xfbml=1&version=v9.0" nonce="FveZBD1v"></script>
<#include "navbar.ftl">
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <h2 class="card-header border-primary text-black-50 mb-3">
            ${name}
        </h2>
        <div style="width: 90%; margin-left: 2%">
            <#if role == "prof">
                <form method="post" action="/lesson/add/${courseNumber}">
                    <button type="submit" class="btn btn-primary" style="float: right; margin-right: 2%">Add Lesson</button>
                </form>
            </#if>
            <ul class="nav nav-tabs">
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/about/${courseNumber}">About</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" data-toggle="tab" href="/course/learnMat/${courseNumber}">Learning Material</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/quiz/${courseNumber}">Quizzes</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/grades/${courseNumber}">Grades</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/classlist/${courseNumber}">Classlist</a>
                </li>
                <#if viewRate??>
                    <li class="nav-item">
                        <a class="nav-link" data-toggle="tab" href="/course/rate/${courseNumber}">Rate</a>
                    </li>
                </#if>
            </ul>
            <div id="myTabContent" class="tab-content" style="margin:2%">
                <#if lessons??>
                    <#list lessons as lesson>
                        <#if role == "prof">
                            <div class="card" >
                                <form action="/upload/${courseNumber}/${lesson.id}" method="post" enctype="multipart/form-data">
                                    <label for="uploadFile" style="float:left; margin-top:10px; margin-left:5px; margin-right:1em;" name="lessonId" value="${lesson.id}"><b>Upload file for ${lesson.name}:</b></label>
                                    <input type="file" class="small" id="inputGroupFile02" style="float: left;margin-top:10px;" name="uploadFile" value ="">
                                    <button type="submit" class="btn-download" style="float: right; margin-top: 8px" name="uploadButton" value=""><i class="fa fa-upload"></i></button>
                                </form>
                            </div>
                        </#if>
                        <form style="display:flex; justify-content:center; width:100%;" method="post" action="/lesson/save/${courseNumber}">
                            <div class="tab-pane fade active show" id="learnMat" style="width:100%; margin:10px;">
                                <div id="accordion">
                                    <div class="card" >

                                        <div class="card-header" id="headingOne">

                                            <h5 class="mb-0">
                                    <span class="text-primary small" data-toggle="collapse" data-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                                        <#if role == "prof">

                                            <input name="name" pattern="\S.*\S" id="name" value="${lesson.name}">
                                        <#else>
                                            ${lesson.name}
                                        </#if>
                                    </span>
                                                <#if role == "prof">
                                                    <div style="float: right">
                                                        <button type="submit" class="btn btn-primary" style="float: right;" name="saveButton" value=${lesson.id}>Save</button>
                                                        <button class="btn-download" style="float: right; margin-top:5px;" name="deleteButton" value=${lesson.id}><i class="fa fa-trash"></i></button>
                                                        <button type="submit" class="btn-download" name="shareButton" value="true" style="margin-top:5px;"><i class="fa fa-share"></i></button>
                                                        <div class="fb-share-button" data-href="http://${ip}/course/learnMat/${courseNumber}" data-layout="button_count" data-size="small">
                                                            <a target="_blank" href="https://www.facebook.com/sharer/sharer.php?u=http%3A%2F%2F${ip}%3A8080%2Fcourse%2FlearnMat%2F${courseNumber}&amp;src=sdkpreparse" class="fb-xfbml-parse-ignore">
                                                                Share
                                                            </a>
                                                        </div>
                                                    </div>
                                                </#if>
                                            </h5>
                                        </div>
                                        <div id="collapseOne" class="collapse show" aria-labelledby="headingOne" data-parent="#accordion">
                                            <div class="card-body">
                                                <#if role == "prof">
                                                    <div class="mb-3">
                                                        <b>Requirements:</b>
                                                        <input style="width: 80%" name="req" pattern="\S.*\S" id="req" value="${lesson.requirements}">
                                                        <input style="display:none" name="lessonId" type="text" id="lessonId" value="${lesson.id}">
                                                    </div>
                                                <#else>
                                                    <div class="mb-3"> <b>Requirements:</b> ${lesson.requirements}</div>
                                                </#if>
                                                <ul class="list-group">
                                                    <#list lesson.materials as material>

                                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                                            <a href="/view/${courseNumber}/${material}"><input readonly style="border:none; text-decoration: underline; width:20em;" name="${material}" value="${material}"></a>
                                                            <div>
                                                                <button type="submit" class="btn-download" name="dlButton" value="${material}"><i class="fa fa-download" ></i></button>
                                                                <#if role == "prof">
                                                                    <button type="submit" class="btn-download" name="deleteLMButton" value="${material}"><i class="fa fa-trash"></i></button>
                                                                </#if>
                                                            </div>
                                                        </li>
                                                    </#list>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </#list>
                <#else>
                    No Lessons Available
                </#if>
            </div>
        </div>
    </div>
</div>
</body>
</html>