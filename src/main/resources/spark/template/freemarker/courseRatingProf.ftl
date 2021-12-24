<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
<div id="myTabContent" class="tab-content">
    <div class="tab-pane fade active show" id="rate">
        <form method="post" action="/course/rate/${courseId}">
            <#if rateLearner??>
                <div class="tab-content">
                    <h4 style="margin: 2% 0 2% 0; font-weight: 500">
                        ${learner_name}
<#--                        <a class="text-white" href="/course/rate/${courseId}">-->
<#--                            <button class="btn btn-primary mb-3" style="float: right">-->
<#--                                Back-->
<#--                            </button>-->
<#--                        </a>-->
                    </h4>
                    <hr />
                    <p class="mb-3" style="margin-bottom: 2%">
                        <b>Current Rating:</b>
                        <#if learner??>
                            <#if learner.rating == 0>
                                No Rating Found
                            <#else>
                                <#list 1..learner.rating as i>
                                    <span class="fa fa-star checked large"></span>
                                </#list>
                                <#list 1..learner.unchecked as j>
                                    <span class="fa fa-star large"></span>
                                </#list>
                            </#if>
                        <#else>
                            No Rating Found
                        </#if>
                    </p>
                    <div class="mb-3" style="display: flex" style="margin-bottom: 2%">
                        <b style="float: left">Your Rating : </b>
                        <div style="float: left">
                            <input type="radio" id="star11" name="Rating" class="star" value="5">
                            <label for="star11" class="star" title="1 stars"></label>
                            <input type="radio" id="star22" name="Rating" class="star" value="4">
                            <label for="star22" class="star" title="4 stars"></label>
                            <input type="radio" id="star33" name="Rating" class="star" value="3">
                            <label for="star33" class="star" title="3 stars"></label>
                            <input type="radio" id="star44" name="Rating" class="star" value="2">
                            <label for="star44" class="star" title="4 stars"></label>
                            <input type="radio" id="star55" name="Rating" class="star" value="1">
                            <label for="star55" class="star" title="5 stars"></label>
                        </div>
                    </div>
                    <div class="mb-3" style="display: flex;margin-bottom: 2%">
                        <label style="margin-right: 2%" for="marks">Feedback</label>
                        <input required type="text" name="feedback" class="form-control col-8" id="feedback">
                    </div>
                    <button
                            type="submit"
                            name="doneRating"
                            class="btn btn-primary mb-3 col-2"
                            value="${learnerId}"
                            style="float: right"
                    >
                        Rate
                    </button>
                </div>
            <#else>
                <table class="table table-bordered">
                    <tr class="table-primary">
                        <th scope="col">Name</th>
                        <th scope="col">Action</th>
                    </tr>
                    <#list classList as k,v>
                        <tr>
                            <th scope="row">${v.name}</th>
                            <td>
                                <button type="submit" name="rateLearner" class="btn btn-primary mb-3 col-2" value="${k}">
                                    Rate
                                </button>
                            </td>
                        </tr>
                    </#list>
                </table>
            </#if>
        </form>
    </div>
</div>
</body>
</html>