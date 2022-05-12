<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Ratings - MyPLS</title>
</head>
<body>
<div style="display: flex;justify-content: center">
    <div class="card text-black mb-3" style="width:100%; border: none">
        <#if user_details??>
            <h2 class="card-header border-primary text-black-50 mb-3 space-between">
                ${user_details.name}
                <a class="text-white" href="/ratings">
                    <button class="btn btn-primary mb-3" style="float: left">
                        Back
                    </button>
                </a>
            </h2>

            <div style="width: 90%; margin-left: 2%">
                <form method="post" action="/ratings" class="mb-3">
                    <div class="tab-content" style="margin:2%">
                            <p class="mb-3" style="margin-bottom: 2%">
                                <b>Current Rating:</b>
                                <#if user_details.rating == 0>
                                    <span class="fa fa-star large"></span>
                                    <span class="fa fa-star large"></span>
                                    <span class="fa fa-star large"></span>
                                    <span class="fa fa-star large"></span>
                                    <span class="fa fa-star large"></span>
                                <#else>
                                    <#list 1..user_details.rating as i>
                                        <span class="fa fa-star checked large"></span>
                                    </#list>
                                    <#if user_details.unchecked != 0>
                                    <#list 1..user_details.unchecked as j>
                                        <span class="fa fa-star large"></span>
                                    </#list>
                                    </#if>
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
                                <input required type="text" name="feedback" class="form-control col-3" id="feedback">
                            </div>
                            <button
                                    type="submit"
                                    name="doneRating"
                                    class="btn btn-primary mb-3 col-2"
                                    value="${curr_user}"
                                    style="float: right"
                            >
                                Rate
                            </button>
                    </div>
                </form>
            </div>
        <#else>
            <div>Something went wrong, please try again later.</div>
        </#if>
    </div>
</div>
</body>
</html>