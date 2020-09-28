export class AnalysisForm {
    gitRepo: string;

    singleModule: boolean = true;
    module: string = "core";

    CK: boolean = true;
    CC: boolean = true;
    USG: boolean = true;
    H: boolean = true;
    LC: boolean = true;
    IMP: boolean = false;

    optimalWeights: boolean = true;
}
