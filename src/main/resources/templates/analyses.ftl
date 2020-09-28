<#list analyses as analysis>
<div class="card
<#if !analysis.isFinished()>
    border-dark
<#elseif analysis.isSuccessful()>
    border-success
<#else>
    border-danger
</#if>
">
    <h5 class="card-header">
        ${analysis.getRepoName()}
        <#if !analysis.isFinished()>
            <span class="badge badge-dark">currently analyzing</span>
        <#elseif analysis.isSuccessful()>
            <span class="badge badge-success">finished</span>
        <#else>
            <span class="badge badge-danger">failed: ${analysis.getErrorMessage()}</span>
        </#if>
    </h5>
    <div class="card-body">
        <table class="table table-borderless">
            <tr>
                <td><b>Start time</b></td>
                <td>${analysis.getStartTime()}</td>
            </tr>
            <tr>
                <td><b>Git repository</b></td>
                <td><a href="${analysis.getGitRepo()}" target="_blank">${analysis.getGitRepo()}</a></td>
            </tr>
            <#if analysis.isFinished()>
            <tr>
                <td><b>Report link</b></td>
                <td><a href='${"/analysis/" + analysis.getRepoName() + "/" + analysis.getReport()}'>${analysis.getReport()}</a></td>

            </tr>
            <tr>
                <td><b>Mutants link</b></td>
                <td><a href='${"/analysis/" + analysis.getRepoName() + "/" + analysis.getMutants()}'>${analysis.getMutants()}</a></td>
            </tr>
            </#if>
        </table>
    </div>
</div>
<br>
</#list>