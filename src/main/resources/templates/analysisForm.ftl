<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Start an Analysis</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
          integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">
</head>

<body>

<div class="container" style="margin-top: 5%; margin-bottom: 5%">
    <h1>Start an Analysis</h1>

    <form action="/submit" method="post">

        <div class="form-group col-sm-10">
            <label for="gitRepo">Git Repository</label>
            <input type="text" class="form-control" id="gitRepo" name="gitRepo">
            <small class="form-text text-muted">Link to the git repository</small>
        </div>

        <hr>
        <p>
            <b>Important</b>: Only <u>Maven</u> projects using <u>JUnit4</u> are currently supported through this
            interface. Refer to the <a href="https://github.com/ZhongXiLu/MuRa" target=_blank>default command line
            tool</a> if you want to use other build tools.
        </p>
        <hr>

        <div class="form-group col-sm-10">
            <div class="custom-control custom-switch">
                <input type="checkbox" class="custom-control-input" id="singleModule" name="singleModule"
                       checked>
                <label class="custom-control-label" for="singleModule">Is your project a <b>single</b> module
                    project?</label>
            </div>
        </div>

        <div id="moduleSettings" style="display: none">
            <div class="form-group col-sm-10">
                <label for="module">Module</label>
                <input type="text" class="form-control" id="module" name="module" value="core">
                <small class="form-text text-muted">The module to analyze (currently only one is supported)</small>
            </div>
        </div>
        <hr>

        <div class="form-group col-sm-10">
            <div class="custom-control custom-switch">
                <input type="checkbox" class="custom-control-input" id="CK" name="CK" checked>
                <label class="custom-control-label" for="CK"><b>CK Ranker</b>: ranks mutants based on the CK metric
                    suite, i.e. CBO, DIT, WMC, RFC, and NOC.</label>
            </div>
        </div>
        <div class="form-group col-sm-10">
            <div class="custom-control custom-switch">
                <input type="checkbox" class="custom-control-input" id="CC" name="CC" checked>
                <label class="custom-control-label" for="CC"><b>Complexity Ranker</b>: ranks mutants based on the
                    cyclomatic complexity.</label>
            </div>
        </div>
        <div class="form-group col-sm-10">
            <div class="custom-control custom-switch">
                <input type="checkbox" class="custom-control-input" id="USG" name="USG" checked>
                <label class="custom-control-label" for="USG"><b>Usage Ranker</b>: ranks mutants based on their
                    usage.</label>
            </div>
        </div>
        <div class="form-group col-sm-10">
            <div class="custom-control custom-switch">
                <input type="checkbox" class="custom-control-input" id="H" name="H" checked>
                <label class="custom-control-label" for="H"><b>History Ranker</b>: ranks mutants based on their change
                    history.</label>
            </div>
        </div>
        <div class="form-group col-sm-10">
            <div class="custom-control custom-switch">
                <input type="checkbox" class="custom-control-input" id="LC" name="LC" checked>
                <label class="custom-control-label" for="LC"><b>Coverage Ranker</b>: ranks mutants based on the achieved
                    line coverage.</label>
            </div>
        </div>
        <div class="form-group col-sm-10">
            <div class="custom-control custom-switch">
                <input type="checkbox" class="custom-control-input" id="IMP" name="IMP">
                <label class="custom-control-label" for="IMP"><b>Impact Ranker</b>: ranks mutants based on their impact.
                    <i class="text-muted">-- Highly recommended to turn this <b>off</b> for performance reasons
                    (turning this on means running every test for each mutant leading to very long run times) and the
                    impact did not have significant influence on the importance of a mutant in an earlier
                    study</i></label>
            </div>
        </div>

        <hr>
        <div class="form-group col-sm-10">
            <div class="custom-control custom-switch">
                <input type="checkbox" class="custom-control-input" id="optimalWeights" name="optimalWeights" checked>
                <label class="custom-control-label" for="optimalWeights">Use <b>optimal weights</b> when ranking the
                    mutants? <i class="text-muted">-- Note that these weights can be modified afterwards in either case.
                        These optimal weights were found in an earlier study and were based on a few projects and are
                        therefore not necessarily suitable for your project, but are nevertheless "better" than the
                        default weights (i.e. equal weights).</i></label>
            </div>
        </div>
        <hr>
        <br>

        <button type="submit" class="btn btn-primary">Start Analyzing</button>
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"
        integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"
        integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV"
        crossorigin="anonymous"></script>

<script type="text/javascript">
function toggleElementById(elementId) {
    const visible = document.getElementById(elementId).style.display;
    document.getElementById(elementId).style.display = (visible == "" ? "none" : "");
}

$("#singleModule").change(function() {
    toggleElementById('moduleSettings');
});
</script>

</body>
</html>