<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Course Rate - MyPLS</title>
</head>
<body>
<div class="space-between" style=" margin: 2% 0 0 7%">
    <div style="width: 45%">
        <h4 class="card-header border-primary text-black-50 mb-3">
            Rate Course
        </h4>
        <form method="post" action="/course/rate/${courseId}" class="mb-3">
            <div class="tab-content" style="margin:2%">
                <p class="mb-3" style="margin-bottom: 2%">
                    <b>Current Rating:</b>
                    <#if course_rate??>
                        <#if course_rate.rating == 0>
                            No Rating Found
                        <#else>
                            <#list 1..course_rate.rating as i>
                                <span class="fa fa-star checked large"></span>
                            </#list>
                            <#list 1..course_rate.unchecked as j>
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
                        <input type="radio" id="star1" name="Rating" class="star" value="5">
                        <label for="star1" class="star" title="1 stars"></label>
                        <input type="radio" id="star2" name="Rating" class="star" value="4">
                        <label for="star2" class="star" title="4 stars"></label>
                        <input type="radio" id="star3" name="Rating" class="star" value="3">
                        <label for="star3" class="star" title="3 stars"></label>
                        <input type="radio" id="star4" name="Rating" class="star" value="2">
                        <label for="star4" class="star" title="4 stars"></label>
                        <input type="radio" id="star5" name="Rating" class="star" value="1">
                        <label for="star5" class="star" title="5 stars"></label>
                    </div>
                </div>
                <div class="mb-3" style="display: flex;margin-bottom: 2%">
                    <label style="margin-right: 2%" for="marks">Feedback</label>
                    <input required pattern="\S.*\S" name="feedback" class="form-control col-10" id="feedback">
                </div>
                <button
                        type="submit"
                        name="doneRating"
                        class="btn btn-primary mb-3"
                        value="${courseId}"
                        style="float: right"
                >
                    Rate
                </button>
            </div>
        </form>
    </div>
    <div style="width: 45%">
        <h4 class="card-header border-primary text-black-50 mb-3">
            Rate Professor
        </h4>
        <form method="post" action="/course/rate/${courseId}" class="mb-3">
            <div class="tab-content" style="margin:2%">
                <p class="mb-3" style="margin-bottom: 2%">
                    <b>Current Rating:</b>
                    <#if prof??>
                        <#if prof.rating == 0>
                            No Rating Found
                        <#else>
                            <#list 1..prof.rating as i>
                                <span class="fa fa-star checked large"></span>
                            </#list>
                            <#list 1..prof.unchecked as j>
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
                    <input required pattern="\S.*\S" name="feedback" class="form-control col-10" id="feedback">
                </div>
                <button
                        type="submit"
                        name="doneRatingProf"
                        class="btn btn-primary mb-3"
                        value="${profId}"
                        style="float: right"
                >
                    Rate
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>