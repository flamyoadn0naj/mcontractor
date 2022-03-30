<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Edit Question - MyPLS</title>
</head>
<body>
<#include "navbar.ftl">
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <h2 class="card-header border-primary text-black-50 mb-3">
            ${title}
        </h2>
        <div style="width: 90%; margin-left: 2%">
            <#if role == "prof">
                <a href="/course/create-question?courseId=${courseId}&lessonId=${lessonId}&quizId=${quizId}&e=-1" class="btn btn-primary" style="float: right">Add</a>
                <a href="/course/quiz/${courseId}" class="btn btn-primary"
                   style="float:
                right">Back</a>
            </#if>
            <ul class="nav nav-tabs">
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/about/${courseId}">About</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/learnMat/${courseId}">Learning Material</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" data-toggle="tab" href="/course/quiz/${courseId}">Quizzes</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/grades/${courseId}">Grades</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="/course/classlist/${courseId}">Classlist</a>
                </li>
                <#if viewRate??>
                    <li class="nav-item">
                        <a class="nav-link" data-toggle="tab" href="/course/rate/${courseId}">Rate</a>
                    </li>
                </#if>
            </ul>

            <form style="display:flex; justify-content:center;" method="post" action="/course/create-question?courseId=${courseId}&lessonId=${lessonId}&quizId=${quizId}&e=${e}">

                <div class="card" style="width: 90%">
                    <div class="card-header" id="headingOne">
                        <h5>
                            <div class="space-between">
                                <div style="display: flex">
                                    <label style="margin-right: 2%" for="total">Total Marks</label>
                                    <input disabled type="number" name="total" class="form-control col-4" id="total"  value=${tot!""}>
                                </div>
                                <div style="display: flex">
                                    <label style="margin-right: 2%" for="min">Minimum Score</label>
                                    <input disabled type="number" name="min" class="form-control col-4" id="min"  value=${min!""}>
                                </div>
                            </div>
                        </h5>
                        <hr>
                        <h6 class="space-between">
                            <#--                                        <div class="form-group space-between mb-3">-->
                            <#--                                            <label class="padding2right col-3" for="1a">Question:</label>-->

                            <#--                                            <input required type="text" name="questionText" class="form-control col-9" id="questionText" <#if question ??>value=${question.questionText!""}</#if>>-->

                            <#--                                        </div>-->
                            <div style="display: flex">
                                <label style="margin-right: 2%" for="marks">Marks</label>
                                <input required type="number" name="marks" class="form-control col-3" id="marks"  <#if question ??>value=${question.mark!""}</#if> >
                            </div>
                        </h6>
                        <#--                                    <div class="form-group mb3">-->
                        <#--                                        <input required type="text" name="QText" class="form-control" id="1"  <#if question ??>value=${question.questionText!""}</#if> >-->
                        <#--                                    </div>-->

                        <div class="form-group space-between">
                            <label class="padding2right" for="1a">Question</label>
                            <input required type="text" name="QText" class="form-control" id="1"  <#if question ??>value="${question.questionText!""}"</#if> >
                        </div>

                        <div class="form-group space-between">
                            <label class="padding2right" for="1a">A</label>
                            <input required type="text" name="QA" class="form-control" id="QA"  <#if question ??>value="${question.responseA!""}"</#if> >
                        </div>
                        <div class="form-group space-between">
                            <label class="padding2right" for="1b">B</label>
                            <input required type="text" name="QB" class="form-control" id="QB"  <#if question ??>value="${question.responseB!""}"</#if> >
                        </div>
                        <div class="form-group space-between">
                            <label class="padding2right" for="1c">C</label>
                            <input required type="text" name="QC" class="form-control" id="QC"  <#if question ??>value="${question.responseC!""}"</#if> >
                        </div>
                        <div class="form-group space-between mb3">
                            <label class="padding2right" for="1d">D</label>
                            <input required type="text" name="QD" class="form-control" id="QD"  <#if question ??>value="${question.responseD!""}"</#if> >
                        </div>
                        <div class="form-group space-between">
                            <label class="padding2right" for="ans1">Answer</label>
                            <input required type="text" name="ans" class="form-control" id="ans"  <#if question ??>value="${question.answer!""}"</#if> >
                        </div>
                        <#if question ??>
                            <button type="submit" name="action" value=${questionId} class="btn btn-primary" style="width:10rem; float:right;">
                                Save
                            </button>
                        <#else>
                            <button type="submit" name="action" value="AddQ" class="btn btn-primary" style="width:10rem; float:right; margin-right: 2%">
                                Add Question
                            </button>
                        </#if>
                    </div>
                </div>
            </form>


            <#--                <div class="card" style="width: 80%">-->
            <#--                    <div class="card-header" id="headingOne">-->
            <#--                        <div class="form-group space-between mb-3">-->
            <#--                            <label class="padding2right col-3" for="1a">Question:</label>-->

            <#--                            <input required type="text" name="questionText" class="form-control col-9" id="questionText" value="${question.questionText}">-->

            <#--                        </div>-->
            <#--                        <div class="form-group mb-3" style="display: flex">-->
            <#--                            <label class="col-3" for="1b">Associated Lesson:</label>-->
            <#--                            <select class="form-control col-9" name="linkedLesson" id="exampleSelect1" style="margin-right: 2%">-->
            <#--                                <option value=-1>None</option>-->
            <#--                                <#list lessons as lesson>-->
            <#--                                    <option value=${lesson.id}>${lesson.name}</option>-->
            <#--                                </#list>-->
            <#--                                <#if currLesson??>-->
            <#--                                    <option selected value=${currLesson}>${currLessonName}</option>-->
            <#--                                </#if>-->
            <#--                            </select>-->
            <#--                        </div>-->

            <#--            <div id="myTabContent" class="tab-content" style="margin:2%">-->
            <#--                <div class="tab-pane fade active show" id="quiz">-->
            <#--                    <table class="table table-bordered">-->
            <#--                        <tr class="table-primary">-->
            <#--                            <th scope="col">Question name</th>-->
            <#--                            <th scope="col">Mark</th>-->
            <#--                            <th scope="col">Action</th>-->
            <#--                        </tr>-->
            <#--                        <#if questions??>-->
            <#--                            <#list questions as k,c>-->
            <#--                                <tr>-->
            <#--                                    <th scope="row"><a class="text-muted" href="${courseId}/${c.quizId}/${k}">${c.questionText}</a></th>-->
            <#--                                    <td>${c.mark}</td>-->
            <#--                                    <td>-->
            <#--                                        <#if role == "prof">-->
            <#--                                            <a href="/course/${courseId}/create-quiz?courseId=${courseId}&quizId=${c.quizId!""}&e=1" class="btn btn-primary" style="float: right"><i class="fa fa-edit"></i></a>-->
            <#--                                        <#else>-->
            <#--                                            <#if c.status == 1>-->
            <#--                                                <button type="button" class="btn btn-primary">Take</button>-->
            <#--                                            </#if>-->
            <#--                                        </#if>-->
            <#--                                    </td>-->
            <#--                                </tr>-->
            <#--                            </#list>-->

            <#--                        <#else>-->
            <#--                            <tr>-->
            <#--                                <div style="margin-left: 2%"> No Questions Found. Please add questions using the "Add"-->
            <#--                                    button.</div>-->
            <#--                            </tr>-->
            <#--                        </#if>-->
            <#--                    </table>-->

            <#--                        <tr>-->
            <#--                            <th scope="row">Quiz 1</th>-->
            <#--                            <td>100</td>-->
            <#--                            <td>80</td>-->
            <#--                            <td>85</td>-->
            <#--                            <td>-->
            <#--                                <#if role == "prof">-->
            <#--                                    <button type="button" class="btn-download" style="float: right">-->
            <#--                                        <i class="fa fa-edit"></i>-->
            <#--                                    </button>-->
            <#--                                    <button type="button" class="btn-download" style="float: right">-->
            <#--                                        <i class="fa fa-share"></i>-->
            <#--                                    </button>-->
            <#--                                <#else>-->
            <#--                                    <button type="button" class="btn btn-primary">Take</button>-->
            <#--                                </#if>-->
            <#--                            </td>-->
            <#--                        </tr>-->
            <#--                        <tr>-->
            <#--                            <th scope="row">Quiz 2</th>-->
            <#--                            <td>100</td>-->
            <#--                            <td>70</td>-->
            <#--                            <td>85</td>-->
            <#--                            <td>-->
            <#--                                <#if role == "prof">-->
            <#--                                    <button type="button" class="btn-download" style="float: right">-->
            <#--                                        <i class="fa fa-edit"></i>-->
            <#--                                    </button>-->
            <#--                                    <button type="button" class="btn-download" style="float: right">-->
            <#--                                        <i class="fa fa-share"></i>-->
            <#--                                    </button>-->
            <#--                                <#else>-->
            <#--                                    <button type="button" class="btn btn-primary">Take</button>-->
            <#--                                </#if>-->
            <#--                            </td>-->
            <#--                        </tr>-->
            <#--                        <tr>-->
            <#--                            <th scope="row">Midterm 1</th>-->
            <#--                            <td>200</td>-->
            <#--                            <td>150</td>-->
            <#--                            <td>180</td>-->
            <#--                            <td>-->
            <#--                                <#if role == "prof">-->
            <#--                                    <button type="button" class="btn-download" style="float: right">-->
            <#--                                        <i class="fa fa-edit"></i>-->
            <#--                                    </button>-->
            <#--                                    <button type="button" class="btn-download" style="float: right">-->
            <#--                                        <i class="fa fa-share"></i>-->
            <#--                                    </button>-->
            <#--                                <#else>-->
            <#--                                    <button type="button" class="btn btn-primary">Take</button>-->
            <#--                                </#if>-->
            <#--                            </td>-->
            <#--                        </tr>-->
            <#--                    </table>-->
            <#--                    <div class="mb-3" style="display: flex; justify-content: space-between">-->
            <#--                        <span>-->
            <#--                            Lesson #1-->
            <#--                        </span>-->
            <#--                        <span>-->
            <#--                            50/100-->
            <#--                        </span>-->
            <#--                        <#if role == "prof">-->
            <#--                            <button type="button" class="btn btn-primary">Edit</button>-->
            <#--                        <#else>-->
            <#--                            <button type="button" class="btn btn-primary">Take</button>-->
            <#--                        </#if>-->
            <#--                    </div>-->
            <#--                    <div class="mb-3" style="display: flex; justify-content: space-between">-->
            <#--                        <span>-->
            <#--                            Lesson #2-->
            <#--                        </span>-->
            <#--                        <span>-->
            <#--                            30/100-->
            <#--                        </span>-->
            <#--                        <#if role == "prof">-->
            <#--                            <button type="button" class="btn btn-primary">Edit</button>-->
            <#--                        <#else>-->
            <#--                            <button type="button" class="btn btn-primary">Take</button>-->
            <#--                        </#if>-->
            <#--                    </div>-->
            <#--                    <div class="mb-3" style="display: flex; justify-content: space-between">-->
            <#--                        <span>-->
            <#--                            Lesson #3-->
            <#--                        </span>-->
            <#--                        <span>-->
            <#--                            90/100-->
            <#--                        </span>-->
            <#--                        <#if role == "prof">-->
            <#--                            <button type="button" class="btn btn-primary">Edit</button>-->
            <#--                        <#else>-->
            <#--                            <button type="button" class="btn btn-primary">Take</button>-->
            <#--                        </#if>-->
            <#--                    </div>-->
        </div>
    </div>
</div>
</div>
</div>
</body>
</html>