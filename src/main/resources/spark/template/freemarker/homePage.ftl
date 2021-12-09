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
    <#if notAuthorized??>
        <div class="alert alert-dismissible alert-danger">
            <strong>Error! </strong>
            ${notAuthorized}
        </div>
    </#if>
    <div style="display: flex;justify-content: center">
        <div class="card text-black mb-3" style="width:100%; border: none">
            <#if name??>
                <h2 class="card-header text-black-50 mb-3" style="padding:10pt">
                    Welcome, ${name}
                </h2>
            <#else>
                <h2 class="card-header text-black-50 mb-3" style="padding:10pt">
                    Welcome!
                </h2>
            </#if>
            <div style="display: flex; justify-content: space-evenly">
                <div class="card text-black border-primary mb-3" style="width:30%; margin-top:5%">
                    <h2 class="card-header text-white bg-primary mb-3" style="padding:10pt">
                        Courses
                    </h2>
                    <#if courses??>
                    <ul class="list-group list-group-flush">
                        <#list courses as k,c>
                            <li class="list-group-item"><a class="text-muted" style="font-weight: bold" href="course/about/${k}">${c.name}</a></li>
                        </#list>
                    </ul>
                    <#else>
                        <div style="text-align: center; margin-top: 2%"> You are not enrolled in any courses </div>
                    </#if>
                </div>
                <div class="card text-black border-primary mb-3" style="width:30%; margin-top:5%">
                    <h2 class="card-header text-white bg-primary mb-3" style="padding:10pt">
                        Discussion Groups
                    </h2>
                    <#if groups??>
                    <table class="table" style="border: none">
                        <tr class="table-primary justify-content-between">
                        </tr>
                            <#list groups as g>
                                <tr>
                                    <th scope="row">
                                        <a class="text-muted" href="/discussion/group-desc/${g.id}">${g.name}</a>
                                        <#if g.privacy??>
                                            <button type="button" class="btn-download" style="margin-left: 2%">
                                                <i class="fa fa-lock"></i>
                                            </button>
                                        </#if>
                                        <#if g.course??>
                                            <span class="badge badge-primary" style="margin-left: 2%">Course</span>
                                        </#if>
                                    </th>
                                </tr>
                            </#list>
                    </table>
                    <#else>
                        <div style="text-align: center; margin-top: 2%"> You are not a member of any group </div>
                    </#if>
                </div>
                <div class="card text-black border-primary mb-3" style="width:30%; margin-top:5%">
                    <h2 class="card-header text-white bg-primary mb-3" style="padding:10pt">
                        Rating
                    </h2>
                    <#if rating??>
                        <div class="mb-3" style="margin-bottom: 2%; margin-top: 2%; display: flex; justify-content: space-evenly">
                        <#if rating.rating == 0>
                            <div style="text-align: center; margin-top: 2%"> No Rating Found </div>
                        <#else>
                            <#list 1..rating.rating as i>
                                <span class="fa fa-star checked large"></span>
                            </#list>
                            <#list 1..rating.unchecked as j>
                                <span class="fa fa-star large"></span>
                            </#list>
                        </#if>
                        </div>
                        <div class="mb-2" style="margin-left: 10%">
                            <ul class="list-group list-group-flush">
                            <#list rating.feedback as f>
                                <#if f?counter < 5>
                                    <li class="list-group-item" style="display: flex">
                                    <div> ${f?counter}. </div>
                                    <div style="margin-left: 4%">${f}</div>
                                    </li>
                                </#if>
                            </#list>
                            </ul>
                        </div>
                    <#else>
                        <div style="text-align: center; margin-top: 2%"> No Rating Found </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>
    <#if role == "learner">
        <div class="footer bg-primary text-white">
            <a class="text-white" href="/apply-prof"> Apply to be a Professor </a>
        </div>
    </#if>
</body>
</html>